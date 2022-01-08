package yapp.android1.delibuddy.model

import yapp.android1.delibuddy.ui.partyInformation.model.PartyStatus
import yapp.android1.delibuddy.util.parseDate
import yapp.android1.domain.entity.PartyInformationEntity
import java.io.Serializable


data class PartyInformation(
    val allStatuses: List<String>,
    val body: String,
    val category: Category,
    val coordinate: String,
    val currentUserCount: Int,
    val id: Int,
    val openKakaoUrl: String?,
    val orderTime: String,
    val placeName: String?,
    val placeNameDetail: String?,
    val status: PartyStatus,
    val targetUserCount: Int,
    val title: String,
    val isIn: Boolean,
    val leader: Leader,
    val users: List<User>
) : Serializable {

    data class Leader(
        val id: Int,
        val nickName: String,
        val partiesCnt: Int,
        val profileImage: String
    ) : Serializable {
        companion object {
            val EMPTY = Leader(
                id           = -1,
                nickName     = "",
                partiesCnt   = 0,
                profileImage = ""
            )

            fun toLeader(entity: PartyInformationEntity.LeaderEntity): Leader {
                return Leader(
                    id           = entity.id,
                    nickName     = entity.nickName,
                    partiesCnt   = entity.partiesCnt,
                    profileImage = entity.profileImage
                )
            }
        }
    }

    data class User(
        val id: Int,
        val nickName: String,
        val partiesCnt: Int,
        val profileImage: String
    ) : Serializable {
        companion object {
            fun toUser(entity: PartyInformationEntity.UserEntity): User {
                return User(
                    id           = entity.id,
                    nickName     = entity.nickName,
                    partiesCnt   = entity.partiesCnt,
                    profileImage = entity.profileImage
                )
            }
        }

    }

    companion object {
        val EMPTY = PartyInformation(
            allStatuses      = emptyList(),
            body             = "",
            category         = Category.EMPTY,
            coordinate       = "",
            currentUserCount = -1,
            id               = -1,
            openKakaoUrl     = "",
            orderTime        = "",
            placeName        = "",
            placeNameDetail  = "",
            status           = PartyStatus.RECRUIT,
            targetUserCount  = -1,
            title            = "",
            isIn             = false,
            leader           = Leader.EMPTY,
            users            = emptyList()
        )

        fun toPartyInformation(partyEntity: PartyInformationEntity): PartyInformation {
            return PartyInformation(
                allStatuses      = partyEntity.allStatuses,
                body             = partyEntity.body,
                category         = Category.mapToCategory(partyEntity.category),
                coordinate       = partyEntity.coordinate,
                currentUserCount = partyEntity.currentUserCount,
                id               = partyEntity.id,
                openKakaoUrl     = partyEntity.openKakaoUrl,
                orderTime        = parseDate(partyEntity.orderTime),
                placeName        = partyEntity.placeName,
                placeNameDetail  = partyEntity.placeNameDetail,
                status           = PartyStatus.of(partyEntity.status),
                targetUserCount  = partyEntity.targetUserCount,
                title            = partyEntity.title,
                isIn             = partyEntity.isIn,
                leader           = Leader.toLeader(partyEntity.leader),
                users            = partyEntity.users.map { User.toUser(it) }
            )
        }
    }
}