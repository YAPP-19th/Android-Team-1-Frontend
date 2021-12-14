package yapp.android1.delibuddy.ui.alarm.model

sealed class AlarmViewTypeModel(open val id: String) {
    data class AlarmDateViewTypeModel(
        override val id: String,
        val date: String,
    ) : AlarmViewTypeModel(id)

    data class AlarmCommentViewTypeModel(
        override val id: String,
        val comment: String,
        val subComment: String? = null,
    ) : AlarmViewTypeModel(id)

    data class AlarmSpacingViewTypeModel(
        override val id: String
    ) : AlarmViewTypeModel(id)
}
