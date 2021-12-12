package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.CommentEntity


data class Comment(
    val id: Int,
    val parentId: Int? = null,
    val partyId: Int,
    val writer: Writer?,
    val body: String,
    val createdAt: String,
    val children: List<Comment> = emptyList()
) {
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

    val viewType = if(parentId == null) PARENT else CHILD

    fun hasChildComments(): Boolean {
        return children.isNotEmpty()
    }

    companion object {
        const val PARENT = 1
        const val CHILD = 2

        fun fromCommentEntity(entity: CommentEntity): Comment {
            return Comment(
                id = entity.id,
                parentId = entity.parentId,
                partyId = entity.partyId,
                writer = if(entity.writer == null) null else Writer.fromWriterEntity(entity.writer!!),
                body = entity.body,
                createdAt = entity.createdAt,
                children = entity.children.map { Comment.fromCommentEntity(it) }
            )
        }
    }

}

