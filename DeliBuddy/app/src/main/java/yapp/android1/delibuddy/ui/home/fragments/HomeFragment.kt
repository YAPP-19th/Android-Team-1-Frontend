package yapp.android1.delibuddy.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentHomeBinding
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.address.AddressActivity
import yapp.android1.delibuddy.ui.createparty.CreatePartyActivity
import yapp.android1.delibuddy.ui.home.adapter.PartiesAdapter
import yapp.android1.delibuddy.ui.home.viewmodel.PartiesViewModel
import yapp.android1.delibuddy.ui.partyDetail.PartyDetailActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

typealias LocationRange = Pair<String, Int>

const val PARTY = "party"

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private val partiesViewModel: PartiesViewModel by viewModels()

    private val launcherForAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AddressActivity.ADDRESS_ACTIVITY_RESULT_CODE) {
            val data: Intent? = result.data
            val selectedAddress =
                data?.getParcelableExtra<Address>(AddressActivity.ADDRESS_ACTIVITY_USER_ADDRESS)
            selectedAddress?.let { address ->
//                partiesViewModel.occurEvent()
//                DeliBuddyApplication.prefs.saveUserAddress(address)
//                binding.tvUserAddress.text = address.addressName
                partiesViewModel.occurEvent(PartiesViewModel.PartiesEvent.SaveAddress(address))
            }
        }
    }

    private val partiesAdapter: PartiesAdapter by lazy {
        PartiesAdapter {
            adapterOnClick(it)
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
            val intent = Intent(requireContext(), AddressActivity::class.java)
            launcherForAddressActivity.launch(intent)
        }

        binding.buttonAddParty.setOnClickListener {
            val intent = Intent(activity, CreatePartyActivity::class.java)
            startActivity(intent)
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
                partiesAdapter.submitList(parties)
            }
        }

        repeatOnStarted {
            partiesViewModel.userAddress.collectLatest {
                binding.tvUserAddress.text = it.addressName
            }
        }
    }

    private fun adapterOnClick(party: Party) {
        val intent = Intent(activity, PartyDetailActivity::class.java)
        intent.putExtra(PARTY, party)
        startActivity(intent)
    }
}
