package yapp.android1.delibuddy.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentHomeBinding
import yapp.android1.delibuddy.databinding.IncludeLayoutPartyItemBinding
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.ui.home.adapter.PartiesAdapter
import yapp.android1.delibuddy.ui.home.viewmodel.PartiesViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

typealias LocationRange = Pair<String, Int>

const val PARTY = "party"

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val partiesViewModel: PartiesViewModel by viewModels()

    private val launcherForAddressActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == AddressActivity.ADDRESS_ACTIVITY_RESULT_CODE) {
                binding.textCurrentAddress.text = activityResult.data?.getStringExtra(AddressActivity.ADDRESS_ACTIVITY_USER_ADDRESS)
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
        getPartiesInCircle()
    }

    private fun initViews() {
        binding.rvParties.apply {
            adapter = partiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.groupCurrentAddress.setOnClickListener {
            launcherForAddressActivity.launch(
                Intent(requireContext(), AddressActivity::class.java)
            )
        }

        binding.buttonAddParty.setOnClickListener {
            //파티 추가화면
        }
    }

    private fun getPartiesInCircle() {
        partiesViewModel.occurEvent(
            PartiesViewModel.PartiesEvent.GetPartiesInCircle(
                LocationRange(
                    "POINT (127.027779 37.497830)",
                    2000
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
                binding.textCurrentAddress.text = it
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

        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), pairFoodIcon,pairTitle,pairLocation,pairTime)

        startActivity(intent, optionsCompat.toBundle())
    }
}
