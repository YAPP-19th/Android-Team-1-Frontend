package yapp.android1.delibuddy.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.Comment


abstract class CommentViewHolder(
    protected val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(comment: Comment, listener: WriteReplyListener)
}

class ParentCommentViewHolder(
    binding: ItemParentCommentBinding
) : CommentViewHolder(binding) {

    override fun onBind(comment: Comment, listener: WriteReplyListener) =
        with(binding as ItemParentCommentBinding) {
            tvWriterNickname.text = comment.writer.nickName
            tvBody.text = comment.body

            Glide.with(itemView.context)
                .load(comment.writer.profileImage)
                .into(ivIconUser)

            tvWriteComment.setOnClickListener {
                listener.invoke(comment)
            }

            if (comment.hasChildComments()) {
                setChildCommentRecyclerView(comment.childComments!!)
            }
        }

    private fun setChildCommentRecyclerView(comments: List<Comment>) =
        with(binding as ItemParentCommentBinding) {
            val commentAdapter = CommentAdapter()
            rvChildComments.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            commentAdapter.submitList(comments)
        }

}

class ChildCommentViewHolder(
    binding: ItemChildCommentBinding
) : CommentViewHolder(binding) {

    override fun onBind(comment: Comment, listener: WriteReplyListener) =
        with(binding as ItemChildCommentBinding) {
            tvWriterNickname.text = comment.writer.nickName
            tvBody.text = comment.body
        }

}
