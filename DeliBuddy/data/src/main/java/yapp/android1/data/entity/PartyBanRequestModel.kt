package yapp.android1.data.entity

import yapp.android1.domain.entity.PartyBanRequestEntity

data class PartyBanRequestModel(
    val targetId: Int,
) {
    companion object {
        fun fromPartyBanRequest(entity: PartyBanRequestEntity): PartyBanRequestModel {
            return PartyBanRequestModel(
                targetId = entity.targetId
            )
        }
    }
}
