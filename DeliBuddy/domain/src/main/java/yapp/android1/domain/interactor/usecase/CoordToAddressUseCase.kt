package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.repository.CoordToAddressRepository
import javax.inject.Inject

class CoordToAddressUseCase @Inject constructor(
    private val coordToAddressRepository: CoordToAddressRepository
) : BaseUseCase<NetworkResult<Address>, Pair<Double, Double>>() {
    override suspend fun run(
        params: Pair<Double, Double>
    ): NetworkResult<Address> {
        val latitude = params.first
        val longitude = params.second

        return coordToAddressRepository.coordToAddress(latitude, longitude)
    }
}