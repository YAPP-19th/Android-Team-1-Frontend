package yapp.android1.delibuddy.ui.address.detail

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.entity.NetworkError
import javax.inject.Inject

@HiltViewModel
class AddressDetailViewModel @Inject constructor(
) : BaseViewModel<Event>() {
    private var job: Job? = null

//    private val _address = MutableStateFlow<Address?>(null)
//    val address: StateFlow<Address?> = _address

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AddressDetailEvent.SaveAddress -> {
                saveAddress(address = event.address)
            }
        }
    }

    private fun saveAddress(address: Address) {
        DeliBuddyApplication.prefs.saveUserAddress(address)
        val test = DeliBuddyApplication.prefs.getCurrentUserAddress()
        Timber.w("Save Success ${test.addressName}, lat: ${test.lat}, lon: ${test.lon}")
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