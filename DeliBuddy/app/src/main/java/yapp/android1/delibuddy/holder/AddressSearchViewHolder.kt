package yapp.android1.delibuddy.holder

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.databinding.ViewHolderAddressSearchResultBinding
import yapp.android1.domain.entity.Address

class AddressSearchViewHolder(
    private val binding: ViewHolderAddressSearchResultBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    fun bind(
        item: Address,
        searchQuery: String,
        searchResultClickListener: ((Address) -> Unit)
    ) = with(binding) {
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

        itemView.setOnClickListener {
            searchResultClickListener.invoke(item)
        }
    }

    // item certain view click listener
}