package yapp.android1.data.entity


data class CommentModel(
    val id: Int,
    val parentId: Int?,
    val partyId: Int,
    val writer: WriterModel,
    val body: String,
    val createAt: String,
    val childComment: List<CommentModel>?
) {
    data class WriterModel(
        val nickName: String,
        val profileImage: String
    )
}