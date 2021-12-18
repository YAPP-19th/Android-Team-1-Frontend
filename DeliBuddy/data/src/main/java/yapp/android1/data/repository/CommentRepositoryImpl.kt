package yapp.android1.data.repository

import yapp.android1.data.entity.CommentCreationRequestModel
import yapp.android1.data.entity.CommentModel
import yapp.android1.data.remote.CommentApi
import yapp.android1.data.remote.DeliBuddyApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentCreationRequestEntity
import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.CommentRepository
import javax.inject.Inject


class CommentRepositoryImpl @Inject constructor(
    private val api: CommentApi,
    private val networkErrorHandler: DeliBuddyNetworkErrorHandler
) : CommentRepository {

    override suspend fun getCommentsInParty(partyId: Int): NetworkResult<List<CommentEntity>> {

            val commentModels = api.getCommentsInParty(partyId)
            val commentEntities = commentModels.map { model ->
                CommentModel.toCommentEntity(model)
            }
            return NetworkResult.Success(commentEntities)

    }

    override suspend fun deleteComment(commentId: Int): NetworkResult<Boolean> {
        try {
            val deleteResult = api.deleteComment(commentId)
            return NetworkResult.Success(deleteResult)
        } catch (t: Throwable) {
            val errorType = networkErrorHandler.getError(t)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun createComment(commentCreationRequest: CommentCreationRequestEntity): NetworkResult<CommentEntity> {
        try {
            val createdComment = api.createComment(CommentCreationRequestModel.fromCommentCreationRequestEntity(commentCreationRequest))
            return NetworkResult.Success(CommentModel.toCommentEntity(createdComment))
        } catch (t: Throwable) {
            val errorType = networkErrorHandler.getError(t)
            return NetworkResult.Error(errorType)
        }
    }
}