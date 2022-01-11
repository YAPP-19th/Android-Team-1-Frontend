package yapp.android1.delibuddy.ui.address.detail

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
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
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType
import kotlin.math.abs

@AndroidEntryPoint
class AddressDetailFragment :
    BaseFragment<FragmentAddressDetailBinding>(FragmentAddressDetailBinding::inflate),
    OnMapReadyCallback {
    private val viewModel: AddressSharedViewModel by activityViewModels()
    private val ABOUT_ZERO = 0.000000000001
    private var naverMap: NaverMap? = null

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private var cancellationTokenSource = CancellationTokenSource()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initMap()
        initObserve()
    }

    private fun initView() = with(binding) {
        activateAddressView(viewModel.selectedAddress.value)

        btnAddressDetail.setOnClickListener {
            val selectedAddress = viewModel.selectedAddress.value
            selectedAddress.addressDetail = etAddressDetail.text.toString()

            val intent = Intent()
            intent.putExtra(AddressActivity.ADDRESS_ACTIVITY_USER_ADDRESS, selectedAddress)
            requireActivity().setResult(AddressActivity.ADDRESS_ACTIVITY_RESULT_CODE, intent)
            requireActivity().finish()
        }

        ivIconCurrentLocation.setOnClickListener {
            getCurrentLocation()
        }

        ivIconBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun getCurrentLocation() {
        PermissionManager.checkPermission(
            requireActivity() as AppCompatActivity,
            PermissionType.LOCATION
        ) {
            when (it) {
                PermissionState.GRANTED -> {
                    val currentLocationTask: Task<Location> =
                        fusedLocationClient.getCurrentLocation(
                            PRIORITY_HIGH_ACCURACY,
                            cancellationTokenSource.token
                        )

                    currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                        if (task.isSuccessful) {
                            val result: Location = task.result
                            val currentLatLng = LatLng(result.latitude, result.longitude)
                            naverMap?.cameraPosition = CameraPosition(currentLatLng, 16.0)
                        } else {
                            val exception = task.exception
                            Timber.w("Location Failure: $exception")
                        }
                    }
                }
                else -> showPermissionDeniedDialog()
            }
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
        val mapUiSettings = map.uiSettings
        mapUiSettings.isScrollGesturesEnabled = true
        mapUiSettings.isTiltGesturesEnabled = false
        mapUiSettings.isRotateGesturesEnabled = false

        if (viewModel.isCurrentLocation.value) {
            getCurrentLocation()
        } else {
            map.cameraPosition = CameraPosition(
                LatLng(viewModel.selectedAddress.value.lat, viewModel.selectedAddress.value.lng),
                16.0
            )
        }

        map.addOnCameraIdleListener {
            if (!isSameCoordWithSearchResult(map.cameraPosition.target)) {
                getAddressFromCoord(
                    map.cameraPosition.target.latitude,
                    map.cameraPosition.target.longitude
                )
            } else {
                activateAddressView(viewModel.selectedAddress.value)
            }
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

        if (address.roadAddress.isNullOrBlank()) {
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
