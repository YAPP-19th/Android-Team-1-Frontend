package yapp.android1.delibuddy.ui.home.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.address.AddressSharedEvent
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.GetPartiesInCircleUseCase
import javax.inject.Inject

typealias LocationRange = Pair<String, Int>

@HiltViewModel
class PartiesViewModel @Inject constructor(
    private val getPartiesInCircleUseCase: GetPartiesInCircleUseCase
) : BaseViewModel<Event>() {

    private val _partiesResult = MutableStateFlow<List<Party>>(emptyList())
    val partiesResult: StateFlow<List<Party>> = _partiesResult

    private val _userAddress = MutableStateFlow<Address>(Address.DEFAULT)
    val userAddress: StateFlow<Address> = _userAddress

    private val _saveAddressEvent = MutableStateFlow<SaveAddressEvent>(SaveAddressEvent.Idle)
    val saveAddressEvent: StateFlow<SaveAddressEvent> = _saveAddressEvent

    sealed class PartiesEvent : Event {
        class GetPartiesInCircle(val locationRange: LocationRange) : PartiesEvent()
        class SaveAddress(val address: Address) : PartiesEvent()
    }

    sealed class SaveAddressEvent : Event {
        object Idle : SaveAddressEvent()
        class Success(val userAddress: String) : SaveAddressEvent()
        object Failed : SaveAddressEvent()
    }

    init {
        DeliBuddyApplication.prefs.getCurrentUserAddress()?.let {
            _userAddress.value = it
        }
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is PartiesEvent.GetPartiesInCircle -> getPartiesInCircle(event.locationRange)

            is PartiesEvent.SaveAddress -> {
                saveAddress(event.address)
            }
        }
    }

    private fun saveAddress(address: Address) {
        DeliBuddyApplication.prefs.saveUserAddress(address)

        if (DeliBuddyApplication.prefs.getCurrentUserAddress() == null) {
            _saveAddressEvent.value = SaveAddressEvent.Failed
            return
        }

        _saveAddressEvent.value = SaveAddressEvent.Success(
            DeliBuddyApplication.prefs.getCurrentUserAddress()!!.address
        )
        _userAddress.value = address
    }

    private suspend fun getPartiesInCircle(locationRange: LocationRange) {
        when (val result = getPartiesInCircleUseCase.invoke(locationRange)) {
            is NetworkResult.Success -> {
                val parties = result.data.map { Party.toParty(it) }
                _partiesResult.emit(parties)
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
