package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CommentCreationRequestEntity
import yapp.android1.domain.entity.CommentEntity


interface CommentRepository {
    suspend fun getCommentsInParty(partyId: Int): NetworkResult<List<CommentEntity>>
    suspend fun deleteComment(commentId: Int): NetworkResult<Boolean>
    suspend fun createComment(commentCreationRequest: CommentCreationRequestEntity): NetworkResult<CommentEntity>

}