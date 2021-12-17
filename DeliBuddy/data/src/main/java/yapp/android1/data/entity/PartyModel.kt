package yapp.android1.data.entity

import yapp.android1.domain.entity.PartyEntity

data class PartyModel(
    val allStatuses: List<String>,
    val body: String,
    val category: CategoryModel,
    val coordinate: String,
    val currentUserCount: Int,
    val id: Int,
    val openKakaoUrl: String?,
    val orderTime: String,
    val placeName: String?,
    val placeNameDetail: String?,
    val status: String,
    val targetUserCount: Int,
    val title: String,
) {
    companion object {
        fun toPartyEntity(partyModel: PartyModel): PartyEntity {
            return PartyEntity(
                allStatuses = partyModel.allStatuses,
                body = partyModel.body,
                category = CategoryModel.toCategoryEntity(partyModel.category),
                coordinate = partyModel.coordinate,
                currentUserCount = partyModel.currentUserCount,
                id = partyModel.id,
                openKakaoUrl = partyModel.openKakaoUrl,
                orderTime = partyModel.orderTime,
                placeName = partyModel.placeName,
                placeNameDetail = partyModel.placeNameDetail,
                status = partyModel.status,
                targetUserCount = partyModel.targetUserCount,
                title = partyModel.title
            )
        }
    }
}

