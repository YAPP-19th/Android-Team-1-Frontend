package yapp.android1.domain.repository

import yapp.android1.domain.NetworkResult
import yapp.android1.domain.entity.CategoryEntity

interface CategoryListRepository {
    suspend fun fetchCategoryList(): NetworkResult<List<CategoryEntity>>
}