package yapp.android1.domain.entity

data class PartyCreationRequestEntity(
    val body: String,
    val categoryId: String,
    val coordinate: String,
    val orderTime: String,
    val targetUserCount: Int,
    val title: String,
)
