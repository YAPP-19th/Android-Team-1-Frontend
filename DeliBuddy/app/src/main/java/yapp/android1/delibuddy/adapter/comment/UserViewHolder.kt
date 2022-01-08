package yapp.android1.delibuddy.adapter.comment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemUserBinding
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.show


class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(user: PartyInformation.User, isOwner: Boolean, leader: PartyInformation.Leader) = with(binding) {
        binding.tvUsername.text = user.nickName

        if(isOwner) {
            ivIconOwner.show()
            btnKickOut.show()

            if(leader.id == user.id) {
                btnKickOut.hide()
            }
        } else {
            ivIconOwner.hide()
            btnKickOut.hide()
        }

        Glide.with(itemView)
            .load(user.profileImage)
            .into(ivIconUser)
    }
}