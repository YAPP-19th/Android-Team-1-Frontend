package yapp.android1.data.entity

import yapp.android1.domain.entity.PartyEditRequestEntity


data class PartyEditRequestModel(
    val body: String,
    val coordinate: String,
    val title: String
) {
    companion object {
        fun fromEntity(entity: PartyEditRequestEntity): PartyEditRequestModel {
            return PartyEditRequestModel(
                body = entity.body,
                coordinate = entity.coordinate,
                title = entity.title
            )
        }
    }
}