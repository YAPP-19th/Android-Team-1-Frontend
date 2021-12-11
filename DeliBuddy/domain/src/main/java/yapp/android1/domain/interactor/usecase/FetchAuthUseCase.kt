package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.AuthEntity
import yapp.android1.domain.repository.AuthRepository
import javax.inject.Inject

class FetchAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseUseCase<NetworkResult<AuthEntity>, String>() {

    override suspend fun run(params: String): NetworkResult<AuthEntity> {
        return authRepository.fetchAuthToken(params)
    }
}