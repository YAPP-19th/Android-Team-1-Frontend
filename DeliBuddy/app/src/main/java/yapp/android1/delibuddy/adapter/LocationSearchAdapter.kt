package yapp.android1.delibuddy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yapp.android1.delibuddy.databinding.ViewHolderLocationSearchResultBinding
import yapp.android1.delibuddy.holder.LocationSearchViewHolder
import yapp.android1.delibuddy.holder.OnItemClickListener
import yapp.android1.domain.entity.Address

class LocationSearchAdapter : RecyclerView.Adapter<LocationSearchViewHolder>() {
    private var items: List<Address> = emptyList()
    var searchQuery = ""
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationSearchViewHolder {
        return LocationSearchViewHolder(
            ViewHolderLocationSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: LocationSearchViewHolder, position: Int) {
        holder.bind(position, getItem(position), searchQuery)
    }

    fun getItem(position: Int): Address {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addResult(newResultList: List<Address>) {
        items = newResultList
        dataChanged()
    }

    fun dataChanged() {
        notifyDataSetChanged()
    }
}