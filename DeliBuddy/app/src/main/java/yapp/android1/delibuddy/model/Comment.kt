package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.CommentEntity


data class Comment(
    val id: Int,
    val parentId: Int? = null,
    val partyId: Int,
    val writer: Writer,
    val body: String,
    val createAt: String,
    val childComments: List<Comment>? = emptyList()
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

    val viewType = if(parentId != null) PARENT else CHILD

    fun hasChildComments(): Boolean {
        return childComments != null && childComments.isNotEmpty()
    }

    companion object {
        const val PARENT = 1
        const val CHILD = 2

        fun fromCommentEntity(entity: CommentEntity): Comment {
            return Comment(
                id = entity.id,
                parentId = entity.parentId,
                partyId = entity.partyId,
                writer = Writer.fromWriterEntity(entity.writer),
                body = entity.body,
                createAt = entity.createAt,
                childComments = entity.childComments?.map { Comment.fromCommentEntity(it) }
            )
        }
    }

}

