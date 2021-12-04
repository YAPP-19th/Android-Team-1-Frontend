package yapp.android1.domain.entity


data class PartyEntity(
    val id: Int,
    val title: String,
    val body: String,
    val coordinate: String,
    val orderTime: String,
    val targetUserCount: Int,
    val category: CategoryEntity,
    val allStatuses: List<String>
)

