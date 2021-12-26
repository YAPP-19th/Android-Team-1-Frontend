package yapp.android1.data.repository

import yapp.android1.data.entity.*
import yapp.android1.data.remote.PartyApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.*
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

    override suspend fun joinParty(id: Int): NetworkResult<Boolean> {
        return runCatching {
            api.joinParty(id)
        }.fold(
            onSuccess = { model ->
                NetworkResult.Success(model.okay)
            },
            onFailure = { throwable ->
                val errorType = deliBuddyNetworkErrorHandler.getError(throwable)
                NetworkResult.Error(errorType)
            }
        )
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


    override suspend fun getPartyInformation(id: Int): NetworkResult<PartyInformationEntity> {
        try {
            val partyModel = api.getPartyInformation(id)
            return NetworkResult.Success(PartyInformationModel.toPartyInformationEntity(partyModel))
        } catch (t: Throwable) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = t)
            return NetworkResult.Error(errorType)
        }
    }
}
