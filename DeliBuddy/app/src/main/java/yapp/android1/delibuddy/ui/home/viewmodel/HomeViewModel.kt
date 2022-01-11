package yapp.android1.delibuddy.ui.home.viewmodel

import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult

class HomeViewModel : BaseViewModel<Event>() {

    private var backKeyPressedTime: Long? = null
    private val TIME_INTERVAL = 2000

    private val _isAppTerminate = MutableEventFlow<Boolean>()
    val isAppTermiate: EventFlow<Boolean> = _isAppTerminate

    sealed class HomeEvent : Event {
        object JudgeAppTerminate : HomeEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is HomeEvent.JudgeAppTerminate -> judgeAppTerminate()
        }
    }

    private suspend fun judgeAppTerminate() {
        var currentTime = System.currentTimeMillis()

        if (backKeyPressedTime == null || currentTime > backKeyPressedTime!! + TIME_INTERVAL) {
            backKeyPressedTime = currentTime
            _isAppTerminate.emit(false)
        } else {
            _isAppTerminate.emit(true)
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {}
}