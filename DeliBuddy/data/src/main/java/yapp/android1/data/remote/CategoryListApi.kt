package yapp.android1.data.remote

import retrofit2.http.GET
import yapp.android1.data.entity.CategoryModel

interface CategoryListApi : DeliBuddyApi {
    @GET("api/v1/categories")
    suspend fun fetchCategoryList(): List<CategoryModel>
}