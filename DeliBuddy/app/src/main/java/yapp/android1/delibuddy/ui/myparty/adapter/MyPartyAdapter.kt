package yapp.android1.delibuddy.ui.myparty.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ViewHolderMypartyItemBinding
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus

class MyPartyAdapter(
    private val onIntentClick: (ViewHolderMypartyItemBinding, PartyInformation) -> Unit,
    private val onMoreOptionsClick: (ViewHolderMypartyItemBinding, PartyInformation) -> Unit,
) :
    ListAdapter<PartyInformation, MyPartyAdapter.ViewHolder>(
        MyPartyDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewHolderMypartyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder constructor(
        private val binding: ViewHolderMypartyItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context
        private val resources = binding.root.resources
        private var currentPartyInformation: PartyInformation? = null

        init {
            binding.itemMyparty.setOnClickListener {
                currentPartyInformation?.let {
                    onIntentClick(binding, it)
                }
            }

            binding.ivMoreIcon.setOnClickListener {
                currentPartyInformation?.let {
                    onMoreOptionsClick(binding, it)
                }
            }
        }

        fun bind(partyInformation: PartyInformation) {
            currentPartyInformation = partyInformation

            binding.apply {
                Glide.with(context)
                    .load(partyInformation.category.iconUrl)
                    .into(foodCategoryImage)
                val backgroundColor =
                    Color.parseColor("#${partyInformation.category.backgroundColorCode}")
                ivLeftBackground.setBackgroundColor(backgroundColor)

                partyLocation.text = partyInformation.placeName
                partyTitle.text = partyInformation.title
                partyScheduledTime.text = partyInformation.orderTime

                setUiBasedStatus(partyInformation.status)
            }
        }

        private fun setUiBasedStatus(status: PartyStatus) {
            when (status) {
                PartyStatus.RECRUIT -> setAbleUI()
                PartyStatus.ORDER -> setOrderingStatusLabel()
                PartyStatus.COMPLETED -> setDisabledUI()
            }
        }

        private fun setAbleUI() {
            binding.apply {
                cardView.setCardBackgroundColor(resources.getColor(R.color.white, null))
                partyLocation.setTextColor(resources.getColor(R.color.black, null))
                partyTitle.setTextColor(resources.getColor(R.color.black, null))
                partyScheduledTime.setTextColor(resources.getColor(R.color.black, null))
                locationIcon.setColorFilter(resources.getColor(R.color.black, null))
            }
        }

        private fun setOrderingStatusLabel() {
            setAbleUI()

            binding.apply {
                orderingStatusLabel.visibility = View.VISIBLE
                partyTitle.maxEms = 9
            }
        }

        private fun setDisabledUI() {
            binding.apply {
                cardView.setCardBackgroundColor(
                    resources.getColor(
                        R.color.block_space_grey,
                        null
                    )
                )
                foodCategoryImage.alpha = 0.4f

                ivLeftBackground.setBackgroundColor(
                    resources.getColor(
                        R.color.block_space_grey,
                        null
                    )
                )
                partyLocation.setTextColor(resources.getColor(R.color.sub_grey, null))
                partyTitle.setTextColor(resources.getColor(R.color.sub_grey, null))
                partyScheduledTime.setTextColor(resources.getColor(R.color.sub_grey, null))
                tvDateInfo.setTextColor(resources.getColor(R.color.sub_grey, null))
                locationIcon.setColorFilter(resources.getColor(R.color.sub_grey, null))
            }
        }
    }
}

private class MyPartyDiffCallback : DiffUtil.ItemCallback<PartyInformation>() {
    override fun areItemsTheSame(oldItem: PartyInformation, newItem: PartyInformation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PartyInformation,
        newItem: PartyInformation
    ): Boolean {
        return oldItem == newItem
    }
}