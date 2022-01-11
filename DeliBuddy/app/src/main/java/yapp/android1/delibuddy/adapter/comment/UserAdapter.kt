package yapp.android1.delibuddy.adapter.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yapp.android1.delibuddy.databinding.ItemUserBinding
import yapp.android1.delibuddy.model.PartyInformation


internal class UserAdapter : ListAdapter<PartyInformation.User, UserViewHolder>(UserDiffUtil()) {

    var leader: PartyInformation.Leader = PartyInformation.Leader.EMPTY
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isOwner: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var listener: ((PartyInformation.User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(getItem(position), isOwner, leader, listener)
    }

    fun setBanButtonListener(listener: (PartyInformation.User) -> Unit) {
        this.listener = listener
    }

}

class UserDiffUtil : DiffUtil.ItemCallback<PartyInformation.User>() {
    override fun areItemsTheSame(
        oldItem: PartyInformation.User,
        newItem: PartyInformation.User
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PartyInformation.User,
        newItem: PartyInformation.User
    ): Boolean {
        return oldItem.id == newItem.id
    }

}