package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.Event

typealias CommentEventListener = (CommentEvent) -> Unit

sealed class CommentEvent : Event {
    class OnWriteCommentClicked(val comment: Comment) : CommentEvent()
    class OnRemoveCommentClicked(val comment: Comment) : CommentEvent()
}

class CommentAdapter(
    private val isOwner: Boolean
) : ListAdapter<Comment, CommentViewHolder>(CommentDiffUtil()) {

    private var commentEventListener: CommentEventListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            Comment.PARENT ->{
                val binding = ItemParentCommentBinding.inflate(layoutInflater, parent, false)
                ParentCommentViewHolder(binding, isOwner)
            }
            Comment.CHILD -> {
                val binding = ItemChildCommentBinding.inflate(layoutInflater, parent, false)
                ChildCommentViewHolder(binding, isOwner)
            }
            else -> throw RuntimeException("올바른 ViewType이 아닙니다")
        }
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(currentList[position], commentEventListener!!)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].viewType
    }

    fun setCommentEventListener(listener: CommentEventListener) {
        commentEventListener = listener
    }

}

class CommentDiffUtil() : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

}