package com.example.listadecomprasmatalv.DataClasses

data class Store(
    val id: Int,
    val name: String,
    val openingClosingTime: String,
    val storeType: String,
    val address: String,
    val imageResourceId: Int,
    val completeDetails: String,
    val paymentType: String,
    val website: String,
    val customerSupportEmail: String,
    val storeItems: List<StoreItem>
)