package yapp.android1.delibuddy.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.model.ChildComment


class ChildCommentAdapter(
    private val isOwner: Boolean
) : ListAdapter<ChildComment, ChildCommentViewHolder>(ChildCommentDiffUtil()) {

    private var commentEventListener: CommentEventListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildCommentViewHolder {
        val binding = ItemChildCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ChildCommentViewHolder(binding, isOwner)
    }

    override fun onBindViewHolder(holder: ChildCommentViewHolder, position: Int) {
        holder.onBind(getItem(position), commentEventListener!!)
    }

    fun setCommentEventListener(listener: CommentEventListener) {
        this.commentEventListener = listener
    }
}

class ChildCommentDiffUtil() : DiffUtil.ItemCallback<ChildComment>() {

    override fun areItemsTheSame(oldItem: ChildComment, newItem: ChildComment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChildComment, newItem: ChildComment): Boolean {
        return oldItem.id == newItem.id
    }

}