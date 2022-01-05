package yapp.android1.delibuddy.model


class Comments private constructor(combinedComments: List<Comment>) {

    companion object {
        val EMPTY = Comments(emptyList())

        fun of(value: List<Comment>): Comments {
            return Comments(value)
        }
    }

    var value: List<CommentType>? = null
        get() = field?.toList()
        private set

    init {
        value = convertFlatten(combinedComments)
    }

    private fun convertFlatten(comments: List<Comment>): List<CommentType> {
        val tempList = mutableListOf<CommentType>()

        for (comment in comments) {
            tempList.add(comment)
            for (childComment in comment.children) {
                tempList.add(childComment)
            }
        }

        return tempList
    }

}