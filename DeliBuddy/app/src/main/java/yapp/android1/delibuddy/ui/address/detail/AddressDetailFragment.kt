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
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.ui.address.AddressSharedEvent
import yapp.android1.delibuddy.ui.address.AddressSharedViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import kotlin.math.abs

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressSharedViewModel by activityViewModels()
    private val ABOUT_ZERO = 0.000000000001

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initMap()
        initObserve()
    }

    private fun initView() = with(binding) {
        activateAddressView(viewModel.selectedAddress.value)

        btnAddressDetail.setOnClickListener {
            saveAddress()
        }
    }

    private fun saveAddress() {
        viewModel.occurEvent(
            AddressSharedEvent.SaveAddress(
                viewModel.selectedAddress.value,
                binding.etAddressDetail.text.toString()
            )
        )
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
                viewModel.selectedAddress.value.lat,
                viewModel.selectedAddress.value.lng
            ),
            16.0
        )

        map.addOnCameraIdleListener {
            if (!isSameCoordWithSearchResult(map.cameraPosition.target)) {
                viewModel.occurEvent(
                    AddressSharedEvent.CoordToAddress(
                        map.cameraPosition.target.latitude,
                        map.cameraPosition.target.longitude
                    )
                )
            } else {
                activateAddressView(viewModel.selectedAddress.value)
            }
        }
    }


    private fun initObserve() = with(binding) {
        repeatOnStarted {
            viewModel.isActivate.collect { isActivate ->
                if (!isActivate) deactivateAddressView()
            }
        }

        repeatOnStarted {
            viewModel.selectedAddress.collect { address ->
                activateAddressView(address)
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect { toastMsg ->
                Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isSameCoordWithSearchResult(latLng: LatLng): Boolean {
        return abs(viewModel.selectedAddress.value.lat - latLng.latitude) < ABOUT_ZERO
                && abs(viewModel.selectedAddress.value.lng - latLng.longitude) < ABOUT_ZERO

    }

    private fun activateAddressView(address: Address) = with(binding) {
        tvAddressDetailName.text = address.addressName

        if (address.roadAddress?.isNullOrBlank() == true) {
            tvAddress.text = address.address
        } else {
            tvAddress.text = address.roadAddress
        }

        etAddressDetail.setText("")
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

        etAddressDetail.setText("")
        etAddressDetail.isClickable = false
        etAddressDetail.isFocusable = false

        btnAddressDetail.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.delibuddy_btn_radius_deactivate)
        btnAddressDetail.isClickable = false
        btnAddressDetail.isEnabled = false
    }
}
