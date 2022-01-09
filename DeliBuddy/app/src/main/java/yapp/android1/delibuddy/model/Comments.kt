package yapp.android1.delibuddy.model


class Comments private constructor(combinedComments: List<CommentType>) {

    companion object {
        val EMPTY = Comments(listOf(EmptyComment()))

        fun of(value: List<Comment>): Comments {
            if(value.isEmpty()) return EMPTY

            return Comments(value)
        }
    }

    var value: List<CommentType>? = null
        get() = field?.toList()
        private set

    init {
        value = convertFlatten(combinedComments)
    }

    private fun convertFlatten(comments: List<CommentType>): List<CommentType> {
        val tempList = mutableListOf<CommentType>()

        if (isEmptyList(comments)){
            return comments
        }

        for (comment in comments) {
            comment as Comment
            tempList.add(comment)
            for (childComment in comment.children) {
                tempList.add(childComment)
            }
        }

        return tempList
    }

    private fun isEmptyList(comments: List<CommentType>): Boolean {
        return comments[0] is EmptyComment
    }

}