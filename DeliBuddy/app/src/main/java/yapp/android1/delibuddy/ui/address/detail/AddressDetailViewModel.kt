package yapp.android1.delibuddy.ui.address.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import javax.inject.Inject

@HiltViewModel
class AddressDetailViewModel @Inject constructor(
) : BaseViewModel<Event>() {
    private var job: Job? = null

    override suspend fun handleEvent(event: Event) {
        when (event) {
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

    override fun onCleared() {
        job = null
    }
}