package yapp.android1.delibuddy.ui.partyInformation

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
import yapp.android1.domain.entity.PartyBanRequestEntity
import yapp.android1.domain.interactor.usecase.*
import javax.inject.Inject

@HiltViewModel
class PartyInformationViewModel @Inject constructor(
    private val fetchPartyInformationUseCase: FetchPartyInformationUseCase,
    private val fetchPartyCommentsUseCase   : FetchPartyCommentsUseCase,
    private val jointPartyUseCase           : JoinPartyUseCase,
    private val changeStatusUseCase         : ChangeStatusUseCase,
    private val createCommentUseCase        : CreateCommentUseCase,
    private val deletePartyUseCase          : DeletePartyUseCase,
    private val deleteCommentUseCase        : DeleteCommentUseCase,
    private val banFromPartyUseCase         : BanFromPartyUseCase
) : BaseViewModel<PartyInformationAction>() {

    private val _event = MutableEventFlow<PartyInformationEvent>()
    val event = _event.asEventFlow()

    private val _isOwner = MutableStateFlow<Boolean>(false)
    val isOwner = _isOwner.asStateFlow()

    private val _party = MutableStateFlow<PartyInformation>(PartyInformation.EMPTY)
    val party = _party.asStateFlow()

    private val _comments = MutableStateFlow<Comments>(Comments.EMPTY)
    val comments = _comments.asStateFlow()

    private val _targetParentComment = MutableStateFlow<Comment?>(null)
    val targetParentComment = _targetParentComment.asStateFlow()

    private var currentUserId = -1

    sealed class PartyInformationAction : Event {
        // [Party]
        class OnIntent(val data: Party, val currentUserId: Int) : PartyInformationAction()
        class OnStatusChanged(val status: PartyStatus) : PartyInformationAction()
        object OnJointPartyClicked : PartyInformationAction()
        object OnDeletePartyMenuClicked : PartyInformationAction()
        object PartyEditSuccess : PartyInformationAction()
        object PartyEditFailed : PartyInformationAction()

        // [Comment]
        class DeleteComment(val commentId: Int) : PartyInformationAction()
        class WriteComment(val body: String) : PartyInformationAction()
        class OnCommentWriteTextViewClicked(val parentComment: Comment) : PartyInformationAction()
        object DeleteTargetComment : PartyInformationAction()

        // [User]
        class BanUserFromParty(val targetUser: PartyInformation.User) : PartyInformationAction()
    }

    sealed class PartyInformationEvent : Event {
        // [Party]
        class OnPartyJoinSuccess(val openKakaoTalkUrl: String) : PartyInformationEvent()
        object OnPartyJoinFailed : PartyInformationEvent()
        object PartyDeleteSuccess : PartyInformationEvent()
        object PartyDeleteFailed : PartyInformationEvent()

        // [Comment]
        object OnCreateCommentSuccess : PartyInformationEvent()
        object OnCreateCommentFailed : PartyInformationEvent()
        class ShowTargetParentComment(val parentComment: Comment) : PartyInformationEvent()
        object HideTargetParentComment : PartyInformationEvent()
        object CommentDeleteSuccess : PartyInformationEvent()
        object CommentDeleteFailed : PartyInformationEvent()

        // [User]
        object UserBanSuccess : PartyInformationEvent()
        object UserBanFailed : PartyInformationEvent()
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
                if (_party.value.isIn == false) {
                    joinParty()
                    _event.emit(PartyInformationEvent.OnPartyJoinSuccess(_party.value.openKakaoUrl!!))
                } else {
                    _event.emit(PartyInformationEvent.OnPartyJoinSuccess(_party.value.openKakaoUrl!!))
                }
            }

            is PartyInformationAction.WriteComment -> {
                if (isWritingChildComment()) {
                    createComment(
                        body     = action.body,
                        parentId = (_targetParentComment.value as Comment).id
                    )
                } else {
                    createComment(body = action.body)
                }
            }

            is PartyInformationAction.DeleteComment -> {
                deleteComment(action.commentId)
            }

            is PartyInformationAction.DeleteTargetComment -> {
                _targetParentComment.value = null
            }

            is PartyInformationAction.OnCommentWriteTextViewClicked -> {
                _targetParentComment.value = action.parentComment
                _event.emit(PartyInformationEvent.ShowTargetParentComment(action.parentComment))
            }

            is PartyInformationAction.OnDeletePartyMenuClicked -> {
                deleteParty()
            }

            is PartyInformationAction.PartyEditSuccess -> {
                fetchPartyInformation(_party.value.id)
            }

            is PartyInformationAction.BanUserFromParty -> {
                banFromParty(action.targetUser)
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
            status           = PartyStatus.of(party.status),
            targetUserCount  = party.targetUserCount,
            title            = party.title,
            isIn             = false,
            leader           = PartyInformation.Leader.EMPTY,
            users            = emptyList()
        )
    }

    private fun isWritingChildComment() = _targetParentComment.value != null

    private suspend fun createComment(body: String, parentId: Int? = null) {
        val params = CommentCreationRequestEntity(
            body     = body,
            parentId = parentId,
            partyId  = _party.value.id
        )

        when (val result = createCommentUseCase.invoke(CreateCommentUseCase.Params(requestEntity = params))) {
            is NetworkResult.Success -> {
                fetchPartyComments(_party.value.id)
                _targetParentComment.value = null
                _event.emit(PartyInformationEvent.OnCreateCommentSuccess)
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }
    }

    private suspend fun deleteComment(commentId: Int) {
        when (val result = deleteCommentUseCase.invoke(commentId)) {
            is NetworkResult.Success -> {
                if (result.data == true) {
                    fetchPartyComments(_party.value.id)
                    _event.emit(PartyInformationEvent.CommentDeleteSuccess)
                } else {
                    _event.emit(PartyInformationEvent.CommentDeleteFailed)
                }
            }

            is NetworkResult.Error -> {
                _event.emit(PartyInformationEvent.CommentDeleteFailed)
            }
        }
    }

    private suspend fun changePartyStatus(partyId: Int, changedStatus: String) {
        val result = changeStatusUseCase.invoke(
            params = ChangeStatusUseCase.Params(
                partyId       = partyId,
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

    private suspend fun joinParty() {
        when (val result = jointPartyUseCase.invoke(_party.value.id)) {
            is NetworkResult.Success -> {
                if (result.data == true) {
                    fetchPartyInformation(_party.value.id)
                    _event.emit(PartyInformationEvent.OnPartyJoinSuccess(_party.value.openKakaoUrl!!))
                } else {
                    _event.emit(PartyInformationEvent.OnPartyJoinFailed)
                }
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }
    }

    private suspend fun deleteParty() {
        when (val result = deletePartyUseCase.invoke(_party.value.id)) {
            is NetworkResult.Success -> {
                if (result.data == true) {
                    _event.emit(PartyInformationEvent.PartyDeleteSuccess)
                } else {
                    _event.emit(PartyInformationEvent.PartyDeleteFailed)
                }
            }
            is NetworkResult.Error -> {
                _event.emit(PartyInformationEvent.PartyDeleteFailed)
            }
        }
    }

    private suspend fun fetchPartyInformation(partyId: Int) {
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

    private suspend fun fetchPartyComments(partyId: Int) {
        val result = fetchPartyCommentsUseCase.invoke(partyId)
        when (result) {
            is NetworkResult.Success -> {
                val comments = result.data.map { entity -> Comment.fromCommentEntity(entity) }
                _comments.value = Comments.of(comments)
            }
            is NetworkResult.Error -> {
                handleError(result, null)
            }
        }
    }

    private suspend fun banFromParty(user: PartyInformation.User) {
        val params = BanFromPartyUseCase.Params(
            partyId       = _party.value.id,
            requestEntity = PartyBanRequestEntity(targetId = user.id)
        )

        when (val result = banFromPartyUseCase(params)) {
            is NetworkResult.Success -> {
                val isSuccess = result.data

                if (isSuccess) {
                    fetchPartyInformation(_party.value.id)
                    _event.emit(PartyInformationEvent.UserBanSuccess)
                } else {
                    _event.emit(PartyInformationEvent.UserBanFailed)
                }
            }
            is NetworkResult.Error -> {
                _event.emit(PartyInformationEvent.UserBanFailed)
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
