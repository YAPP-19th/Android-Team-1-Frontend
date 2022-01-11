package yapp.android1.delibuddy.ui.login.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.delibuddy.util.user.UserAuthManager
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.FetchAuthUseCase
import yapp.android1.domain.interactor.usecase.RefreshAuthUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val fetchAuthUseCase: FetchAuthUseCase,
    private val refreshAuthUseCase: RefreshAuthUseCase
) : BaseViewModel<Event>() {

    private val _tokenResult = MutableEventFlow<Auth>()
    val tokenResult: EventFlow<Auth> = _tokenResult

    sealed class AuthEvent : Event {
        class OnKakaoLoginSuccess(val token: String) : AuthEvent()
        class OnKakaoLoginFailed(val message: String) : AuthEvent()
        class OnAuthTokenRefresh() : AuthEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AuthEvent.OnKakaoLoginSuccess -> loginWithDelibuddyApi(token = event.token)
            is AuthEvent.OnKakaoLoginFailed -> showToast(message = event.message)
            is AuthEvent.OnAuthTokenRefresh -> refreshAuthTokenApi()
        }
    }

    private suspend fun loginWithDelibuddyApi(token: String) {
        when (val result = fetchAuthUseCase.invoke(token)) {
            is NetworkResult.Success -> {
                val auth = Auth.mapToAuth(result.data)
                _tokenResult.emit(auth)
            }
            is NetworkResult.Error -> handleError(result) {}
        }
    }

    private suspend fun refreshAuthTokenApi() {
        when (val result = refreshAuthUseCase.invoke(Unit)) {
            is NetworkResult.Success -> {
                val auth = Auth.mapToAuth(result.data)
                _tokenResult.emit(auth)
            }
            is NetworkResult.Error -> handleError(result) {}
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

