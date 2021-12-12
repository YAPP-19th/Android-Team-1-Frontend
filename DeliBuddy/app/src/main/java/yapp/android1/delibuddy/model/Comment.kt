package yapp.android1.delibuddy.model


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
    )

    val viewType = if(parentId != null) PARENT else CHILD

    fun hasChildComments(): Boolean {
        return childComments != null && childComments.isNotEmpty()
    }

    companion object {
        const val PARENT = 1
        const val CHILD = 2
    }

}

