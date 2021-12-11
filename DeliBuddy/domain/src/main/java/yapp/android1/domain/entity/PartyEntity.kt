package yapp.android1.domain.entity

data class PartyEntity(
    val allStatuses: List<String>,
    val body: String,
    val category: CategoryEntity,
    val coordinate: String,
    val currentUserCount: Int,
    val id: Int,
    val orderTime: String,
    val targetUserCount: Int,
    val title: String,
)
