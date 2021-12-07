package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.AuthEntity

data class Auth(
    val token: String,
) {
    companion object {
        val EMPTY = Auth(
            token = ""
        )

        fun mapToAuth(entity: AuthEntity): Auth {
            return Auth(
                    token = entity.token
                )
        }
    }

}
