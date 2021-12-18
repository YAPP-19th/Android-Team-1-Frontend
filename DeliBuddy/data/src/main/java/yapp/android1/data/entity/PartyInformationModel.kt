package yapp.android1.data.entity

import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.entity.PartyInformationEntity


data class PartyInformationModel(
    val allStatuses: List<String>,
    val body: String,
    val category: CategoryModel,
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
    val leader: LeaderModel,
) {
    data class LeaderModel(
        val id: Int,
        val nickName: String,
        val partiesCnt: Int,
        val profileImage: String
    ) {
        companion object {
            fun toLeaderEntity(model: LeaderModel): PartyInformationEntity.LeaderEntity {
                return PartyInformationEntity.LeaderEntity(
                    id = model.id,
                    nickName = model.nickName,
                    partiesCnt = model.partiesCnt,
                    profileImage = model.profileImage
                )
            }
        }
    }

    companion object {
        fun toPartyInformationEntity(model: PartyInformationModel): PartyInformationEntity {
            return PartyInformationEntity(
                allStatuses = model.allStatuses,
                body = model.body,
                category = CategoryModel.toCategoryEntity(model.category),
                coordinate = model.coordinate,
                currentUserCount = model.currentUserCount,
                id = model.id,
                openKakaoUrl = model.openKakaoUrl,
                orderTime = model.orderTime,
                placeName = model.placeName,
                placeNameDetail = model.placeNameDetail,
                status = model.status,
                targetUserCount = model.targetUserCount,
                title = model.title,
                leader = LeaderModel.toLeaderEntity(model.leader)
            )
        }
    }
}