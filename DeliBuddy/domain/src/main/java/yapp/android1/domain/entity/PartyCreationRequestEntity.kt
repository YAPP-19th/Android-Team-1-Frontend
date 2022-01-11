package yapp.android1.domain.entity

data class PartyCreationRequestEntity(
    val body: String,
    val categoryId: Int,
    val coordinate: String,
    val openKakaoUrl: String,
    val orderTime: String,
    val placeName: String,
    val placeNameDetail: String,
    val targetUserCount: Int,
    val title: String
)
