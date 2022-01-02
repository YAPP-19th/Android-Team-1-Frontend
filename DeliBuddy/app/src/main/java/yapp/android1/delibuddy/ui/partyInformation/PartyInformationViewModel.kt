package yapp.android1.delibuddy.ui.partyInformation

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yapp.android1.delibuddy.base.BaseViewModel
import yapp.android1.delibuddy.base.RetryAction
import yapp.android1.delibuddy.model.*
import yapp.android1.delibuddy.ui.partyInformation.PartyInformationViewModel.PartyInformationAction
import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus
import yapp.android1.delibuddy.util.MutableEventFlow
import yapp.android1.delibuddy.util.asEventFlow
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentCreationRequestEntity
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.usecase.*
import javax.inject.Inject

@HiltViewModel
class PartyInformationViewModel @Inject constructor(
    private val fetchPartyInformationUseCase: FetchPartyInformationUseCase,
    private val fetchPartyCommentsUseCase: FetchPartyCommentsUseCase,
    private val jointPartyUseCase: JoinPartyUseCase,
    private val changeStatusUseCase: ChangeStatusUseCase,
    private val createCommentUseCase: CreateCommentUseCase
) : BaseViewModel<PartyInformationAction>() {

    private val _event = MutableEventFlow<PartyInformationEvent>()
    val event = _event.asEventFlow()

    private val _hasJoined = MutableStateFlow<Boolean>(false)
    val hasJoined = _hasJoined.asStateFlow()

    private val _isOwner = MutableStateFlow<Boolean>(false)
    val isOwner = _isOwner.asStateFlow()

    private val _party = MutableStateFlow<PartyInformation>(PartyInformation.EMPTY)
    val party = _party.asStateFlow()

    private val _comments = MutableStateFlow<Comments>(Comments.EMPTY)
    val comments = _comments.asStateFlow()

    private var currentUserId = -1

    sealed class PartyInformationAction : Event {
        class OnIntent(val data: Party, val currentUserId: Int) : PartyInformationAction()
        class OnStatusChanged(val status: PartyStatus) : PartyInformationAction()
        object OnJointPartyClicked : PartyInformationAction()
        class OnWriteParentComment(val body: String) : PartyInformationAction()
        class OnCommentWriteTextViewClicked(val comment: Comment) : PartyInformationAction()
    }

    sealed class PartyInformationEvent : Event {
        object OnPartyJoinSuccess : PartyInformationEvent()
        object OnPartyJoinFailed : PartyInformationEvent()
        object OnCreateCommentSuccess : PartyInformationEvent()
        object OnCreateCommentFailed : PartyInformationEvent()
        class PopUpTargetComment(val comment: Comment) : PartyInformationEvent()
    }

    override suspend fun handleEvent(action: PartyInformationAction) {
        when (action) {
            is PartyInformationAction.OnIntent -> {
                currentUserId = action.currentUserId
                setPartyWithoutLeader(action.data)
                fetchPartyInformation(_party.value.id)
                fetchPartyComments(_party.value.id)
            }

            is PartyInformationAction.OnStatusChanged -> {
                val currentStatus = party.value.status.value
                val changedStatus = action.status.value

                if (currentStatus != changedStatus) {
                    changePartyStatus(_party.value.id, changedStatus)
                }
            }

            is PartyInformationAction.OnJointPartyClicked -> {
                if (_hasJoined.value == false) {
                    _hasJoined.value = true
                    joinParty()
                }
            }

            is PartyInformationAction.OnWriteParentComment -> {
                createComment(action.body)
            }

            is PartyInformationAction.OnCommentWriteTextViewClicked -> {
                _event.emit(PartyInformationEvent.PopUpTargetComment(action.comment))
            }

        }
    }

    private fun setPartyWithoutLeader(party: Party) {
        _party.value = PartyInformation(
            id = party.id,
            allStatuses = party.allStatuses,
            body = party.body,
            category = party.category,
            coordinate = party.coordinate,
            currentUserCount = party.currentUserCount,
            openKakaoUrl = party.openKakaoUrl,
            orderTime = party.orderTime,
            placeName = party.placeName,
            placeNameDetail = party.placeNameDetail,
            status = PartyStatus.of(party.status),
            targetUserCount = party.targetUserCount,
            title = party.title,
            leader = PartyInformation.Leader.EMPTY
        )
    }

    private fun createComment(body: String, parentId: Int? = null) = viewModelScope.launch {
        val params = CommentCreationRequestEntity(
            body = body,
            parentId = parentId,
            partyId = _party.value.id
        )
        val result = createCommentUseCase.invoke(
            CreateCommentUseCase.Params(requestEntity = params)
        )
        when (result) {
            is NetworkResult.Success -> {
                val comment = Comment.fromCommentEntity(result.data)
                _comments.value = _comments.value.addComment(newComment = comment)
                _event.emit(PartyInformationEvent.OnCreateCommentSuccess)
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }
    }

    private fun changePartyStatus(partyId: Int, changedStatus: String) = viewModelScope.launch {
        val result = changeStatusUseCase.invoke(
            params = ChangeStatusUseCase.Params(
                partyId = partyId,
                changedStatus = changedStatus
            )
        )

        when (result) {
            is NetworkResult.Success -> {
                if (result.data == true) {
                    _party.value = _party.value.copy(
                        status = PartyStatus.of(changedStatus)
                    )
                } else {
                    showToast("알 수 없는 에러가 발생했습니다.")
                }
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }

    }

    private fun joinParty() = viewModelScope.launch {
        when (val result = jointPartyUseCase.invoke(_party.value.id)) {
            is NetworkResult.Success -> {
                if (result.data == true) {
                    _event.emit(PartyInformationEvent.OnPartyJoinSuccess)
                } else {
                    _hasJoined.value = false
                    _event.emit(PartyInformationEvent.OnPartyJoinFailed)
                }
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }
    }

    private fun fetchPartyInformation(partyId: Int) = viewModelScope.launch {
        val result = fetchPartyInformationUseCase.invoke(partyId)
        when (result) {
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
        if (currentUserId == ownerId) {
            _isOwner.value = true
        }
    }

    private fun fetchPartyComments(partyId: Int) = viewModelScope.launch {
        val result = fetchPartyCommentsUseCase.invoke(partyId)
        when (result) {
            is NetworkResult.Success -> {
                val comments = result.data.map { comment ->
                    Comment.fromCommentEntity(comment)
                }
                _comments.value = Comments(comments)
            }
            is NetworkResult.Error -> {
                handleError(result, null)
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
            is NetworkError.Unknown -> {
                showToast("알수없는 에러 발생")
            }
        }
    }
}
