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
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.CoordToAddressUseCase
import javax.inject.Inject

sealed class AddressDetailEvent : Event {
    class SaveAddress(val address: Address) : AddressDetailEvent()
    class CoordToAddress(val lat: Double, val lng: Double) : AddressDetailEvent()
}

@HiltViewModel
class AddressDetailViewModel @Inject constructor(
    private val coordToAddressUseCase: CoordToAddressUseCase
) : BaseViewModel<Event>() {
    private var job: Job? = null

    private val _addressResult = MutableStateFlow<Address?>(null)
    val addressResult: StateFlow<Address?> = _addressResult

    private val _isActivate = MutableEventFlow<Boolean>()
    val isActivate: EventFlow<Boolean> = _isActivate

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

        // save address test
        val test = DeliBuddyApplication.prefs.getCurrentUserAddress()
        Timber.w("Save Success ${test.addressName}, lat: ${test.lat}, lon: ${test.lng}")
    }

    private fun convertCoordToAddress(lat: Double, lng: Double) {
        job?.cancel()
        job = viewModelScope.launch {
            when (val result = coordToAddressUseCase(Pair<Double, Double>(lat, lng))) {
                is NetworkResult.Success -> {
                    Timber.w("success to convert")
                    _addressResult.value = result.data
                }

                is NetworkResult.Error -> handleError(result) {

                }
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        _isActivate.emit(false)
        Timber.w("fail to convert")

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
        super.onCleared()
    }
}
