package yapp.android1.delibuddy.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.IncludeLayoutPartyItemBinding
import yapp.android1.delibuddy.util.dpToPx
import yapp.android1.domain.entity.PartyEntity


class PartiesAdapter(private val onClick: (PartyEntity) -> Unit) :
    ListAdapter<PartyEntity, PartiesAdapter.PartiesViewHolder>(PartiesDiffCallback) {

    inner class PartiesViewHolder(
        private val binding: IncludeLayoutPartyItemBinding,
        val onClick: (PartyEntity) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private var currentPartyEntity: PartyEntity? = null

        init {
            binding.root.setOnClickListener {
                currentPartyEntity?.let {
                    onClick(it)
                }
            }
        }

        fun bind(partyEntity: PartyEntity) {
            currentPartyEntity = partyEntity

            binding.foodCategoryImage.setImageResource(R.drawable.icon_food_bread_small)
            binding.partyLocation.text = "성복역 1번 출구"
            binding.partyTitle.text = "유로코 피자 4인"
            binding.partyScheduledTime.text = "10월 5일 10시 40분"
            setMemberIcon(5, 3)
            binding.memberCount.text = "3/5"
        }

        private fun setMemberIcon(totalMemberNumber: Int, presenceMemberNumber: Int) {
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, context.dpToPx(8).toInt(), 0)

            makeMemberIcon(context, params, R.drawable.icon_party_member_presence, presenceMemberNumber)
            makeMemberIcon(context, params, R.drawable.icon_party_member_absence, totalMemberNumber - presenceMemberNumber)
        }

        private fun makeMemberIcon(
            context: Context,
            params: LinearLayout.LayoutParams,
            drawableId: Int,
            count: Int,
        ) {
            for (index in 0 until count) {
                var image = ImageView(context)
                image.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
                image.layoutParams = params
                binding.memberIcon.addView(image)
            }
        }

        private fun setDisabledUI() {
            binding.partyItem.alpha = 0.4f
            binding.partyItem.setBackgroundColor(ContextCompat.getColor(context, R.color.block_space_grey))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IncludeLayoutPartyItemBinding.inflate(inflater, parent, false)
        return PartiesViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PartiesViewHolder, position: Int) {
        val party = getItem(position)
        holder.bind(party)
    }
}

object PartiesDiffCallback : DiffUtil.ItemCallback<PartyEntity>() {
    override fun areItemsTheSame(oldItem: PartyEntity, newItem: PartyEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PartyEntity, newItem: PartyEntity): Boolean {
        return oldItem == newItem
    }
}