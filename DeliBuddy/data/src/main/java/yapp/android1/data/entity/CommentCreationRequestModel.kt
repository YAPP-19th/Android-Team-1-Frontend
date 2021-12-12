package yapp.android1.data.entity

import retrofit2.http.Body
import yapp.android1.domain.entity.CommentCreationRequestEntity


data class CommentCreationRequestModel(
    val body: String,
    val parentId: Int,
    val partyId: Int
) {
    companion object {
        fun fromCommentCreationRequestEntity(entity: CommentCreationRequestEntity): CommentCreationRequestModel {
            return CommentCreationRequestModel(
                body = entity.body,
                parentId = entity.parentId,
                partyId = entity.partyId
            )
        }
    }
}