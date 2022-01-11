package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CategoryEntity
import yapp.android1.domain.entity.UserEntity
import yapp.android1.domain.repository.CategoryListRepository
import yapp.android1.domain.repository.UserRepository
import javax.inject.Inject

class GetMyInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun invoke(): NetworkResult<UserEntity> {
        return userRepository.getMyInfo()
    }
}
