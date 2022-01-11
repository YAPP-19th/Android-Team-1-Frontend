package yapp.android1.delibuddy.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.Balloon
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemEmptyCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.*


class CommentAdapter(
    private val commentOptionsBalloon: Balloon
) : ListAdapter<CommentType, RecyclerView.ViewHolder>(CommentDiffUtil()) {

    companion object {
        private const val PARENT = 1
        private const val CHILD  = 2
        private const val EMPTY  = 3
    }

    var isOwner: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var commentEventListener: CommentEventListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            PARENT -> {
                val binding = ItemParentCommentBinding.inflate(layoutInflater, parent, false)
                ParentCommentViewHolder(binding, commentOptionsBalloon)
            }

            CHILD -> {
                val binding = ItemChildCommentBinding.inflate(layoutInflater, parent, false)
                ChildCommentViewHolder(binding, commentOptionsBalloon)
            }

            EMPTY -> {
                val binding = ItemEmptyCommentBinding.inflate(layoutInflater, parent, false)
                EmptyCommentViewHolder(binding)
            }

            else -> throw IllegalArgumentException("올바르지 않은 CommentType입니다.")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(currentList[position]){
            is Comment      -> PARENT
            is ChildComment -> CHILD
            is EmptyComment -> EMPTY
            else            -> throw IllegalArgumentException("올바르지 않은 CommentType입니다.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ParentCommentViewHolder -> {
                val item = currentList[position] as Comment
                holder.onBind(item, commentEventListener!!, isOwner)
            }
            is ChildCommentViewHolder -> {
                val item = currentList[position] as ChildComment
                holder.onBind(item, commentEventListener!!, isOwner)
            }
        }
    }

    fun setCommentEventListener(listener: CommentEventListener) {
        commentEventListener = listener
    }

}

class CommentDiffUtil() : DiffUtil.ItemCallback<CommentType>() {
    override fun areItemsTheSame(oldItem: CommentType, newItem: CommentType): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentType, newItem: CommentType): Boolean {
        return oldItem.id == newItem.id
    }

}