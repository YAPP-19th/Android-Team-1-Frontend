package yapp.android1.delibuddy.ui.partyInformation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.CommentAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentCommentTabBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class CommentTabFragment : BaseFragment<FragmentCommentTabBinding>(FragmentCommentTabBinding::inflate) {

    private val viewModel = activityViewModels<PartyInformationViewModel>()

    private val commentAdapter = CommentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
    }

    private fun initializeRecyclerView() = with(binding) {
        rvComment.adapter = commentAdapter
        rvComment.setHasFixedSize(true)
        rvComment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        commentAdapter.setWriteReplyListener { comment ->
            Toast.makeText(requireContext(), comment.writer.nickName, Toast.LENGTH_SHORT).show()
        }
    }

    private fun collectComments() {
        repeatOnStarted {
            viewModel.value.party.collect { party ->
                //TODO recyclerview에 submit
            }
        }
    }
}