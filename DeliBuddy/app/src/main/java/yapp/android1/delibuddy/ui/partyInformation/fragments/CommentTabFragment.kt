package yapp.android1.delibuddy.ui.partyInformation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.balloon.balloon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.adapter.comment.CommentAdapter
import yapp.android1.delibuddy.adapter.comment.CommentEvent
import yapp.android1.delibuddy.base.BaseFragment
import yapp.android1.delibuddy.databinding.FragmentCommentTabBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationAction.*
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent
import yapp.android1.delibuddy.ui.partyInformation.view.CommentOptionsBalloonFactory
import yapp.android1.delibuddy.util.extensions.repeatOnStarted

@AndroidEntryPoint
class CommentTabFragment : BaseFragment<FragmentCommentTabBinding>(FragmentCommentTabBinding::inflate) {

    private val viewModel = activityViewModels<PartyInformationViewModel>()

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
        rvComment.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        commentAdapter.setCommentEventListener { commentEvent ->
            handleCommentEvent(commentEvent)
        }
    }

    private fun collectComments() {

        repeatOnStarted {
            viewModel.value.isOwner.collect { isOwner->
                commentAdapter.isOwner = isOwner
            }
        }

        repeatOnStarted {
            viewModel.value.comments.collect { comments ->
                commentAdapter.submitList(comments.value)
            }
        }

        repeatOnStarted {
            viewModel.value.showToast.collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        repeatOnStarted {
            viewModel.value.event.collect { event ->
                handleEvent(event)
            }
        }
    }

    private fun handleEvent(event: PartyInformationEvent) {
        when(event) {
            else -> Unit
        }
    }

    private fun handleCommentEvent(event: CommentEvent) {
        when(event) {
            is CommentEvent.OnWriteCommentClicked -> viewModel.value.occurEvent(OnCommentWriteTextViewClicked(event.comment as Comment))
            is CommentEvent.OnRemoveCommentClicked -> viewModel.value.occurEvent(DeleteComment(event.comment.id))
        }
    }
}