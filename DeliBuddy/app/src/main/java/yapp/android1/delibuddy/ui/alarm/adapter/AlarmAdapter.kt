package yapp.android1.delibuddy.ui.alarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yapp.android1.delibuddy.databinding.ViewHolderAlarmCommentBinding
import yapp.android1.delibuddy.databinding.ViewHolderAlarmDateTitleItemBinding
import yapp.android1.delibuddy.databinding.ViewHolderAlarmSpacingBinding
import yapp.android1.delibuddy.ui.alarm.model.AlarmViewTypeModel
import yapp.android1.delibuddy.ui.alarm.viewholder.AlarmCommentViewHolder
import yapp.android1.delibuddy.ui.alarm.viewholder.AlarmDateTitleViewHolder
import yapp.android1.delibuddy.ui.alarm.viewholder.AlarmSpacingViewHolder
import yapp.android1.delibuddy.ui.alarm.viewholder.AlarmViewHolder

class AlarmAdapter : ListAdapter<AlarmViewTypeModel, AlarmViewHolder>(
    AlarmDiffCallback()
) {

    enum class AlarmViewType {
        DATE,
        COMMENT,
        SPACING;

        companion object {
            fun ordinalOf(ordinal: Int): AlarmViewType? = values()
                .firstOrNull { it.ordinal == ordinal }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is AlarmViewTypeModel.AlarmDateViewTypeModel -> AlarmViewType.DATE.ordinal
            is AlarmViewTypeModel.AlarmCommentViewTypeModel -> AlarmViewType.COMMENT.ordinal
            is AlarmViewTypeModel.AlarmSpacingViewTypeModel -> AlarmViewType.SPACING.ordinal
            else -> throw IllegalStateException("Unknown type: ${item::class.java.simpleName}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (AlarmViewType.ordinalOf(viewType)) {
            AlarmViewType.DATE -> {
                AlarmDateTitleViewHolder(ViewHolderAlarmDateTitleItemBinding.inflate(layoutInflater, parent, false))
            }
            AlarmViewType.COMMENT -> {
                AlarmCommentViewHolder(ViewHolderAlarmCommentBinding.inflate(layoutInflater, parent, false))
            }
            AlarmViewType.SPACING -> {
                AlarmSpacingViewHolder(
                    ViewHolderAlarmSpacingBinding.inflate(layoutInflater, parent, false)
                )
            }

            null -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bindModel(getItem(position))
    }

}

private class AlarmDiffCallback : DiffUtil.ItemCallback<AlarmViewTypeModel>() {
    override fun areItemsTheSame(oldItem: AlarmViewTypeModel, newItem: AlarmViewTypeModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlarmViewTypeModel, newItem: AlarmViewTypeModel): Boolean {
        return oldItem == newItem
    }
}
