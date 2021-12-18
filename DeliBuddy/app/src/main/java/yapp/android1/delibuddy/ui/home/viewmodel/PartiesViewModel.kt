package yapp.android1.delibuddy.ui.home.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.DeliBuddyApplication
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.Party
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

    private val _userAddress = MutableStateFlow<String>("")
    val userAddress: StateFlow<String> = _userAddress

    sealed class PartiesEvent : Event {
        class GetPartiesInCircle(val locationRange: LocationRange) : PartiesEvent()
    }

    init {
        DeliBuddyApplication.prefs.getCurrentUserAddress()?.let {
            _userAddress.value = it.address
        }
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is PartiesEvent.GetPartiesInCircle -> getPartiesInCircle(event.locationRange)
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
