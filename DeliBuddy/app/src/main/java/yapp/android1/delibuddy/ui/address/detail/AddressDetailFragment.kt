package yapp.android1.delibuddy.ui.address.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressDetailBinding
import yapp.android1.delibuddy.ui.address.AddressSharedViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.domain.entity.Address

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressDetailViewModel by viewModels()
    private val addressSharedViewModel: AddressSharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initMap()
        initObserve()
    }

    private fun initView() = with(binding) {
        val addressDataFromSearchFragment: Address = addressSharedViewModel.selectedAddress.value!!
        tvAddressDetailName.text = addressDataFromSearchFragment.addressName
        if (addressDataFromSearchFragment.roadAddress != "") {
            tvAddress.text = addressDataFromSearchFragment.roadAddress
        } else {
            tvAddress.text = addressDataFromSearchFragment.address
        }

        btnAddressDetail.setOnClickListener {
            viewModel.occurEvent(
                AddressDetailEvent.SaveAddress(addressSharedViewModel.selectedAddress.value!!)
            )
        }
    }

    private fun initMap() = with(binding) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.fragment_map, it).commit()
            }
        mapFragment.getMapAsync(this@AddressDetailFragment)
    }

    override fun onMapReady(map: NaverMap) {
        val mapUiSettings = map.uiSettings
        mapUiSettings.isScrollGesturesEnabled = true
        mapUiSettings.isTiltGesturesEnabled = false
        mapUiSettings.isRotateGesturesEnabled = false

        map.cameraPosition = CameraPosition(
            LatLng(
                addressSharedViewModel.selectedAddress.value!!.lat,
                addressSharedViewModel.selectedAddress.value!!.lon
            ),
            16.0
        )

        map.addOnCameraIdleListener {
//            marker.position = map.cameraPosition.target
//            Timber.w("lat: ${marker.position.latitude}, lng: ${marker.position.longitude}")
//
//            val sharedAddress = LatLng(
//                addressSharedViewModel.selectedAddress.value!!.lat,
//                addressSharedViewModel.selectedAddress.value!!.lon
//            )

//            Timber.w("sharedAddress => lat: ${sharedAddress.latitude}, lng: ${sharedAddress.longitude}")
//            Timber.w("marker => lat: ${marker.position.latitude}, lng: ${marker.position.longitude}")

            viewModel.occurEvent(
                AddressDetailEvent.CoordToAddress(
//                    marker.position.latitude,
//                    marker.position.longitude
                    map.cameraPosition.target.latitude,
                    map.cameraPosition.target.longitude
                )
            )
        }
    }


    private fun initObserve() = with(binding) {
        repeatOnStarted {
            viewModel.addressResult.collect {
                if (it != null) {
                    activateAddressView(it)
                } else {
                    deactivateAddressView()
                }
            }
        }

        repeatOnStarted {
            viewModel.isActivate.collect {
                if (!it) {
                    deactivateAddressView()
                }
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun activateAddressView(address: Address) = with(binding) {
        tvAddressDetailName.text = address.addressName

        if (address.roadAddress != "") {
            tvAddress.text = address.roadAddress
        } else {
            tvAddress.text = address.address
        }

        etAddressDetail.isFocusableInTouchMode = true
        etAddressDetail.isFocusable = true

        btnAddressDetail.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.delibuddy_btn_radius)
        btnAddressDetail.isClickable = true
        btnAddressDetail.isEnabled = true
    }

    private fun deactivateAddressView() = with(binding) {
        tvAddressDetailName.text = "위치를 다시 지정해 주세요"

        tvAddress.text = "주소 정보가 없습니다"

        etAddressDetail.isClickable = false
        etAddressDetail.isFocusable = false

        btnAddressDetail.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.delibuddy_btn_radius_deactivate)
        btnAddressDetail.isClickable = false
        btnAddressDetail.isEnabled = false
    }
}