package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.repository.CoordToAddressRepository
import javax.inject.Inject

class CoordToAddressUseCase @Inject constructor(
    private val coordToAddressRepository: CoordToAddressRepository
) : BaseUseCase<NetworkResult<Address>, Array<Double>>() {
    override suspend fun run(
        params: Array<Double>
    ): NetworkResult<Address> {
        val latitude = params[0]
        val longitude = params[1]

        return coordToAddressRepository.coordToAddress(latitude, longitude)
    }
}