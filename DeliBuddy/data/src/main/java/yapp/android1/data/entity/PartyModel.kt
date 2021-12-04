package yapp.android1.data.entity

import yapp.android1.domain.entity.CategoryEntity


data class PartyModel(
    val id: Int,
    val title: String,
    val body: String,
    val coordinate: String,
    val orderTime: String,
    val targetUserCount: Int,
    val category: CategoryModel,
    val allStatuses: List<String>
)