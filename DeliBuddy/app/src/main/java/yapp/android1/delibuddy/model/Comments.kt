package yapp.android1.delibuddy.model


class Comments(comments: List<Comment>) {

    var value: List<Comment> = comments.toList()
        private set

    fun addComment(newComment: Comment): Comments {
        if (newComment.parentId != null) {
            for(comment in value) {
                if(comment.id == newComment.parentId) {
                    comment.children.value = comment.children.value + newComment
                }
            }

            return Comments(value)
        } else {
            return Comments(value + newComment)
        }
    }

    companion object {
        val EMPTY = Comments(emptyList())
    }
}