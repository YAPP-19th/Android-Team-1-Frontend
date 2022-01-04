package yapp.android1.delibuddy.adapter.comment

import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.model.ChildComment
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.show


class ChildCommentViewHolder(
    private val binding: ItemChildCommentBinding,
    private val isOwner: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(comment: ChildComment, listener: CommentEventListener) = with(binding) {
        tvWriterNickname.text = comment.writer?.nickName
        tvBody.text = comment.body
        tvTimeAgo.text = comment.createdAt
        ivOptions.setOnClickListener {
            listener.invoke(CommentEvent.OnRemoveCommentClicked(comment))
        }

        if (isOwner) {
            ivOptions.show()
        } else {
            ivOptions.hide()
        }
    }

}