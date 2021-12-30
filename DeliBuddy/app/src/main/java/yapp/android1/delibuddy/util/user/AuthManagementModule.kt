package yapp.android1.delibuddy.util.user

import com.auth0.android.jwt.JWT
import timber.log.Timber

class AuthManagementModule {

    val REMAINING_PERIOD = 259200L

    fun checkAuthRefreshRequired(token: String, call: (Boolean) -> Unit) {
        val jwt = JWT(token)
        Timber.d("jwt ${jwt.expiresAt}")
        val isExpired = jwt.isExpired(REMAINING_PERIOD)
        Timber.d("jwt $isExpired")
        call(isExpired)
    }
}