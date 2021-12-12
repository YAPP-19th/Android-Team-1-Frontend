package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.CategoryEntity

data class Category(
    val backgroundColorCode: String,
    val code: String?,
    val iconUrl: String,
    val id: Int,
    val name: String,
) {
    companion object {
        val EMPTY = Category(
            backgroundColorCode = "",
            code = null,
            iconUrl = "",
            id = -1,
            name = ""
        )

        fun toCategory(categoryEntity: CategoryEntity): Category {
            return Category(
                backgroundColorCode = categoryEntity.backgroundColorCode,
                code = categoryEntity.code,
                iconUrl = categoryEntity.iconUrl,
                id = categoryEntity.id,
                name = categoryEntity.name
            )
        }
    }
}
