package yapp.android1.delibuddy.model

import yapp.android1.delibuddy.util.parseCommentDate
import yapp.android1.delibuddy.util.parseDate
import yapp.android1.domain.entity.CommentEntity

abstract class CommentType(
    open val id: Int
)

data class Comment(
    override val id: Int,
    val parentId: Int? = null,
    val partyId: Int,
    val writer: Writer?,
    val body: String,
    val createdAt: String,
    val children: List<ChildComment> = emptyList()
) : CommentType(id) {
    data class Writer(
        val nickName: String,
        val profileImage: String
    ) {
        companion object {
            fun fromWriterEntity(entity: CommentEntity.WriterEntity): Writer {
                return Writer(
                    nickName = entity.nickName,
                    profileImage = entity.profileImage
                )
            }
        }
    }

    companion object {

        fun fromCommentEntity(entity: CommentEntity): Comment {
            return Comment(
                id = entity.id,
                parentId = entity.parentId,
                partyId = entity.partyId,
                writer = if(entity.writer == null) null else Writer.fromWriterEntity(entity.writer!!),
                body = entity.body,
                createdAt = parseCommentDate(entity.createdAt),
                children = entity.children.map { fromCommentEntity(it) }
                    .map { ChildComment.toChildComment(it) }
            )
        }
    }

}

data class ChildComment(
    override val id: Int,
    val parentId: Int,
    val partyId: Int,
    val writer: Comment.Writer?,
    val body: String,
    val createdAt: String,
) : CommentType(id) {
    companion object {
        fun toChildComment(comment: Comment): ChildComment {
            return ChildComment(
                id = comment.id,
                parentId = comment.parentId!!,
                partyId = comment.partyId,
                writer = comment.writer,
                body = comment.body,
                createdAt = comment.createdAt
            )
        }
    }
}

data class EmptyComment(
    override val id: Int = -1
) : CommentType(id)

