package yapp.android1.domain.entity


data class StatusChangeRequestEntity(
    val status: String
) {
    companion object {
        fun fromStringToEntity(status: String): StatusChangeRequestEntity {
            return StatusChangeRequestEntity(
                status = status
            )
        }
    }
}