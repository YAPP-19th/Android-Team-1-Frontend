package yapp.android1.delibuddy.ui.home.fragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentHomeBinding
import yapp.android1.delibuddy.databinding.IncludeLayoutPartyItemBinding
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity
import yapp.android1.delibuddy.ui.dialog.PermissionDialogFragment
import yapp.android1.delibuddy.ui.home.adapter.PartiesAdapter
import yapp.android1.delibuddy.ui.home.viewmodel.PartiesViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.permission.PermissionManager
import yapp.android1.delibuddy.util.permission.PermissionState
import yapp.android1.delibuddy.util.permission.PermissionType

typealias LocationRange = Pair<String, Int>

const val PARTY = "party"

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val partiesViewModel: PartiesViewModel by viewModels()

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private var cancellationTokenSource = CancellationTokenSource()

    private val launcherForAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddressActivity.ADDRESS_ACTIVITY_RESULT_CODE) {
            val data: Intent? = result.data
            val selectedAddress =
                data?.getParcelableExtra<Address>(AddressActivity.ADDRESS_ACTIVITY_USER_ADDRESS)
            selectedAddress?.let { address ->
                partiesViewModel.occurEvent(
                    PartiesViewModel.UserAddressEvent.SaveUserAddress(address)
                )
            }
        }
    }

    private val partiesAdapter: PartiesAdapter by lazy {
        PartiesAdapter { binding, party ->
            adapterOnClick(binding, party)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserve()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        getCurrentAddressPartiesInCircle()
    }

    private fun initViews() {
        binding.rvParties.apply {
            adapter = partiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.groupCurrentAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressActivity::class.java)
            launcherForAddressActivity.launch(intent)
        }

        binding.buttonAddParty.setOnClickListener {
            val intent = Intent(activity, CreatePartyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCurrentAddressPartiesInCircle() {
        val address = DeliBuddyApplication.prefs.getCurrentUserAddress()

        if (address != null) {
            val point = "POINT (${address.lng} ${address.lat})"
            getPartiesInCircle(
                point,
                2000
            )
        } else {
            Toast.makeText(
                activity,
                R.string.address_call_fail,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getPartiesInCircle(point: String, distance: Int) {
        partiesViewModel.occurEvent(
            PartiesViewModel.PartiesEvent.GetPartiesInCircle(
                LocationRange(
                    point,
                    distance
                )
            )
        )
    }

    private fun initObserve() {
        repeatOnStarted {
            partiesViewModel.partiesResult.collect { parties ->
                checkPartiesEmpty(parties)
            }
        }

        repeatOnStarted {
            partiesViewModel.showToast.collect {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        }

        repeatOnStarted {
            partiesViewModel.userAddress.collectLatest {
                if (it.addressName == "주소를 입력해 주세요") {
                    getCurrentAddress()
                } else {
                    binding.tvUserAddress.text = it.addressName
                }
            }
        }

        repeatOnStarted {
            partiesViewModel.saveAddressEvent.collectLatest { event ->
                when (event) {
                    is PartiesViewModel.SaveAddressEvent.Success -> {
                        getCurrentAddressPartiesInCircle()
                    }
                    is PartiesViewModel.SaveAddressEvent.Failed -> {
                    }
                }
            }
        }
    }

    private fun checkPartiesEmpty(parties: List<Party>) {
        if (parties.isEmpty()) {
            binding.partiesEmptyFragment.visibility = View.VISIBLE
            binding.rvParties.visibility = View.INVISIBLE
        } else {
            binding.partiesEmptyFragment.visibility = View.INVISIBLE
            binding.rvParties.visibility = View.VISIBLE
            partiesAdapter.submitList(parties)
        }
    }

    private fun adapterOnClick(binding: IncludeLayoutPartyItemBinding, party: Party) {
        val intent = Intent(activity, PartyInformationActivity::class.java)
        intent.putExtra(PARTY, party)

        val pairFoodIcon = androidx.core.util.Pair<View, String>(
            binding.foodCategoryImage,
            binding.foodCategoryImage.transitionName
        )
        val pairTitle = androidx.core.util.Pair<View, String>(
            binding.partyTitle,
            binding.partyTitle.transitionName
        )
        val pairLocation = androidx.core.util.Pair<View, String>(
            binding.partyLocation,
            binding.partyLocation.transitionName
        )
        val pairTime = androidx.core.util.Pair<View, String>(
            binding.partyScheduledTime,
            binding.partyScheduledTime.transitionName
        )

        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            pairFoodIcon,
            pairTitle,
            pairLocation,
            pairTime
        )

        startActivity(intent, optionsCompat.toBundle())
    }

    private fun getCurrentAddress() {
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
                            val currentLatLng = Pair(result.latitude, result.longitude)
                            partiesViewModel.occurEvent(
                                PartiesViewModel.UserAddressEvent.GetCurrentAddress(currentLatLng)
                            )
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
                Toast.makeText(context, "위치 권한이 없어 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        permissionDialog.show(parentFragmentManager, null)
    }

    override fun onStop() {
        super.onStop()
        cancellationTokenSource.cancel()
    }
}
