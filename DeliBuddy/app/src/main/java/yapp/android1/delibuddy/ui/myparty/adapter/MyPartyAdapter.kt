package yapp.android1.delibuddy.ui.myparty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.databinding.ViewHolderMypartyItemBinding

class MyPartyAdapter : ListAdapter<MyPartyMockEntity, MyPartyAdapter.ViewHolder>(
    MyPartyDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewHolderMypartyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder constructor(
        private val binding: ViewHolderMypartyItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyPartyMockEntity) {
            binding.apply {
                tvLocation.text = item.location
                tvContent.text = item.content
                tvDate.text = item.date

                //todo: left icon, background 색상 지정이 필요함
            }
        }
    }
}

private class MyPartyDiffCallback : DiffUtil.ItemCallback<MyPartyMockEntity>() {
    override fun areItemsTheSame(oldItem: MyPartyMockEntity, newItem: MyPartyMockEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MyPartyMockEntity, newItem: MyPartyMockEntity): Boolean {
        return oldItem == newItem
    }
}
