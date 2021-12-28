package yapp.android1.data.repository

import yapp.android1.data.entity.CategoryModel
import yapp.android1.data.remote.CategoryListApi
import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CategoryEntity
import yapp.android1.domain.interactor.DeliBuddyNetworkErrorHandler
import yapp.android1.domain.repository.CategoryListRepository

class CategoryListRepositoryImpl(
    private val api: CategoryListApi,
    private val deliBuddyNetworkErrorHandler: DeliBuddyNetworkErrorHandler
) : CategoryListRepository {
    override suspend fun fetchCategoryList(): NetworkResult<List<CategoryEntity>> {
        return try {
            val response = api.fetchCategoryList()

            NetworkResult.Success(
                response.map {
                    CategoryModel.toCategoryEntity(it)
                }
            )
        } catch (e: Exception) {
            val errorType = deliBuddyNetworkErrorHandler.getError(exception = e)
            return NetworkResult.Error(errorType)
        }
    }
}