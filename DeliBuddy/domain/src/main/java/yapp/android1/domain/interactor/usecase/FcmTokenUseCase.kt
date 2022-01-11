package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.repository.UserRepository

class FcmTokenUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<NetworkResult<Boolean>, String>() {
    override suspend fun run(params: String): NetworkResult<Boolean> {
        return userRepository.setFcmToken(params)
    }
}