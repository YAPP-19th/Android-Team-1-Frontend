package yapp.android1.delibuddy.ui.partyInformation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.adapter.comment.UserAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentUserListTabBinding
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Action.*
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Callback.UserCallback
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.extensions.showCustomDialog
import yapp.android1.delibuddy.util.extensions.showToast

@AndroidEntryPoint
class UserListTabFragment : BaseFragment<FragmentUserListTabBinding>(FragmentUserListTabBinding::inflate) {

    private val viewModel by activityViewModels<PartyInformationViewModel>()

    private val userAdapter = UserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
        collectData()
    }

    private fun collectData() = with(viewModel) {
        repeatOnStarted {
            party.collect { partyInformation->
                userAdapter.submitList(partyInformation.users)
                userAdapter.leader = partyInformation.leader
            }
        }

        repeatOnStarted {
            isOwner.collect { isOwner ->
                userAdapter.isOwner = isOwner
            }
        }

        repeatOnStarted {
            event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: PartyInformationViewModel.Callback) {
        when(event) {
            is UserCallback.UserBanSuccess -> {
                requireContext().showToast("?????? ????????? ??????????????????")
            }

            is UserCallback.UserBanFailed -> {
                requireContext().showToast("?????? ??? ?????? ????????? ?????????")
            }

            else -> Unit
        }
    }

    private fun initializeRecyclerView() = with(binding) {
        rvUser.adapter = userAdapter
        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        userAdapter.setBanButtonListener { user ->
            requireContext().showCustomDialog(
                title          = "??????",
                message        = "?????? ?????????????????????????",
                positiveMethod = { viewModel.occurEvent(UserAction.BanUserFromParty(user)) },
                negativeMethod = null
            )
        }
    }
}