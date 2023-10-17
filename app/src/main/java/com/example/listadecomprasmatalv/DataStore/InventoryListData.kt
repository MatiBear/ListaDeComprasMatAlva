package com.example.listadecomprasmatalv.DataStore

import com.example.listadecomprasmatalv.DataClasses.StoreItem

object InventoryListData {
    private val itemList = mutableListOf<StoreItem>()

    fun addItem(item: StoreItem) {
        itemList.add(item)
    }

    fun removeItem(item: StoreItem) {
        itemList.remove(item)
    }

    fun getItems(): List<StoreItem> {
        return itemList
    }

    fun clearItems() {
        itemList.clear()
    }
}