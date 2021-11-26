package yapp.android1.delibuddy.ui.address.detail

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressDetailBinding
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferenceHelper
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager
import yapp.android1.domain.entity.Address

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressDetailViewModel by viewModels()

    private lateinit var address: Address

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAddressData()
        initView()
        initMap()
        initObserve()
    }

    private fun initAddressData() {
        address = arguments?.getSerializable("address") as Address
    }

    private fun initView() = with(binding) {
        tvAddressDetailName.text = address.addressName

        btnAddressDetail.setOnClickListener {
            viewModel.occurEvent(AddressDetailEvent.SaveAddress(address))
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

        val addressLatLng = LatLng(address.lat, address.lon)
        map.cameraPosition = CameraPosition(addressLatLng, 16.0)

        val marker = Marker()
        marker.position = addressLatLng
        marker.icon = OverlayImage.fromResource(R.drawable.icon_marker)
        marker.map = map
    }

    private fun initObserve() {

    }
}