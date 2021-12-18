package yapp.android1.delibuddy.ui.partyInformation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.Comment
import yapp.android1.delibuddy.model.Event
import yapp.android1.delibuddy.model.Party
import yapp.android1.delibuddy.model.PartyInformation
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationEvent
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.FetchPartyCommentsUseCase
import yapp.android1.domain.interactor.usecase.FetchPartyInformationUseCase
import javax.inject.Inject

@HiltViewModel
class PartyInformationViewModel @Inject constructor(
    private val fetchPartyInformationUseCase: FetchPartyInformationUseCase,
    private val fetchPartyCommentsUseCase: FetchPartyCommentsUseCase
) : BaseViewModel<PartyInformationEvent>() {

    private val _isOwner = MutableStateFlow<Boolean>(false)
    val isOwner = _isOwner.asStateFlow()

    private val _party = MutableStateFlow<PartyInformation>(PartyInformation.EMPTY)
    val party = _party.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private var currentUserId = -1

    sealed class PartyInformationEvent : Event {
        class OnIntent(val data: Party, val currentUserId: Int) : PartyInformationEvent()
        class OnStatusChanged(val status: PartyStatus) : PartyInformationEvent()
    }

    override suspend fun handleEvent(event: PartyInformationEvent) {
        when(event) {
            is PartyInformationEvent.OnIntent -> {
                currentUserId = event.currentUserId
                setPartyWithoutLeader(event.data)
                fetchPartyInformation(_party.value.id)
                fetchPartyComments(_party.value.id)
            }

            is PartyInformationEvent.OnStatusChanged -> {
                if(party.value.status != event.status.value) {
                    //ChangeStatus
                }
            }
        }
    }

    private fun setPartyWithoutLeader(party: Party) {
        _party.value = PartyInformation(
            id               = party.id,
            allStatuses      = party.allStatuses,
            body             = party.body,
            category         = party.category,
            coordinate       = party.coordinate,
            currentUserCount = party.currentUserCount,
            openKakaoUrl     = party.openKakaoUrl,
            orderTime        = party.orderTime,
            placeName        = party.placeName,
            placeNameDetail  = party.placeNameDetail,
            status           = party.status,
            targetUserCount  = party.targetUserCount,
            title            = party.title,
            leader           = PartyInformation.Leader.EMPTY
        )
    }

    private fun fetchPartyInformation(partyId: Int) = viewModelScope.launch {
        val result = fetchPartyInformationUseCase.invoke(partyId)
        when(result) {
            is NetworkResult.Success -> {
                _party.value = PartyInformation.toPartyInformation(result.data)
                setPartyOwnerState(party.value.leader.id)
            }
            is NetworkResult.Error -> {
               handleError(result, null)
            }
        }
    }

    private fun setPartyOwnerState(ownerId: Int) {
        if(currentUserId == ownerId) {
            _isOwner.value = true
        }
    }

    private fun fetchPartyComments(partyId: Int) = viewModelScope.launch {
        val result = fetchPartyCommentsUseCase.invoke(partyId)
        when(result) {
            is NetworkResult.Success -> {
                val comments = result.data.map { comment ->
                    Comment.fromCommentEntity(comment)
                }
                _comments.value = comments
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }
    }

    override suspend fun handleError(result: NetworkResult.Error, retryAction: RetryAction?) {
        when(result.errorType) {
            is NetworkError.Unknown -> {
                showToast("알 수 없는 에러가 발생했습니다.")
            }
            is NetworkError.Timeout -> {
                showToast("타임 아웃 에러가 발생했습니다.")
            }
            is NetworkError.InternalServer -> {
                showToast("내부 서버에서 오류가 발생했습니다.")
            }
            is NetworkError.Unknown -> {
                showToast("알수없는 에러 발생")
            }
        }
    }
}
