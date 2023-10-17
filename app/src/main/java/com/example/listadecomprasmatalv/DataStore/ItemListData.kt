package com.example.listadecomprasmatalv.DataStore

import com.example.listadecomprasmatalv.R

object StoreListData {
    val baseItems = listOf(
        StoreItemBase(1, "Tomate", R.drawable.tomato, "Frutas",
            900, 1.5, "Kg"),
        StoreItemBase(2, "Lechuga", R.drawable.lettuce, "Vegetales",
            800, 1.0, "Un"),
        StoreItemBase(3, "Papas", R.drawable.potatos, "Vegetales",
            750, 1.5, "Kg"),
        StoreItemBase(4, "Jugo", R.drawable.juice, "Liquidos",
            1200, 1.5, "L"),
        StoreItemBase(5, "Coca-Cola", R.drawable.drink, "Liquidos",
            1500, 1.5, "L"),
        StoreItemBase(6, "Amoxicilina", R.drawable.amoxilina, "Antibioticos",
            3500, 1.0, "Un"),
        StoreItemBase(7, "Aspirina", R.drawable.aspirina, "Salicilatos",
            3000, 1.0, "Un"),
        StoreItemBase(8, "Colgate", R.drawable.colgate, "Dental",
            2500, 1.0, "Un"),
        // Define more base items
    )
}

data class StoreItemBase(
    val itemId: Int,
    val itemName: String,
    val itemImageResourceId: Int,
    val itemType: String,
    val basePrice: Int,
    val units: Double,
    val measurement: String
)