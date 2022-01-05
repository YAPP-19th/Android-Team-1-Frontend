package yapp.android1.delibuddy.ui.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.FcmTokenUseCase
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fcmTokenUseCase: FcmTokenUseCase
) : BaseViewModel<Event>() {
    private var job: Job? = null

    private val _setFcmTokenResult = MutableEventFlow<Boolean>()
    val setFcmTokenResult: EventFlow<Boolean> = _setFcmTokenResult

    sealed class SplashEvent : Event {
        class SetFcmTokenEvent(val token: String) : SplashEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is SplashEvent.SetFcmTokenEvent -> setFcmToken(event.token)
        }
    }

    private fun setFcmToken(token: String) {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = fcmTokenUseCase.invoke(token)) {
                is NetworkResult.Success -> {
                    _setFcmTokenResult.emit(result.data)
                }

                is NetworkResult.Error -> Timber.w("fcm fail") //handleError(result) {}
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        when (result.errorType) {
            is NetworkError.Unknown -> {
                showToast("알 수 없는 에러가 발생했습니다.")
            }
            is NetworkError.Timeout -> {
                showToast("타임 아웃 에러가 발생했습니다.")
            }
            is NetworkError.InternalServer -> {
                showToast("내부 서버에서 오류가 발생했습니다.")
            }
            is NetworkError.BadRequest -> {
                val code = (result.errorType as NetworkError.BadRequest).code
                val msg = (result.errorType as NetworkError.BadRequest).message
                showToast("에러 코드 ${code}, $msg")
            }
        }
    }
}