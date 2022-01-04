package yapp.android1.domain.entity


data class CommentCreationRequestEntity(
    val body: String,
    val parentId: Int?,
    val partyId: Int
)