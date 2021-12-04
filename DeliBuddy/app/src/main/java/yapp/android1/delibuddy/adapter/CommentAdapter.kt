package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.Comment


class CommentAdapter : ListAdapter<Comment, CommentViewHolder>(CommentDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CommentViewHolder.of(layoutInflater, viewType)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.onBind(currentList[position])
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

class CommentViewHolder private constructor(
    private val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(comment: Comment) {
        when(binding) {
            is ItemParentCommentBinding -> {
                bindParentComment(binding, comment)
            }

            is ItemChildCommentBinding -> {
                bindChildComment(binding, comment)
            }
        }
    }

    private fun bindParentComment(binding: ItemParentCommentBinding, comment: Comment) = with(binding) {
        tvWriterNickname.text = comment.writer.nickName
        binding.tvWriteComment.setOnClickListener {

        }
        Glide.with(itemView.context)
            .load(comment.writer.profileImage)
            .into(binding.ivUserIcon)
    }

    private fun bindChildComment(binding: ItemChildCommentBinding, comment: Comment) = with(binding) {

    }

    companion object {
        fun of(layoutInflater: LayoutInflater, viewType: Int): CommentViewHolder {
            return when (viewType) {
                Comment.PARENT -> {
                    CommentViewHolder(ItemParentCommentBinding.inflate(layoutInflater))
                }
                Comment.CHILD -> {
                    CommentViewHolder(ItemChildCommentBinding.inflate(layoutInflater))
                }
                else -> throw IllegalArgumentException("올바른 Comment 타입이 아닙니다.")
            }
        }
    }
}