package yapp.android1.delibuddy.model

import yapp.android1.delibuddy.R

sealed class FoodType(
    val type: String,
    val colorResource: Int,
    val bigIconResource: Int,
) {
    object Bread : FoodType(type = "Bread", colorResource = R.color.bread_background, bigIconResource = R.drawable.big_food_icon_bread)
    object Burger : FoodType(type = "Burger", colorResource = R.color.burger_background, bigIconResource = R.drawable.big_food_icon_burger)
    object Cafe : FoodType(type = "Cafe", colorResource = R.color.desert_background, bigIconResource = R.drawable.big_food_icon_cafe)
    object Chicken : FoodType(type = "Chicken", colorResource = R.color.chicken_background, bigIconResource = R.drawable.big_food_icon_chicken)
    object Chinese : FoodType(type = "Chinese", colorResource = R.color.chinese_background, bigIconResource = R.drawable.big_food_icon_chinese)
    object Japanese : FoodType(type = "Japanese", colorResource = R.color.japanese_background, bigIconResource = R.drawable.big_food_icon_japanese)
    object Korean : FoodType(type = "Korean", colorResource = R.color.korean_background, bigIconResource = R.drawable.big_food_icon_korean)
    object Pizza : FoodType(type = "Pizza", colorResource = R.color.pizza_background, bigIconResource = R.drawable.big_food_icon_pizza)
    object Seafood : FoodType(type = "Seafood", colorResource = R.color.seafood_background, bigIconResource = R.drawable.big_food_icon_seafood)
    object Snack : FoodType(type = "Snackbar", colorResource = R.color.snack_background, bigIconResource = R.drawable.big_food_icon_snackbar)
    object Western : FoodType(type = "Western", colorResource = R.color.western_background, bigIconResource = R.drawable.big_food_icon_western)

    companion object {
        fun of(type: String): FoodType {
            return when (type) {
                "Bread"     -> Bread
                "Burger"    -> Burger
                "Cafe"      -> Cafe
                "Chicken"   -> Chicken
                "Chinese"   -> Chinese
                "Japanese"  -> Japanese
                "Korean"    -> Korean
                "Pizza"     -> Pizza
                "Seafood"   -> Seafood
                "Snackbar"  -> Snack
                "Western"   -> Western
                else -> throw IllegalArgumentException("올바른 타입이 아닙니다")
            }
        }
    }
}

