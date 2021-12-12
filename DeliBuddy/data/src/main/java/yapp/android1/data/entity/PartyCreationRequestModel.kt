package yapp.android1.data.entity

import yapp.android1.domain.entity.PartyCreationRequestEntity

data class PartyCreationRequestModel(
    val body: String,
    val categoryId: String,
    val coordinate: String,
    val orderTime: String,
    val targetUserCount: Int,
    val title: String,
) {
    companion object {
        fun toPartyCreationRequestEntity(model: PartyCreationRequestModel): PartyCreationRequestEntity {
            return PartyCreationRequestEntity(
                body = model.body,
                categoryId = model.categoryId,
                coordinate = model.coordinate,
                orderTime = model.orderTime,
                targetUserCount = model.targetUserCount,
                title = model.title
            )
        }

        fun fromPartyCreationRequest(entity: PartyCreationRequestEntity): PartyCreationRequestModel {
            return PartyCreationRequestModel(
                body = entity.body,
                categoryId = entity.categoryId,
                coordinate = entity.coordinate,
                orderTime = entity.orderTime,
                targetUserCount = entity.targetUserCount,
                title = entity.title
            )
        }
    }
}
