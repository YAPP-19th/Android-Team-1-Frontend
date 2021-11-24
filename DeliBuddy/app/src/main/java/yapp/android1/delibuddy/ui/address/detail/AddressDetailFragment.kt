package yapp.android1.delibuddy.ui.address.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressDetailBinding

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressDetailViewModel by viewModels()
    private val latLng = LatLng(37.54626561463918, 127.07269333978718)

    //    private val actViewModel: LocationViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMap()
        initObserve()
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

        map.cameraPosition = CameraPosition(latLng, 16.0)

        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(R.drawable.icon_marker)
        marker.map = map
    }

    private fun initObserve() {

    }
}