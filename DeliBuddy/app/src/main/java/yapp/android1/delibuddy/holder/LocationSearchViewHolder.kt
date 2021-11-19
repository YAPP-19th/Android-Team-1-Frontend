package yapp.android1.delibuddy.holder

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ViewHolderLocationSearchResultBinding
import yapp.android1.domain.entity.Address

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class LocationSearchViewHolder(
    private val binding: ViewHolderLocationSearchResultBinding,
    private val listener: OnItemClickListener?
) : RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    init {
        binding.root.setOnClickListener {
            listener?.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(position: Int, item: Address, searchQuery: String) = with(binding) {
        if (searchQuery.isNotBlank()) {
            val builder = SpannableStringBuilder(item.addressName)
            val startIdx = item.addressName.indexOf(searchQuery)
            if (startIdx >= 0) {
                builder.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            R.color.main_orange
                        )
                    ),
                    startIdx,
                    startIdx + searchQuery.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            tvAddressName.text = builder
        } else {
            tvAddressName.text = item.addressName
        }
        //when (item) {
//            is LocationSearchType.KeywordType -> {
//                binding.tvAddressName.text = item.data.place_name
//                // binding.locAddrText.text = item.data.road_address_name
//            }
//
//            is LocationSearchType.AddressType -> {
//                binding.tvAddressName.text = item.data.address_name
////                when (item.data.address_type) {
////                    "REGION" -> binding.locAddrText.text = item.data.address.address_name
////                    "REGION_ADDR" -> binding.locAddrText.text = item.data.address.address_name
////                    "ROAD" -> binding.locAddrText.text = item.data.road_address.address_name
////                    "ROAD_ADDR" -> binding.locAddrText.text = item.data.road_address.address_name
////                }
//            }
        //}
    }

    // item certain view click listener
}