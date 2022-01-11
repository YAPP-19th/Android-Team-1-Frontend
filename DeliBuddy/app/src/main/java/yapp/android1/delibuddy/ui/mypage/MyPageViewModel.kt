package yapp.android1.delibuddy.ui.mypage

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.User
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.GetMyInfoUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : BaseViewModel<MyPageViewModel.MyPageEvent>() {

    private val _myInfo = MutableStateFlow<User?>(null)
    val myInfo: MutableStateFlow<User?> = _myInfo

    private val _currentAddress = MutableStateFlow<Address?>(null)
    val currentAddress: StateFlow<Address?> = _currentAddress

    sealed class MyPageEvent : Event

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            when (val result = getMyInfoUseCase.invoke()) {
                is NetworkResult.Success -> {
                    _myInfo.value = User.mapToUser(result.data)
                    _currentAddress.value = DeliBuddyApplication.prefs.getCurrentUserAddress()
                }

                is NetworkResult.Error -> handleError(result) {}
            }
        }
    }

    override suspend fun handleEvent(event: MyPageEvent) {}
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
