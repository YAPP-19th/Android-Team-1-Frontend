package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.databinding.ViewHolderAddressSearchResultBinding
import yapp.android1.delibuddy.holder.AddressSearchViewHolder
import yapp.android1.delibuddy.holder.OnItemClickListener
import yapp.android1.domain.entity.Address

class AddressSearchAdapter : RecyclerView.Adapter<AddressSearchViewHolder>() {
    private var items: List<Address> = emptyList()
    var searchQuery = ""
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressSearchViewHolder {
        return AddressSearchViewHolder(
            ViewHolderAddressSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: AddressSearchViewHolder, position: Int) {
        holder.bind(position, getItem(position), searchQuery)
    }

    fun getItem(position: Int): Address {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateResult(newResultList: List<Address>) {
        items = newResultList
        dataChanged()
    }

    fun dataChanged() {
        notifyDataSetChanged()
    }
}