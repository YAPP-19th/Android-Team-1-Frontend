package yapp.android1.delibuddy.util.user

import com.auth0.android.jwt.JWT
import java.util.*

class AuthManagementModule {

    companion object {
        const val REMAINING_PERIOD = 259200L // 3 days

        const val AUTH_TOKEN_EXPIRED_STATUS = -1
        const val AUTH_TOKEN_REFRESH_REQUIRED_STATUS = 0
        const val AUTH_TOKEN_AVAILABLE_STATUS = 1
    }

    fun checkAuthStatus(token: String, callback: (Int) -> Unit) {
        val jwt = JWT(token)
        val todayTime = Date().time
        val diffTime = jwt.expiresAt?.time?.minus(todayTime)

        var status = AUTH_TOKEN_AVAILABLE_STATUS
        if (diffTime != null) {
            if (diffTime <= REMAINING_PERIOD * 1000) {
                status = AUTH_TOKEN_REFRESH_REQUIRED_STATUS
            } else if (diffTime < 0) {
                status = AUTH_TOKEN_EXPIRED_STATUS
            }
        }

        callback(status)
    }
}