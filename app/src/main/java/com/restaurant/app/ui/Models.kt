package com.restaurant.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object Palette {
    val menu = listOf(
        Food("1", "Трактирная пицца", "Моцарелла, томаты, базилик", 82.0, "🍕", "Популярное"),
        Food("2", "Фирменный бургер", "Говядина, сыр, пикантный соус", 68.0, "🍔", "Хит"),
        Food("3", "Лосось на гриле", "Овощи, лимонный соус", 124.0, "🐟", "Шеф советует"),
        Food("4", "Куркума суп", "Кокосовое молоко, овощи", 38.0, "🍲", "Новинка"),
        Food("5", "Паста карбонара", "Пармезан, бекон, яйцо", 76.0, "🍝", "Популярное"),
        Food("6", "Фруктовый десерт", "Ягоды, крем, мята", 32.0, "🍰", "Сладкое"),
    )
}

data class Food(val id: String, val name: String, val description: String, val price: Double, val emoji: String, val tag: String)
data class CartItem(val food: Food, var quantity: Int = 1)
data class TableInfo(val number: Int, val seats: Int, val status: String)
