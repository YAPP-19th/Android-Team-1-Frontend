package yapp.android1.delibuddy.ui.home.viewmodel

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private var backKeyPressedTime: Long? = null
    private val TIME_INTERVAL = 2000

    fun judgeAppTerminate(): Boolean {
        var currentTime = System.currentTimeMillis()

        return if (backKeyPressedTime == null || currentTime > backKeyPressedTime!! + TIME_INTERVAL) {
            backKeyPressedTime = currentTime
            false
        } else {
            true
        }
    }
}