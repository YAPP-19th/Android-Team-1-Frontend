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
import timber.log.Timber
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressDetailBinding
import yapp.android1.delibuddy.ui.address.AddressSharedViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.domain.entity.Address
import kotlin.math.abs

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressDetailViewModel by viewModels()
    private val addressSharedViewModel: AddressSharedViewModel by activityViewModels()
    private val ABOUT_ZERO = 0.0000000000001

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initMap()
        initObserve()
    }

    private fun initView() = with(binding) {
        activateAddressView(addressSharedViewModel.selectedAddress.value)

        btnAddressDetail.setOnClickListener {
            viewModel.occurEvent(
                AddressDetailEvent.SaveAddress(addressSharedViewModel.selectedAddress.value)
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
                addressSharedViewModel.selectedAddress.value.lat,
                addressSharedViewModel.selectedAddress.value.lng
            ),
            16.0
        )

        map.addOnCameraIdleListener {
            Timber.w("camera => lat: ${map.cameraPosition.target.latitude}, lng: ${map.cameraPosition.target.longitude}")
            Timber.w("search => lat: ${addressSharedViewModel.selectedAddress.value.lat}, lng: ${addressSharedViewModel.selectedAddress.value.lng}")

            if (!isSameCoordWithSearchResult(map.cameraPosition.target)) {
                Timber.w("occur coord to address event")
                viewModel.occurEvent(
                    AddressDetailEvent.CoordToAddress(
                        map.cameraPosition.target.latitude,
                        map.cameraPosition.target.longitude
                    )
                )
            } else {
                activateAddressView(addressSharedViewModel.selectedAddress.value)
            }
        }
    }


    private fun initObserve() = with(binding) {
        repeatOnStarted {
            viewModel.addressResult.collect {
                if (it != null) {
                    if (!isSameAddressWithSearchResult(it)) {
                        activateAddressView(it)
                    } else {
                        activateAddressView(addressSharedViewModel.selectedAddress.value)
                    }
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

    private fun isSameAddressWithSearchResult(address: Address): Boolean {
        return addressSharedViewModel.selectedAddress.value.roadAddress == address.roadAddress
                || addressSharedViewModel.selectedAddress.value.address == address.address
    }

    private fun isSameCoordWithSearchResult(latLng: LatLng): Boolean {
        return abs(addressSharedViewModel.selectedAddress.value.lat - latLng.latitude) < ABOUT_ZERO
                && abs(addressSharedViewModel.selectedAddress.value.lng - latLng.longitude) < ABOUT_ZERO

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