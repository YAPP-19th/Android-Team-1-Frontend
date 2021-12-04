package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.repository.CoordToAddressRepository
import javax.inject.Inject

typealias LatLngPair = Pair<Double, Double>

class CoordToAddressUseCase @Inject constructor(
    private val coordToAddressRepository: CoordToAddressRepository
) : BaseUseCase<NetworkResult<Address>, LatLngPair>() {
    override suspend fun run(
        params: LatLngPair
    ): NetworkResult<Address> {
        val latitude = params.first
        val longitude = params.second

        return coordToAddressRepository.convertCoordToAddress(latitude, longitude)
    }
}