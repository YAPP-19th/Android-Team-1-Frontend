package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.*

interface PartyRepository {
    suspend fun getPartiesInCircle(point: String, distance: Int): NetworkResult<List<PartyEntity>>
    suspend fun getPartiesInGeom(): NetworkResult<Unit>
    suspend fun createParty(request: PartyCreationRequestEntity): NetworkResult<PartyInformationEntity>
    suspend fun getPartyInformation(id: Int): NetworkResult<PartyInformationEntity>
    suspend fun editParty(id: Int, request: PartyEditRequestEntity): NetworkResult<Boolean>
    suspend fun deleteParty(id: Int): NetworkResult<Boolean>
    suspend fun banFromParty(id: String, request: PartyBanRequestEntity): NetworkResult<Unit>
    suspend fun joinParty(id: Int): NetworkResult<Boolean>
    suspend fun leaveParty(id: String): NetworkResult<Unit>
    suspend fun changeStatus(id: Int, request: StatusChangeRequestEntity): NetworkResult<Boolean>
}
