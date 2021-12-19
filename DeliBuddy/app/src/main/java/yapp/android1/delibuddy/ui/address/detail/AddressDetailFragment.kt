package yapp.android1.delibuddy.ui.address.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentAddressDetailBinding
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.ui.address.AddressSharedEvent
import yapp.android1.delibuddy.ui.address.AddressSharedViewModel
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import kotlin.math.abs

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressSharedViewModel by activityViewModels()
    private val ABOUT_ZERO = 0.000000000001
    private var naverMap: NaverMap? = null
    private var isSelectedCurrentLocation = false
    private lateinit var locationListener: LocationSource.OnLocationChangedListener
    private lateinit var locationSource: FusedLocationSource

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isSelectedCurrentLocation = viewModel.isCurrentLocation.value
        Timber.w("isSelectedCurrentLocation: $isSelectedCurrentLocation")
        initLocationObject()
        initView()
        initMap()
        initObserve()
    }

    private fun initLocationObject() {
        locationListener = LocationSource.OnLocationChangedListener { location ->
            Timber.w("location listener")
            if (location == null) return@OnLocationChangedListener
            naverMap?.cameraPosition = CameraPosition(LatLng(location), 16.0)
        }
        locationSource = FusedLocationSource(this, 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap!!.locationTrackingMode = LocationTrackingMode.None
                showPermissionDeniedDialog()
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initView() = with(binding) {
        activateAddressView(viewModel.selectedAddress.value)

        btnAddressDetail.setOnClickListener {
            val selectedAddress = viewModel.selectedAddress.value
            selectedAddress.addressDetail = etAddressDetail.text.toString()

            val intent = Intent()
            intent.putExtra(
                AddressActivity.ADDRESS_ACTIVITY_USER_ADDRESS,
                selectedAddress
            )
            requireActivity().setResult(AddressActivity.ADDRESS_ACTIVITY_RESULT_CODE, intent)
            requireActivity().finish()
        }

        ivIconCurrentLocation.setOnClickListener {
            locationSource.activate(locationListener)
            Timber.w("locationSource-ivIconCurrentLocation: ${locationSource.isActivated}")
        }

        ivIconBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showPermissionDeniedDialog() {
        val permissionDialog = PermissionDialogFragment(requireActivity()).apply {
            negativeCallback = {
                Toast.makeText(context, "위치 권한이 없어 실행할 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
        permissionDialog.show(parentFragmentManager, null)
    }

    private fun initMap() = with(binding) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.fragment_map, it).commit()
            }
        mapFragment.getMapAsync(this@AddressDetailFragment)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap!!.locationSource = locationSource
        val mapUiSettings = map.uiSettings
        mapUiSettings.isScrollGesturesEnabled = true
        mapUiSettings.isTiltGesturesEnabled = false
        mapUiSettings.isRotateGesturesEnabled = false

        map.addOnCameraIdleListener {
            if (!isSelectedCurrentLocation) {
                locationSource.deactivate()
            }
            isSelectedCurrentLocation = false
            Timber.w("locationSource-cameraIdleListener: ${locationSource.isActivated}")
            if (!isSameCoordWithSearchResult(map.cameraPosition.target)) {
                getAddressFromCoord(
                    map.cameraPosition.target.latitude,
                    map.cameraPosition.target.longitude
                )
            } else {
                activateAddressView(viewModel.selectedAddress.value)
            }
        }

        if (isSelectedCurrentLocation) {
            locationSource.activate(locationListener)
            Timber.w("locationSource-currentLocation: ${locationSource.isActivated}")
        } else {
            map.cameraPosition = CameraPosition(
                LatLng(
                    viewModel.selectedAddress.value.lat,
                    viewModel.selectedAddress.value.lng
                ),
                16.0
            )
        }
    }

    private fun getAddressFromCoord(lat: Double, lng: Double) {
        viewModel.occurEvent(AddressSharedEvent.CoordToAddress(lat, lng))
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
