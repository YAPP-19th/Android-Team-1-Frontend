package yapp.android1.delibuddy.ui.login.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Auth
import yapp.android1.delibuddy.model.Auth.Companion.EMPTY
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.util.user.KakaoLoginModule
import yapp.android1.delibuddy.util.user.UserLoginManager
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.FetchAuthUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val fetchAuthUseCase: FetchAuthUseCase,
    private val userManager: UserLoginManager,
) : BaseViewModel<Event>() {

    private val _tokenResult = MutableStateFlow<Auth>(EMPTY)
    val tokenResult: StateFlow<Auth> = _tokenResult.asStateFlow()

    sealed class AuthEvent : Event {
        class OnKakaoLoginSuccess(val token: String) : AuthEvent()
        class OnKakaoLoginFailed(val message: String) : AuthEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AuthEvent.OnKakaoLoginSuccess -> loginWithDelibuddyApi(token = event.token)
            is AuthEvent.OnKakaoLoginFailed -> showToast(message = event.message)
        }
    }

    private suspend fun loginWithDelibuddyApi(token: String) {
        when (val result = fetchAuthUseCase.invoke(token)) {
            is NetworkResult.Success -> {
                val auth = Auth.mapToAuth(result.data)
                userManager.setDeliBuddyAuth(auth)
                _tokenResult.value = auth
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

