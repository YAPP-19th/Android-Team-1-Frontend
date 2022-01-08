package yapp.android1.delibuddy.ui.partyInformation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class UserListTabFragment : BaseFragment<FragmentUserListTabBinding>(FragmentUserListTabBinding::inflate) {

    private val viewModel by activityViewModels<PartyInformationViewModel>()

    private val userAdapter = UserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViewModel()
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
    }

    private fun initializeViewModel() = with(binding) {
        rvUser.adapter = userAdapter
        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}