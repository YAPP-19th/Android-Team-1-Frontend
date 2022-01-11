package yapp.android1.delibuddy.ui.partyInformation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.balloon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import yapp.android1.delibuddy.adapter.comment.CommentAdapter
import yapp.android1.delibuddy.adapter.comment.CommentEvent
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentCommentTabBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.Action.CommentAction
import yapp.android1.delibuddy.ui.partyInformation.view.CommentOptionsBalloonFactory
import yapp.android1.delibuddy.ui.splash.SplashActivity
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class CommentTabFragment :
    BaseFragment<FragmentCommentTabBinding>(FragmentCommentTabBinding::inflate) {

    private val viewModel by activityViewModels<PartyInformationViewModel>()

    private lateinit var commentAdapter: CommentAdapter

    private val commentOptionsBalloon by balloon<CommentOptionsBalloonFactory>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
        collectComments()
    }

    private fun initializeRecyclerView() = with(binding) {
        commentAdapter = CommentAdapter(commentOptionsBalloon)
        commentAdapter.setHasStableIds(false)

        rvComment.adapter = commentAdapter
        rvComment.setHasFixedSize(true)
        rvComment.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        commentAdapter.setCommentEventListener { commentEvent ->
            handleCommentEvent(commentEvent)
        }
    }

    private fun collectComments() {
        repeatOnStarted {
            viewModel.isOwner.collect { isOwner ->
                commentAdapter.isOwner = isOwner
            }
        }

        repeatOnStarted {
            viewModel.comments.collect { comments ->
                commentAdapter.submitList(comments.value)

                val commentId =
                    requireActivity().intent.getIntExtra(SplashActivity.KEY_COMMENT_ID, -1)
                Timber.w("commentId: $commentId")
                if (commentId > -1) {
                    if (!comments.value.isNullOrEmpty()) {
                        comments.value!!.find { comment ->
                            comment.id == commentId
                        }?.let { matchedComment ->
                            Timber.w("matchedComment: $matchedComment")
                            Timber.w("scroll id: ${comments.value!!.indexOf(matchedComment)}")
                            binding.rvComment.scrollToPosition(comments.value!!.indexOf(matchedComment))
                        }
                    }
                }
            }
        }

        repeatOnStarted {
            viewModel.showToast.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        repeatOnStarted {
            viewModel.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: PartyInformationViewModel.Callback) {
        when (event) {
            else -> Unit
        }
    }

    private fun handleCommentEvent(event: CommentEvent) {
        when (event) {
            is CommentEvent.OnWriteCommentClicked -> viewModel.occurEvent(
                CommentAction.WriteReplyCommentClicked(
                    event.comment as Comment
                )
            )
            is CommentEvent.OnRemoveCommentClicked -> viewModel.occurEvent(
                CommentAction.DeleteComment(
                    event.comment.id
                )
            )
        }
    }
}