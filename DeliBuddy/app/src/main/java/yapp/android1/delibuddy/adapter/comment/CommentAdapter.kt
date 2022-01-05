package yapp.android1.delibuddy.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.Event



class CommentAdapter(
    private val isOwner: Boolean
) : ListAdapter<Comment, ParentCommentViewHolder>(CommentDiffUtil()) {

    private var commentEventListener: CommentEventListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentCommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemParentCommentBinding.inflate(layoutInflater, parent, false)

        return ParentCommentViewHolder(binding, isOwner)

    }

    override fun onBindViewHolder(holder: ParentCommentViewHolder, position: Int) {
        holder.onBind(currentList[position], commentEventListener!!)
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