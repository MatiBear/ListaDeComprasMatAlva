package com.example.listadecomprasmatalv.DataClasses

data class StoreItem(
    val itemId: Int,
    val itemName: String,
    val itemImageResourceId: Int,
    val itemType: String,
    val basePrice: Int,
    val purchasedFromStore: String, // Store name
    val units: Double,
    val measurement: String, // 'unit', 'kg', 'liter', etc.
    var amountPurchased: Double = 0.0, // Amount purchased, initialized to 0
    var isSelected: Boolean = false,
    var totalPrice: Int = 0,
) {
    init {
        amountPurchased = 0.0;
        totalPrice = 0
    }

    fun calculateTotalPrice(): Int {
        return ((basePrice / units) * amountPurchased).toInt()
    }
}

