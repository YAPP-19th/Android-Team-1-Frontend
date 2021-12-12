package yapp.android1.data.entity

import yapp.android1.domain.entity.CategoryEntity

import yapp.android1.domain.entity.PartyEntity

data class PartyModel(
    val allStatuses: List<String>,
    val body: String,
    val category: CategoryModel,
    val coordinate: String,
    val currentUserCount: Int,
    val id: Int,
    val orderTime: String,
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
                orderTime = partyModel.orderTime,
                targetUserCount = partyModel.targetUserCount,
                title = partyModel.title
            )
        }
    }
}