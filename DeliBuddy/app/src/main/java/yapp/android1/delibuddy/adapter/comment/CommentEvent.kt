package yapp.android1.delibuddy.adapter.comment

import yapp.android1.delibuddy.model.CommentType
import yapp.android1.delibuddy.model.Event

typealias CommentEventListener = (CommentEvent) -> Unit

sealed class CommentEvent : Event {
    class OnWriteCommentClicked(val comment: CommentType) : CommentEvent()
    class OnRemoveCommentClicked(val comment: CommentType) : CommentEvent()
}