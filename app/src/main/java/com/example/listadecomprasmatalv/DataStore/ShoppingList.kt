package com.example.listadecomprasmatalv.DataStore

import com.example.listadecomprasmatalv.DataClasses.StoreItem

data class ShoppingList(
    val id: Int, // Unique identifier for the list
    var name: String,
    val items: MutableList<StoreItem>, // Use MutableList for items in the list
)

object ShoppingListData {
    private val shoppingLists = mutableListOf<ShoppingList>()
    private var lastAssignedId = 0

    fun addList(list: ShoppingList) {
        shoppingLists.add(list)
    }

    fun removeList(list: ShoppingList) {
        shoppingLists.remove(list)
    }

    fun getLists(): List<ShoppingList> {
        return shoppingLists
    }

    fun clearLists() {
        shoppingLists.clear()
    }

    fun generateUniqueId(): Int {
        lastAssignedId++
        return lastAssignedId
    }

    fun addItemToList(listId: Int, item: StoreItem) {
        val shoppingList = shoppingLists.find { it.id == listId }
        shoppingList?.items?.add(item)
    }

    fun removeItemFromList(listId: Int, item: StoreItem) {
        val shoppingList = shoppingLists.find { it.id == listId }
        shoppingList?.items?.remove(item)
    }
}
