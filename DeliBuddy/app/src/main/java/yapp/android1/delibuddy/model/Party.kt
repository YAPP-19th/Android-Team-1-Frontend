package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.CategoryEntity
import yapp.android1.domain.entity.PartyEntity


class Party(
    val id: Int,
    val title: String,
    val body: String,
    val coordinate: String,
    val orderTime: String,
    val targetUserCount: Int,
    val category: FoodType,
    val allStatuses: List<String>
) {
    companion object {
        val EMPTY = Party(
            id              = -1,
            title           = "",
            body            = "",
            coordinate      = "",
            orderTime       = "",
            targetUserCount = -1,
            category        = FoodType.Korean,
            allStatuses     = emptyList()
        )

        fun mapToParty(entity: PartyEntity): Party {
            return Party(
                id              = entity.id,
                title           = entity.title,
                body            = entity.body,
                coordinate      = entity.coordinate,
                orderTime       = entity.orderTime,
                targetUserCount = entity.targetUserCount,
                category        = FoodType.of(entity.category.name),
                allStatuses     = entity.allStatuses
            )
        }
    }
}