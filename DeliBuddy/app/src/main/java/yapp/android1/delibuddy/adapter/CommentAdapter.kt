package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yapp.android1.delibuddy.model.Comment

typealias WriteReplyListener = (Comment) -> Unit

class CommentAdapter : ListAdapter<Comment, CommentViewHolder>(CommentDiffUtil()) {

    private var writeReplyListener: WriteReplyListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CommentViewHolder.of(layoutInflater, viewType)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(currentList[position], writeReplyListener!!)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].viewType
    }

    fun setWriteReplyListener(listener: WriteReplyListener) {
        writeReplyListener = listener
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