package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.PartyBanRequestEntity
import yapp.android1.domain.entity.PartyCreationRequestEntity
import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.entity.PartyInformationEntity

interface PartyRepository {
    suspend fun getPartiesInCircle(point: String, distance: Int): NetworkResult<List<PartyEntity>>
    suspend fun getPartiesInGeom(): NetworkResult<Unit>
    suspend fun createParty(request: PartyCreationRequestEntity): NetworkResult<PartyInformationEntity>
    suspend fun getPartyInformation(id: Int): NetworkResult<PartyInformationEntity>
    suspend fun editParty(id: String): NetworkResult<Unit>
    suspend fun deleteParty(id: String): NetworkResult<Unit>
    suspend fun banFromParty(id: String, request: PartyBanRequestEntity): NetworkResult<Unit>
    suspend fun joinParty(id: String): NetworkResult<Unit>
    suspend fun leaveParty(id: String): NetworkResult<Unit>
}
