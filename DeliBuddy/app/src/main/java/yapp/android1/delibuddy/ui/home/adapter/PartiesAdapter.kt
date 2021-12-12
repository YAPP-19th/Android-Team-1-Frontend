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
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.IncludeLayoutPartyItemBinding
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.util.dpToPx

class PartiesAdapter(private val onClick: (IncludeLayoutPartyItemBinding, Party) -> Unit) :
    ListAdapter<Party, PartiesAdapter.PartiesViewHolder>(PartiesDiffCallback) {

    inner class PartiesViewHolder(
        private val binding: IncludeLayoutPartyItemBinding,
        val onClick: (IncludeLayoutPartyItemBinding, Party) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private var currentParty: Party? = null

        init {
            binding.root.setOnClickListener {
                currentParty?.let {
                    onClick(binding, it)
                }
            }
        }

        fun bind(party: Party) {
            currentParty = party

            Glide.with(context)
                .load(party.category.iconUrl)
                .into(binding.foodCategoryImage)
            binding.partyLocation.text = party.coordinate
            binding.partyTitle.text = party.title
            binding.partyScheduledTime.text = party.orderTime
            setMemberIcon(party.targetUserCount, party.currentUserCount)
            binding.memberCount.text = "${party.currentUserCount} / ${party.targetUserCount}"
        }

        private fun setMemberIcon(targetUserCount: Int, currentUserCount: Int) {
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, context.dpToPx(8).toInt(), 0)

            makeMemberIcon(context, params, R.drawable.icon_party_member_presence, currentUserCount)
            makeMemberIcon(
                context,
                params,
                R.drawable.icon_party_member_absence,
                targetUserCount - currentUserCount
            )
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

object PartiesDiffCallback : DiffUtil.ItemCallback<Party>() {
    override fun areItemsTheSame(oldItem: Party, newItem: Party): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Party, newItem: Party): Boolean {
        return oldItem == newItem
    }
}