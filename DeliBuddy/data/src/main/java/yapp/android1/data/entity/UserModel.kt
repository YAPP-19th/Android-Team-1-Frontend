package yapp.android1.data.entity

import yapp.android1.domain.entity.UserEntity

data class UserModel(
    val id: Int,
    val nickname: String,
    val partiesCnt: Int,
    val profileImage: String,
) {
    companion object {
        fun toUserEntity(model: UserModel): UserEntity {
            return UserEntity(
                model.id,
                model.nickname,
                model.partiesCnt,
                model.profileImage
            )
        }
    }
}
