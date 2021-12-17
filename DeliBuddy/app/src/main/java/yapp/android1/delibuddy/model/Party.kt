package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.PartyEntity
import java.io.Serializable

data class Party(
    val allStatuses: List<String>,
    val body: String,
    val category: Category,
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
) : Serializable {
    companion object {
        val EMPTY = Party(
            allStatuses = emptyList(),
            body = "",
            category = Category.EMPTY,
            coordinate = "",
            currentUserCount = -1,
            id = -1,
            openKakaoUrl = "",
            orderTime = "",
            placeName = "",
            placeNameDetail = "",
            status = "",
            targetUserCount = -1,
            title = ""
        )

        fun toParty(partyEntity: PartyEntity): Party {
            return Party(
                allStatuses = partyEntity.allStatuses,
                body = partyEntity.body,
                category = Category.toCategory(partyEntity.category),
                coordinate = partyEntity.coordinate,
                currentUserCount = partyEntity.currentUserCount,
                id = partyEntity.id,
                openKakaoUrl = partyEntity.openKakaoUrl,
                orderTime = partyEntity.orderTime,
                placeName = partyEntity.placeName,
                placeNameDetail = partyEntity.placeNameDetail,
                status = partyEntity.status,
                targetUserCount = partyEntity.targetUserCount,
                title = partyEntity.title
            )
        }
    }
}

