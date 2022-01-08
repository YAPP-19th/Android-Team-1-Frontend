package yapp.android1.delibuddy.adapter.comment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yapp.android1.delibuddy.databinding.ItemUserBinding
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.util.extensions.hide
import yapp.android1.delibuddy.util.extensions.show


class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(
        user: PartyInformation.User,
        isOwner: Boolean,
        leader: PartyInformation.Leader,
        listener: ((PartyInformation.User) -> Unit)?
    ) = with(binding) {
        binding.tvUsername.text = user.nickName

        if(isOwner) {
            btnBan.show()
            btnBan.setOnClickListener {
                listener?.invoke(user)
            }

            if(leader.id == user.id) {
                ivIconOwner.show()
                btnBan.hide()
            }
        } else {
            ivIconOwner.hide()
            btnBan.hide()
        }

        Glide.with(itemView)
            .load(user.profileImage)
            .into(ivIconUser)
    }
}