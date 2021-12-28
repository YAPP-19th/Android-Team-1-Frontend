package yapp.android1.delibuddy.model

import yapp.android1.domain.entity.CategoryEntity
import java.io.Serializable

data class Category(
    val backgroundColorCode: String,
    val iconUrl: String,
    val id: Int,
    val name: String,
) : Serializable {
    companion object {
        val EMPTY = Category(
            backgroundColorCode = "",
            iconUrl = "",
            id = -1,
            name = ""
        )

        fun mapToCategory(categoryEntity: CategoryEntity): Category {
            return Category(
                backgroundColorCode = categoryEntity.backgroundColorCode,
                iconUrl = categoryEntity.iconUrl,
                id = categoryEntity.id,
                name = categoryEntity.name
            )
        }
    }
}
