package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.databinding.ViewHolderAddressSearchResultBinding
import yapp.android1.delibuddy.holder.AddressSearchViewHolder
import yapp.android1.domain.entity.Address

class AddressSearchAdapter : RecyclerView.Adapter<AddressSearchViewHolder>() {
    private var searchResultClickListener: ((Address) -> Unit)? = null
    private var resultPair: Pair<String, List<Address>> = Pair("", emptyList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressSearchViewHolder {
        return AddressSearchViewHolder(
            ViewHolderAddressSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressSearchViewHolder, position: Int) {
        with(holder) {
            bind(
                item = getItem(position),
                searchQuery = resultPair.first,
                searchResultClickListener = searchResultClickListener!!
            )
        }
    }

    private fun getItem(position: Int): Address {
        return resultPair.second[position]
    }

    override fun getItemCount(): Int {
        return resultPair.second.size
    }

    fun updateResult(result: Pair<String, List<Address>>) {
        resultPair = result
        dataChanged()
    }

    private fun dataChanged() {
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: ((Address) -> Unit)) {
        this.searchResultClickListener = listener
    }
}