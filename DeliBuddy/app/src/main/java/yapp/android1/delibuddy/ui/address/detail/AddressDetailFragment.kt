package yapp.android1.delibuddy.ui.address.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressDetailBinding
import yapp.android1.delibuddy.ui.address.AddressSharedViewModel

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
        tvAddressDetailName.text = addressSharedViewModel.selectedAddress.value!!.addressName

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
        mapUiSettings.isScrollGesturesEnabled = false
        mapUiSettings.isTiltGesturesEnabled = false
        mapUiSettings.isRotateGesturesEnabled = false

        //val addressLatLng = LatLng(address!!.lat, address.lon)
        val addressLatLng = LatLng(
            addressSharedViewModel.selectedAddress.value!!.lat,
            addressSharedViewModel.selectedAddress.value!!.lon
        )
        map.cameraPosition = CameraPosition(addressLatLng, 16.0)

        val marker = Marker()
        marker.position = addressLatLng
        marker.icon = OverlayImage.fromResource(R.drawable.icon_marker)
        marker.map = map
    }

    private fun initObserve() {

    }
}