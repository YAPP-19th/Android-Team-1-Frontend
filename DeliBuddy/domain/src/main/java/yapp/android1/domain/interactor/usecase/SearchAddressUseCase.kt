package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.Address
import yapp.android1.domain.repository.AddressRepository

class SearchAddressUseCase(
    private val addressRepository: AddressRepository
) : BaseUseCase<List<Address>, String>() {
    override suspend fun run(params: String): List<Address> {
        val responseAddress = addressRepository.searchAddressByAddress(params)
        val responseKeyword = addressRepository.searchAddressByKeyword(params)

        return when {
            responseAddress is NetworkResult.Success &&
                    responseKeyword is NetworkResult.Success -> {
                if (responseAddress.data.size > responseKeyword.data.size) {
                    responseAddress.data + responseKeyword.data
                } else {
                    responseKeyword.data + responseAddress.data
                }
            }
            responseAddress is NetworkResult.Success -> responseAddress.data
            responseKeyword is NetworkResult.Success -> responseKeyword.data
            else -> emptyList()
        }
    }
}