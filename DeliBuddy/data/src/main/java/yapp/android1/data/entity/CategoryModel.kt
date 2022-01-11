package yapp.android1.data.entity

import yapp.android1.domain.entity.CategoryEntity

data class CategoryModel(
    val backgroundColorCode: String,
    val iconUrl: String,
    val id: Int,
    val name: String,
) {
    companion object {
        fun toCategoryEntity(categoryModel: CategoryModel): CategoryEntity {
            return CategoryEntity(
                backgroundColorCode = categoryModel.backgroundColorCode,
                iconUrl = categoryModel.iconUrl,
                id = categoryModel.id,
                name = categoryModel.name
            )
        }
    }
}
