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
    val orderTime: String,
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
            orderTime = "",
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
                orderTime = partyEntity.orderTime,
                targetUserCount = partyEntity.targetUserCount,
                title = partyEntity.title
            )
        }
    }
}

