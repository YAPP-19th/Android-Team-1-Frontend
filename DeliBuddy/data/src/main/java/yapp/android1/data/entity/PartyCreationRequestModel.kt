package yapp.android1.data.entity

import yapp.android1.domain.entity.PartyCreationRequestEntity
import java.io.Serializable

data class PartyCreationRequestModel(
    val body: String,
    val categoryId: Int,
    val coordinate: String,
    val openKakaoUrl: String,
    val orderTime: String,
    val placeName: String,
    val placeNameDetail: String,
    val targetUserCount: Int,
    val title: String
) : Serializable {
    companion object {
        fun toPartyCreationRequestEntity(model: PartyCreationRequestModel): PartyCreationRequestEntity {
            return PartyCreationRequestEntity(
                body = model.body,
                categoryId = model.categoryId,
                coordinate = model.coordinate,
                openKakaoUrl = model.openKakaoUrl,
                orderTime = model.orderTime,
                placeName = model.placeName,
                placeNameDetail = model.placeNameDetail,
                targetUserCount = model.targetUserCount,
                title = model.title
            )
        }

        fun fromPartyCreationRequest(entity: PartyCreationRequestEntity): PartyCreationRequestModel {
            return PartyCreationRequestModel(
                body = entity.body,
                categoryId = entity.categoryId,
                coordinate = entity.coordinate,
                openKakaoUrl = entity.openKakaoUrl,
                orderTime = entity.orderTime,
                placeName = entity.placeName,
                placeNameDetail = entity.placeNameDetail,
                targetUserCount = entity.targetUserCount,
                title = entity.title
            )
        }
    }
}
