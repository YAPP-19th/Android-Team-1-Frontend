package yapp.android1.delibuddy.ui.myparty

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentMypartyBinding
import yapp.android1.delibuddy.databinding.ViewHolderMypartyItemBinding
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.myparty.adapter.MyPartyAdapter
import yapp.android1.delibuddy.ui.myparty.viewmodel.MyPartyViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

const val PARTY_INFORMATION = "partyInformation"

@AndroidEntryPoint
class MyPartyFragment : BaseFragment<FragmentMypartyBinding>(
    FragmentMypartyBinding::inflate
) {
    private val myPartyAdapter by lazy {
        MyPartyAdapter { binding, partyInformation ->
            adapterOnClick(binding, partyInformation)
        }
    }

    private val myPartyViewModel: MyPartyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initObserve()
        getMyPartiesUseCase()
    }

    private fun getMyPartiesUseCase() {
        myPartyViewModel.occurEvent(
            MyPartyViewModel.MyPartyEvent.GetMyPartiesUseCase
        )
    }

    private fun initViews() {
        binding.recyclerView.adapter = myPartyAdapter
    }

    private fun initObserve() {
        repeatOnStarted {
            myPartyViewModel.myParties.collect { myParties ->
                myPartyAdapter.submitList(myParties)
            }
        }
    }

    private fun adapterOnClick(
        binding: ViewHolderMypartyItemBinding,
        partyInformation: PartyInformation
    ) {
        val intent = Intent(activity, PartyInformationActivity::class.java)
        intent.putExtra(PARTY_INFORMATION, partyInformation)

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
}