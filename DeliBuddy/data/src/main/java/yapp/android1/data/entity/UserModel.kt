package yapp.android1.data.entity

import yapp.android1.domain.entity.UserEntity

data class UserModel(
    val id: Int,
    val nickName: String,
    val partiesCnt: Int,
    val profileImage: String,
) {
    companion object {
        fun toUserEntity(model: UserModel): UserEntity {
            return UserEntity(
                id = model.id,
                nickName = model.nickName,
                partiesCnt = model.partiesCnt,
                profileImage = model.profileImage
            )
        }
    }
}
