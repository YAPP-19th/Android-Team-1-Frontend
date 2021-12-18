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
import timber.log.Timber
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.adapter.CommentAdapter
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentCommentTabBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.util.extensions.repeatOnStarted
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferenceHelper
import yapp.android1.delibuddy.util.sharedpreferences.SharedPreferencesManager

@AndroidEntryPoint
class CommentTabFragment : BaseFragment<FragmentCommentTabBinding>(FragmentCommentTabBinding::inflate) {

    private val viewModel = activityViewModels<PartyInformationViewModel>()

    private val sharedPreferenceManager = SharedPreferencesManager(requireContext())

    private lateinit var commentAdapter: CommentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
        collectComments()
    }

    private fun initializeRecyclerView() = with(binding) {
        val currentUserId = sharedPreferenceManager.getUserId()
        commentAdapter = CommentAdapter(currentUserId)

        rvComment.adapter = commentAdapter
        rvComment.setHasFixedSize(true)
        rvComment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        commentAdapter.setWriteReplyListener { comment ->
            Toast.makeText(requireContext(), comment.writer?.nickName, Toast.LENGTH_SHORT).show()
        }
    }

    private fun collectComments() {
        repeatOnStarted {

            viewModel.value.comments.collect { comments ->
                Timber.tag("[TAG]").d("$comments")
                commentAdapter.submitList(comments)
            }
        }

        repeatOnStarted {
            viewModel.value.showToast.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}