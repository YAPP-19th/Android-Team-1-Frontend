package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.UserEntity

data class User(
    val name: String,
    val partiesCount: Int,
    val profileImageUrl: String
) {
    companion object {
        fun mapToUser(userEntity: UserEntity): User {
            return User(
                name = userEntity.nickName,
                partiesCount = userEntity.partiesCnt,
                profileImageUrl = userEntity.profileImage
            )
        }
    }
}