package yapp.android1.delibuddy.ui.myparty.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.GetMyPartiesUseCase
import javax.inject.Inject

@HiltViewModel
class MyPartyViewModel @Inject constructor(
    private val getMyPartiesUseCase: GetMyPartiesUseCase
) : BaseViewModel<Event>() {

    private val _myParties = MutableStateFlow<List<PartyInformation>>(emptyList())
    val myParties: StateFlow<List<PartyInformation>>
        get() = _myParties

    sealed class MyPartyEvent : Event {
        object GetMyPartiesUseCase : MyPartyEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is MyPartyEvent.GetMyPartiesUseCase -> getMyParties()
        }
    }

    private suspend fun getMyParties() {
        when (val result = getMyPartiesUseCase.invoke(Unit)) {
            is NetworkResult.Success -> {
                val parties = result.data.map { PartyInformation.toPartyInformation(it) }
                _myParties.emit(parties)
            }
            is NetworkResult.Error -> {
            }
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