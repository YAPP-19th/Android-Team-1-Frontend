package yapp.android1.delibuddy.adapter.comment

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skydoves.balloon.Balloon
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemEmptyCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.ChildComment
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.show


class ParentCommentViewHolder(
    private val binding: ItemParentCommentBinding,
    private val commentOptionsBalloon: Balloon
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(comment: Comment, listener: CommentEventListener, isOwner: Boolean) = with(binding) {
        tvWriterNickname.text = comment.writer?.nickName
        tvBody.text = comment.body
        tvTimeAgo.text = comment.createdAt

        Glide.with(itemView)
            .load(comment.writer?.profileImage)
            .into(ivIconUser)

        setListener(comment, listener)

        if (isOwner) {
            ivOptions.show()
        } else {
            ivOptions.hide()
        }
    }

    private fun setListener(comment: Comment, listener: CommentEventListener) = with(binding) {
        tvWriteComment.setOnClickListener {
            listener.invoke(CommentEvent.OnWriteCommentClicked(comment))
        }

        ivOptions.setOnClickListener { optionsButton ->
            val removeButton = commentOptionsBalloon.getContentView().findViewById<ConstraintLayout>(R.id.btn_remove)

            removeButton.setOnClickListener {
                commentOptionsBalloon.dismiss()
                listener.invoke(CommentEvent.OnRemoveCommentClicked(comment))
            }

            commentOptionsBalloon.showAlignBottom(optionsButton)
        }
    }

}

class ChildCommentViewHolder(
    private val binding: ItemChildCommentBinding,
    private val commentOptionsBalloon: Balloon
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(comment: ChildComment, listener: CommentEventListener, isOwner: Boolean) = with(binding) {
        tvWriterNickname.text = comment.writer?.nickName
        tvBody.text = comment.body
        tvTimeAgo.text = comment.createdAt
        ivOptions.setOnClickListener {
            listener.invoke(CommentEvent.OnRemoveCommentClicked(comment))
        }

        setListener(comment, listener)

        if (isOwner) {
            ivOptions.show()
        } else {
            ivOptions.hide()
        }
    }

    private fun setListener(comment: ChildComment, listener: CommentEventListener) = with(binding) {
        ivOptions.setOnClickListener { optionsButton ->
            val removeButton = commentOptionsBalloon.getContentView().findViewById<ConstraintLayout>(R.id.btn_remove)

            removeButton.setOnClickListener {
                commentOptionsBalloon.dismiss()
                listener.invoke(CommentEvent.OnRemoveCommentClicked(comment))
            }

            commentOptionsBalloon.showAlignBottom(optionsButton)
        }
    }

}

class EmptyCommentViewHolder(
    private val binding: ItemEmptyCommentBinding
) : RecyclerView.ViewHolder(binding.root)


