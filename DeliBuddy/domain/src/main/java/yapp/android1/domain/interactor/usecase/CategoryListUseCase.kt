package yapp.android1.domain.interactor.usecase

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CategoryEntity
import yapp.android1.domain.repository.CategoryListRepository
import javax.inject.Inject

class CategoryListUseCase @Inject constructor(
    private val categoryListRepository: CategoryListRepository
) {
    suspend fun invoke(): NetworkResult<List<CategoryEntity>> {
        return categoryListRepository.fetchCategoryList()
    }
}