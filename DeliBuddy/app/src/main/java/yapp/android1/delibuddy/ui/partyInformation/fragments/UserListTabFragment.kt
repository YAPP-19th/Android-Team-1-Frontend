package yapp.android1.delibuddy.ui.partyInformation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.comment.CommentAdapter
import yapp.android1.delibuddy.adapter.comment.UserAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentUserListTabBinding
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationAction.*
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

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

    private fun handleEvent(event: PartyInformationViewModel.PartyInformationEvent) {
        when(event) {
            is PartyInformationViewModel.PartyInformationEvent.UserBanSuccess -> {
                Toast.makeText(requireContext(), "해당 유저를 내보냈습니다", Toast.LENGTH_SHORT).show()
            }

            is PartyInformationViewModel.PartyInformationEvent.UserBanFailed -> {
                Toast.makeText(requireContext(), "잠시 후 다시 시도해 보세요", Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    private fun initializeRecyclerView() = with(binding) {
        rvUser.adapter = userAdapter
        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        userAdapter.setBanButtonListener { user ->
            viewModel.occurEvent(BanUserFromParty(user))
        }
    }
}