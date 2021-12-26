package yapp.android1.delibuddy.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.show


abstract class CommentViewHolder(
    protected val binding: ViewBinding,
    protected val isOwner: Boolean
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(comment: Comment, listener: CommentEventListener)
}

class ParentCommentViewHolder(
    binding: ItemParentCommentBinding,
    isOwner: Boolean
) : CommentViewHolder(binding, isOwner) {

    override fun onBind(comment: Comment, listener: CommentEventListener) =
        with(binding as ItemParentCommentBinding) {
            tvWriterNickname.text = comment.writer?.nickName
            tvBody.text = comment.body
            tvTimeAgo.text = comment.createdAt

            Glide.with(itemView)
                .load(comment.writer?.profileImage)
                .into(ivIconUser)

            tvWriteComment.setOnClickListener {
                listener?.invoke(CommentEvent.OnWriteCommentClicked(comment))
            }

            if (comment.hasChildComments()) {
                setChildCommentRecyclerView(comment.children, listener)
            }

            if(isOwner) {
                ivOptions.show()
            } else {
                ivOptions.hide()
            }
        }

    private fun setChildCommentRecyclerView(comments: List<Comment>, listener: CommentEventListener) =
        with(binding as ItemParentCommentBinding) {
            val commentAdapter = CommentAdapter(isOwner)
            rvChildComments.apply {
                adapter = commentAdapter
                setHasFixedSize(false)
                isNestedScrollingEnabled = false
            }
            commentAdapter.setCommentEventListener(listener)
            commentAdapter.submitList(comments)
        }

}

class ChildCommentViewHolder(
    binding: ItemChildCommentBinding,
    isOwner: Boolean
) : CommentViewHolder(binding, isOwner) {

    override fun onBind(comment: Comment, listener: CommentEventListener) =
        with(binding as ItemChildCommentBinding) {
            tvWriterNickname.text = comment.writer?.nickName
            tvBody.text = comment.body
            tvTimeAgo.text = comment.createdAt
            ivOptions.setOnClickListener {
                listener.invoke(CommentEvent.OnRemoveCommentClicked(comment))
            }

            if(isOwner) {
                ivOptions.show()
            } else {
                ivOptions.hide()
            }
        }

}
