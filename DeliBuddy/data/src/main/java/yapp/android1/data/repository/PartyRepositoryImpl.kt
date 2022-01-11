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
    override suspend fun getPartiesInCircle(
        point: String,
        distance: Int
    ): NetworkResult<List<PartyEntity>> {
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

    override suspend fun createParty(request: PartyCreationRequestEntity): NetworkResult<PartyInformationEntity> {
        return try {
            val response =
                api.createParty(PartyCreationRequestModel.fromPartyCreationRequest(request))
            NetworkResult.Success(PartyInformationModel.toPartyInformationEntity(response))
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }

    override suspend fun editParty(
        id: Int,
        request: PartyEditRequestEntity
    ): NetworkResult<Boolean> {
        return runCatching {
            api.editParty(id, partyEditRequestModel = PartyEditRequestModel.fromEntity(request))
        }.fold(
            onSuccess = { model ->
                NetworkResult.Success(model.okay)
            },
            onFailure = { throwable ->
                val errorType = deliBuddyNetworkErrorHandler.getError(exception = throwable)
                NetworkResult.Error(errorType)
            }
        )
    }

    override suspend fun deleteParty(id: Int): NetworkResult<Boolean> {
        return runCatching {
            api.deleteParty(id)
        }.fold(
            onSuccess = { model ->
                NetworkResult.Success(model.okay)
            },
            onFailure = { throwable ->
                val errorType = deliBuddyNetworkErrorHandler.getError(exception = throwable)
                NetworkResult.Error(errorType)
            }
        )
    }

    override suspend fun banFromParty(id: Int, request: PartyBanRequestEntity): NetworkResult<Boolean> {
        return runCatching {
            api.banFromParty(id, PartyBanRequestModel.fromPartyBanRequest(request))
        }.fold(
            onSuccess = { model ->
                NetworkResult.Success(model.okay)
            },
            onFailure = { throwable ->
                val errorType = deliBuddyNetworkErrorHandler.getError(exception = throwable)
                NetworkResult.Error(errorType)
            }
        )
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

    override suspend fun leaveParty(id: Int): NetworkResult<Boolean> {
        return try {
            val response = api.leaveParty(id)
            NetworkResult.Success(response.okay)
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

    override suspend fun changeStatus(
        id: Int,
        request: StatusChangeRequestEntity
    ): NetworkResult<Boolean> {
        return runCatching {
            api.changeStatus(id, StatusChangeRequestModel.fromEntitiyToModel(request))
        }.fold(
            onSuccess = { model ->
                NetworkResult.Success(model.okay)
            },
            onFailure = { throwable ->
                val errorType = deliBuddyNetworkErrorHandler.getError(exception = throwable)
                NetworkResult.Error(errorType)
            }
        )
    }

    override suspend fun getMyParties(): NetworkResult<List<PartyInformationEntity>> {
        return kotlin.runCatching {
            api.getMyParties()
        }.mapCatching { modelList ->
            modelList.map {
                PartyInformationModel.toPartyInformationEntity(it)
            }
        }.fold(
            onSuccess = { entity ->
                NetworkResult.Success(entity)
            },
            onFailure = { throwable ->
                val errorType = deliBuddyNetworkErrorHandler.getError(throwable)
                NetworkResult.Error(errorType)
            }
        )
    }
}