package yapp.android1.delibuddy.ui.myparty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.R
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
        val resources = binding.root.resources

        fun bind(item: MyPartyMockEntity) {
            binding.apply {
                tvLocation.text = item.location
                tvContent.text = item.content
                tvDate.text = item.date

                //todo: left icon, background 색상 지정이 필요함
                if (item.isCompleted) {
                    cardView.setCardBackgroundColor(resources.getColor(R.color.block_space_grey, null))
                    tvLocation.setTextColor(resources.getColor(R.color.sub_grey, null))
                    tvContent.setTextColor(resources.getColor(R.color.sub_grey, null))
                    tvDate.setTextColor(resources.getColor(R.color.sub_grey, null))
                    locationIcon.setColorFilter(resources.getColor(R.color.sub_grey, null))
                } else {
                    cardView.setCardBackgroundColor(resources.getColor(R.color.white, null))
                    tvLocation.setTextColor(resources.getColor(R.color.black, null))
                    tvContent.setTextColor(resources.getColor(R.color.black, null))
                    tvDate.setTextColor(resources.getColor(R.color.black, null))
                    locationIcon.setColorFilter(resources.getColor(R.color.black, null))
                }
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
