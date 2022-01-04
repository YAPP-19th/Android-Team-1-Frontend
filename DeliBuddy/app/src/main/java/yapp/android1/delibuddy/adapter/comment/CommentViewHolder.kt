package yapp.android1.delibuddy.adapter.comment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.ChildComment
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.show


class ParentCommentViewHolder(
    private val binding: ItemParentCommentBinding,
    private val isOwner: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    var childCommentAdapter: ChildCommentAdapter? = null

    fun onBind(comment: Comment, listener: CommentEventListener) = with(binding) {
        tvWriterNickname.text = comment.writer?.nickName
        tvBody.text = comment.body
        tvTimeAgo.text = comment.createdAt

        Glide.with(itemView)
            .load(comment.writer?.profileImage)
            .into(ivIconUser)

        tvWriteComment.setOnClickListener {
            listener.invoke(CommentEvent.OnWriteCommentClicked(comment))
        }

        if (comment.hasChildComments()) {
            if (childCommentAdapter == null) {
                setChildCommentRecyclerView(comment.children, listener)
            } else {
                childCommentAdapter!!.submitList(comment.children)
            }
        }

        if (isOwner) {
            ivOptions.show()
        } else {
            ivOptions.hide()
        }
    }

    private fun setChildCommentRecyclerView(
        comments: List<ChildComment>,
        listener: CommentEventListener
    ) = with(binding) {
            childCommentAdapter = ChildCommentAdapter(isOwner)
            rvChildComments.apply {
                adapter = childCommentAdapter
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
            }
            childCommentAdapter?.setCommentEventListener(listener)
            childCommentAdapter?.submitList(comments)
        }

}


