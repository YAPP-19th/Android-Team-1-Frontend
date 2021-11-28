package yapp.android1.delibuddy.ui.address.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.CoordToAddressUseCase
import javax.inject.Inject

@HiltViewModel
class AddressDetailViewModel @Inject constructor(
    private val coordToAddressUseCase: CoordToAddressUseCase
) : BaseViewModel<Event>() {
    private var job: Job? = null

    private val _addressResult = MutableStateFlow<Address?>(null)
    val addressResult: StateFlow<Address?> = _addressResult

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is AddressDetailEvent.SaveAddress -> {
                saveAddress(event.address)
            }

            is AddressDetailEvent.CoordToAddress -> {
                convertCoordToAddress(event.lat, event.lng)
            }
        }
    }

    private fun saveAddress(address: Address) {
        DeliBuddyApplication.prefs.saveUserAddress(address)
        val test = DeliBuddyApplication.prefs.getCurrentUserAddress()
        Timber.w("Save Success ${test.addressName}, lat: ${test.lat}, lon: ${test.lon}")
    }

    private fun convertCoordToAddress(lat: Double, lng: Double) {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = coordToAddressUseCase(Pair<Double, Double>(lat, lng))) {
                is NetworkResult.Success -> {
                    Timber.w("convert coord to address network success")
                    val address = result.data
                    _addressResult.value = address
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        Timber.w("convert coord to address network error")
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
