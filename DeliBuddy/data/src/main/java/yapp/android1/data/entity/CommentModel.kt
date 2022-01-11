package yapp.android1.data.entity

import yapp.android1.domain.entity.CommentEntity
import yapp.android1.domain.entity.CommentEntity.*


data class CommentModel(
    val id: Int,
    val parentId: Int?,
    val partyId: Int,
    val writer: WriterModel?,
    val body: String,
    val createdAt: String,
    val children: List<CommentModel> = emptyList()
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
                writer =  if(model.writer == null) null else WriterModel.toWriterEntity(model.writer),
                body = model.body,
                createdAt = model.createdAt,
                children = model.children?.map { CommentModel.toCommentEntity(it) }
            )
        }
    }
}