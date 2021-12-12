package yapp.android1.data.entity

import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.entity.CommentEntity.*


data class CommentModel(
    val id: Int,
    val parentId: Int?,
    val partyId: Int,
    val writer: WriterModel,
    val body: String,
    val createAt: String,
    val childComments: List<CommentModel>?
) {
    data class WriterModel(
        val nickName: String,
        val profileImage: String
    ) {
        companion object {
            fun toWriterEntity(model: WriterModel): WriterEntity {
                return WriterEntity(
                    nickName = model.nickName,
                    profileImage = model.profileImage
                )
            }
        }
    }

    companion object {
        fun toCommentEntity(model: CommentModel): CommentEntity {
            return CommentEntity(
                id = model.id,
                parentId = model.parentId,
                partyId = model.partyId,
                writer =  WriterModel.toWriterEntity(model.writer),
                body = model.body,
                createAt = model.createAt,
                childComments = model.childComments?.map { CommentModel.toCommentEntity(it) }
            )
        }
    }
}