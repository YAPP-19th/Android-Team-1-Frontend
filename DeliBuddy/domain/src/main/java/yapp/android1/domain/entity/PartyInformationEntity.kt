package yapp.android1.domain.entity

data class PartyInformationEntity(
    val allStatuses: List<String>,
    val body: String,
    val category: CategoryEntity,
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
    val leader: LeaderEntity,
) {
    data class LeaderEntity(
        val id: Int,
        val nickName: String,
        val partiesCnt: Int,
        val profileImage: String
    )

}