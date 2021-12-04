package yapp.android1.delibuddy.model


class Comment(
    val id: Int,
    val parentId: Int?,
    val partyId: Int,
    val writer: Writer,
    val body: String,
    val createAt: String,
    val childComment: List<Comment>? = emptyList()
) {
    val viewType = if (parentId == null) PARENT else CHILD

    data class Writer(
        val nickName: String,
        val profileImage: String
    )

    companion object {
        const val PARENT = 1
        const val CHILD = 2
    }
}

