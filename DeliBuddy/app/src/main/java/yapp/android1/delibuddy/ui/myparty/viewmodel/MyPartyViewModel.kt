package yapp.android1.delibuddy.ui.myparty.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.util.EventFlow
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.GetMyPartiesUseCase
import yapp.android1.domain.interactor.usecase.LeavePartyUseCase
import javax.inject.Inject

@HiltViewModel
class MyPartyViewModel @Inject constructor(
    private val getMyPartiesUseCase: GetMyPartiesUseCase,
    private val leavePartyUseCase: LeavePartyUseCase,
) : BaseViewModel<Event>() {

    private val _myParties = MutableStateFlow<List<PartyInformation>>(emptyList())
    val myParties: StateFlow<List<PartyInformation>>
        get() = _myParties

    private val _leaveParty = MutableEventFlow<Boolean>()
    val leaveParty: EventFlow<Boolean>
        get() = _leaveParty

    sealed class MyPartyEvent : Event {
        object OnGetMyParties : MyPartyEvent()
        class OnLeavePartyMenuClicked(val id: Int) : MyPartyEvent()
    }

    override suspend fun handleEvent(event: Event) {
        when (event) {
            is MyPartyEvent.OnGetMyParties -> getMyParties()
            is MyPartyEvent.OnLeavePartyMenuClicked -> leaveParty(event.id)
        }
    }

    private suspend fun getMyParties() {
        when (val result = getMyPartiesUseCase.invoke(Unit)) {
            is NetworkResult.Success -> {
                val parties = result.data.map { PartyInformation.toPartyInformation(it) }
                _myParties.emit(parties)
            }
            is NetworkResult.Error -> handleError(result) {}
        }
    }

    private suspend fun leaveParty(id: Int) {
        when (val result = leavePartyUseCase.invoke(id)) {
            is NetworkResult.Success -> {
                val okay = result.data
                _leaveParty.emit(okay)
                showToast("????????? ??????????????? ???????????????.")
            }
            is NetworkResult.Error -> handleError(result) {}
        }
    }


    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        when (result.errorType) {
            is NetworkError.Unknown -> {
                showToast("??? ??? ?????? ????????? ??????????????????.")
            }
            is NetworkError.Timeout -> {
                showToast("?????? ?????? ????????? ??????????????????.")
            }
            is NetworkError.InternalServer -> {
                showToast("?????? ???????????? ????????? ??????????????????.")
            }
            is NetworkError.BadRequest -> {
                val code = (result.errorType as NetworkError.BadRequest).code
                val msg = (result.errorType as NetworkError.BadRequest).message
                showToast("?????? ?????? ${code}, $msg")
            }
        }
    }
}