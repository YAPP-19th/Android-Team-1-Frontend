package yapp.android1.data.repository

import yapp.android1.data.entity.MapAddress
import yapp.android1.data.remote.KakaoLocalApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.interactor.KakaoNetworkErrorHandler
import yapp.android1.domain.repository.CoordToAddressRepository
import javax.inject.Inject

class CoordToAddressRepositoryImpl @Inject constructor(
    private val api: KakaoLocalApi,
    private val kakaoNetworkErrorHandler: KakaoNetworkErrorHandler
) : CoordToAddressRepository {
    override suspend fun convertCoordToAddress(lat: Double, lng: Double): NetworkResult<Address> {
        return try {
            val response = api.convertCoordToAddress(lng = lng.toString(), lat = lat.toString())

            NetworkResult.Success(
                MapAddress.toAddress(lat, lng, response.coordAddressDocuments[0])
            )
        } catch (e: Exception) {
            val errorType = kakaoNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }
}