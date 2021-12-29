package yapp.android1.delibuddy.ui.home.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Address
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.Party
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.CoordToAddressUseCase
import yapp.android1.domain.interactor.usecase.GetPartiesInCircleUseCase
import javax.inject.Inject

typealias LocationRange = Pair<String, Int>

@HiltViewModel
class PartiesViewModel @Inject constructor(
    private val getPartiesInCircleUseCase: GetPartiesInCircleUseCase,
    private val convertCoordToAddressUseCase: CoordToAddressUseCase
) : BaseViewModel<Event>() {

    private val _partiesResult = MutableStateFlow<List<Party>>(emptyList())
    val partiesResult: StateFlow<List<Party>> = _partiesResult

    private val _userAddress = MutableStateFlow<Address>(Address.DEFAULT)
    val userAddress: StateFlow<Address> = _userAddress

    private val _saveAddressEvent = MutableStateFlow<SaveAddressEvent>(SaveAddressEvent.Idle)
    val saveAddressEvent: StateFlow<SaveAddressEvent> = _saveAddressEvent

    sealed class PartiesEvent : Event {
        class GetPartiesInCircle(val locationRange: LocationRange) : PartiesEvent()
    }

    sealed class UserAddressEvent : Event {
        class SaveUserAddressEvent(val address: Address) : UserAddressEvent()
        class GetCurrentAddressEvent(val currentLatLng: Pair<Double, Double>) : UserAddressEvent()
    }

    sealed class SaveAddressEvent : Event {
        object Idle : SaveAddressEvent()
        class Success(val userAddress: String) : SaveAddressEvent()
        object Failed : SaveAddressEvent()
    }

    init {
        DeliBuddyApplication.prefs.getCurrentUserAddress()?.let {
            Timber.w("init user address")
            _userAddress.value = it
        }
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is PartiesEvent.GetPartiesInCircle -> getPartiesInCircle(event.locationRange)
            is UserAddressEvent.SaveUserAddressEvent -> saveAddress(event.address)
            is UserAddressEvent.GetCurrentAddressEvent -> convertCoordToAddress(event.currentLatLng)
        }
    }

    private suspend fun saveAddress(address: Address) {
        DeliBuddyApplication.prefs.saveUserAddress(address)

        if (DeliBuddyApplication.prefs.getCurrentUserAddress() == null) {
            _saveAddressEvent.value = SaveAddressEvent.Failed
            showToast("주소 변경에 실패하였습니다.\n다시 시도해 주세요.")
            return
        }

        _saveAddressEvent.value = SaveAddressEvent.Success(
            DeliBuddyApplication.prefs.getCurrentUserAddress()!!.address
        )
        _userAddress.value = address
        showToast("주소 변경에 성공하였습니다.")
    }

    private suspend fun convertCoordToAddress(latLng: Pair<Double, Double>) {
        viewModelScope.launch {
            Timber.w("latLng: $latLng")
            //when (val result = convertCoordToAddressUseCase(Pair(37.5463968, 127.0727856))) {
            when (val result = convertCoordToAddressUseCase(latLng)) {
                is NetworkResult.Success -> {
                    Timber.w("success to convert")
                    _userAddress.value = Address.mapToAddress(result.data)
                    saveAddress(_userAddress.value)
                }

                is NetworkResult.Error -> {
                    //_isActivate.emit(false)
                    Timber.w("fail to convert")
                    showToast("현재 주소를 가져올 수 없습니다 주소를 지정해 주세요")
                    Timber.w("errorType: ${result.errorType}")
                }
            }
        }
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
