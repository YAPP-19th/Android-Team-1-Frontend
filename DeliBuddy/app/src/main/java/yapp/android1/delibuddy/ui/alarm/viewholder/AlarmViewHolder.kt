package yapp.android1.delibuddy.ui.alarm.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.ui.alarm.model.AlarmViewTypeModel

sealed class AlarmViewHolder(val dataBinding: ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root) {
    open fun bindModel(model: AlarmViewTypeModel?) {}
}
