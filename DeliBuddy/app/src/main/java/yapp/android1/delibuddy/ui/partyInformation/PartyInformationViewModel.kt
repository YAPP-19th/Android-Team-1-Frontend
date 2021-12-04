package yapp.android1.delibuddy.ui.partyInformation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent
import yapp.android1.domain.NetworkResult


class PartyInformationViewModel : BaseViewModel<PartyInformationEvent>() {

    private val _isOwner = MutableStateFlow<Boolean>(false)
    val isOwner = _isOwner.asStateFlow()

    private val _party = MutableStateFlow<Party>(Party.EMPTY)
    val party = _party.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comment = _comments.asStateFlow()

    sealed class PartyInformationEvent : Event {
        class OnIntent(val data: Party) : PartyInformationEvent()
        class OnCommentSend(val body: String) : PartyInformationEvent()
        class OnStatusChanged(val status: String) : PartyInformationEvent()
    }

    override suspend fun handleEvent(event: PartyInformationEvent) {
        when(event) {
            is PartyInformationEvent.OnIntent -> {
                _party.value = event.data
                fetchPartyInformation(_party.value.id)
            }
        }
    }

    fun fetchPartyInformation(id: Int) = viewModelScope.launch {
        // 불러오기
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        TODO("Not yet implemented")
    }
}
