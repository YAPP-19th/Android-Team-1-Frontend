package yapp.android1.delibuddy.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentHomeBinding
import yapp.android1.delibuddy.databinding.IncludeLayoutPartyItemBinding
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.home.adapter.PartiesAdapter
import yapp.android1.delibuddy.ui.home.viewmodel.PartiesViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.intentTo

typealias LocationRange = Pair<String, Int>

const val PARTY = "party"

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {

    private val partiesViewModel: PartiesViewModel by viewModels()
    private val partiesAdapter: PartiesAdapter by lazy {
        PartiesAdapter { partyBinding, party ->
            adapterOnClick(partyBinding, party)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPartiesInCircle()

        initPartiesRecyclerView()
        setPartiesAdapterData()
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

    private fun initPartiesRecyclerView() = with(binding.rvParties) {
        adapter = partiesAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setPartiesAdapterData() {
        repeatOnStarted {
            partiesViewModel.partiesResult.collect { parties ->
                partiesAdapter.submitList(parties)
            }
        }
    }

    private fun adapterOnClick(partyBinding: IncludeLayoutPartyItemBinding, party: Party) {
        val intent = Intent(activity, PartyInformationActivity::class.java)
        intent.putExtra(PARTY, party)

        val pairFoodIcon = androidx.core.util.Pair<View, String>(partyBinding.foodCategoryImage, partyBinding.foodCategoryImage.transitionName)
        val pairTitle = androidx.core.util.Pair<View, String>(partyBinding.partyTitle, partyBinding.partyTitle.transitionName)
        val pairLocation = androidx.core.util.Pair<View, String>(partyBinding.partyLocation, partyBinding.partyLocation.transitionName)
        val pairTime = androidx.core.util.Pair<View, String>(partyBinding.partyScheduledTime, partyBinding.partyScheduledTime.transitionName)

        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), pairFoodIcon,pairTitle,pairLocation,pairTime)

        startActivity(intent, optionsCompat.toBundle())
    }
}