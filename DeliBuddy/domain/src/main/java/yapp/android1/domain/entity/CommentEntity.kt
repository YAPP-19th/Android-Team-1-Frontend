package yapp.android1.domain.entity


data class CommentEntity(
    val id: Int,
    val parentId: Int?,
    val partyId: Int,
    val writer: WriterEntity,
    val body: String,
    val createAt: String,
    val childComments: List<CommentEntity>?
) {
    data class WriterEntity(
        val nickName: String,
        val profileImage: String
    )
}