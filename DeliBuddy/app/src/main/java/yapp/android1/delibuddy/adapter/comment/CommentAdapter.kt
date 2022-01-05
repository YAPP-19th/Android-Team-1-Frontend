package yapp.android1.delibuddy.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.Balloon
import yapp.android1.delibuddy.databinding.ItemChildCommentBinding
import yapp.android1.delibuddy.databinding.ItemParentCommentBinding
import yapp.android1.delibuddy.model.ChildComment
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.CommentType
import yapp.android1.delibuddy.model.Event



class CommentAdapter(
    private val commentOptionsBalloon: Balloon
) : ListAdapter<CommentType, RecyclerView.ViewHolder>(CommentDiffUtil()) {

    companion object {
        private const val PARENT = 1
        private const val CHILD  = 2
    }

    var isOwner: Boolean = false
        set(value) {
            field = true
            notifyDataSetChanged()
        }

    private var commentEventListener: CommentEventListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            PARENT -> {
                val binding = ItemParentCommentBinding.inflate(layoutInflater, parent, false)
                ParentCommentViewHolder(binding, isOwner, commentOptionsBalloon)
            }

            CHILD -> {
                val binding = ItemChildCommentBinding.inflate(layoutInflater, parent, false)
                ChildCommentViewHolder(binding, isOwner, commentOptionsBalloon)
            }

            else -> throw IllegalArgumentException("올바르지 않은 CommentType입니다.")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(currentList[position] is Comment) PARENT else CHILD
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ParentCommentViewHolder -> {
                val item = currentList[position] as Comment
                holder.onBind(item, commentEventListener!!)
            }
            is ChildCommentViewHolder -> {
                val item = currentList[position] as ChildComment
                holder.onBind(item, commentEventListener!!)
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