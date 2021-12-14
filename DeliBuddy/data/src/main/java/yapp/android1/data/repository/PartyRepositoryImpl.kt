package yapp.android1.data.repository

import yapp.android1.data.entity.PartyBanRequestModel
import yapp.android1.data.entity.PartyCreationRequestModel
import yapp.android1.data.entity.PartyModel
import yapp.android1.data.remote.PartyApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.PartyBanRequestEntity
import yapp.android1.domain.entity.PartyCreationRequestEntity
import yapp.android1.domain.entity.PartyEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.PartyRepository
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val api: PartyApi,
    private val deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler,
) : PartyRepository {
    override suspend fun getPartiesInCircle(point: String, distance: Int): NetworkResult<List<PartyEntity>> {
        return try {
            val response = api.getPartiesInCircle(point, distance)
            NetworkResult.Success(
                response.map {
                    PartyModel.toPartyEntity(it)
                }
            )
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun getPartiesInGeom(): NetworkResult<Unit> {
        return try {
            val response = api.getPartiesInGeom()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun createParty(request: PartyCreationRequestEntity): NetworkResult<List<PartyEntity>> {
        return try {
            val response = api.createParty(
                PartyCreationRequestModel.fromPartyCreationRequest(request)
            )

            NetworkResult.Success(
                response.map {
                    PartyModel.toPartyEntity(it)
                }
            )
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun editParty(id: String): NetworkResult<Unit> {
        return try {
            val response = api.editParty(id)
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun deleteParty(id: String): NetworkResult<Unit> {
        return try {
            val response = api.deleteParty(id)
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun banFromParty(id: String, request: PartyBanRequestEntity): NetworkResult<Unit> {
        return try {
            val response = api.banFromParty(id, PartyBanRequestModel.fromPartyBanRequest(request))
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun joinParty(id: String): NetworkResult<Unit> {
        return try {
            val response = api.joinParty(id)
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun leaveParty(id: String): NetworkResult<Unit> {
        return try {
            val response = api.leaveParty(id)
            NetworkResult.Success(response)
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }
}
