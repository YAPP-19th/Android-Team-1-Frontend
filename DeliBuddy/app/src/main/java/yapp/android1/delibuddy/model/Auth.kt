package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.AuthEntity

data class Auth(
    val token: String,
    val userId: Int,
) {
    companion object {
        val EMPTY = Auth(
            token = "",
            userId = -1,
        )

        fun mapToAuth(entity: AuthEntity): Auth {
            return Auth(
                token = entity.token,
                userId = entity.userId
            )
        }
    }

}
