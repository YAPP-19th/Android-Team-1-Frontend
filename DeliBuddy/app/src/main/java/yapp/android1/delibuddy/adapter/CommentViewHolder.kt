package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.Comment


class CommentViewHolder private constructor(
    private val binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(comment: Comment, listener: WriteReplyListener) {
        when (binding) {
            is ItemParentCommentBinding -> {
                bindParentComment(binding, comment, listener)
            }

            is ItemChildCommentBinding -> {
                bindChildComment(binding, comment)
            }
        }
    }

    private fun bindParentComment(
        binding: ItemParentCommentBinding,
        comment: Comment,
        listener: WriteReplyListener

    ) = with(binding) {
        tvWriterNickname.text = comment.writer.nickName
        tvBody.text = comment.body

        tvWriteComment.setOnClickListener {
            listener.invoke(comment)
        }

//        Glide.with(itemView.context)
//            .load(comment.writer.profileImage)
//            .into(binding.ivIconUser)
    }

    private fun bindChildComment(
        binding: ItemChildCommentBinding,
        comment: Comment

    ) = with(binding) {
        tvWriterNickname.text = comment.writer.nickName
        tvBody.text = comment.body

//        Glide.with(itemView.context)
//            .load(comment.writer.profileImage)
//            .into(binding.ivIconUser)
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
