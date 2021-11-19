package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.repository.AddressRepository

class SearchAddressUseCase(
    private val addressRepository: AddressRepository
) : BaseUseCase<List<Address>, String>() {
    override suspend fun run(params: String): List<Address> {
        val response = addressRepository.fetchAddressByAddress(params)
        val response2 = addressRepository.fetchAddressByKeyword(params)

        return when {
            response is NetworkResult.Success &&
                    response2 is NetworkResult.Success -> {
                if (response.data.size > response2.data.size) {
                    response.data + response2.data
                } else {
                    response2.data + response.data
                }
            }
            response is NetworkResult.Success -> response.data
            response2 is NetworkResult.Success -> response2.data
            else -> emptyList()
        }
    }
}