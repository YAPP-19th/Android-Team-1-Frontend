package yapp.android1.data.entity

import yapp.android1.domain.entity.StatusChangeRequestEntity


data class StatusChangeRequestModel(
    val status: String
) {
    companion object {
        fun fromEntitiyToModel(entity: StatusChangeRequestEntity): StatusChangeRequestModel {
            return StatusChangeRequestModel(
                status = entity.status
            )
        }
    }
}