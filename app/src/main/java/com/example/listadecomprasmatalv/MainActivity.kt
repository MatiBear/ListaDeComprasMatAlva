package com.example.listadecomprasmatalv

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.listadecomprasmatalv.DataClasses.StoreItem
import com.example.listadecomprasmatalv.DataStore.ShoppingList
import com.example.listadecomprasmatalv.DataStore.ShoppingListData

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        val budgetTextView: TextView = findViewById(R.id.budgetTextView)
        val sharedPref: SharedPreferences = getSharedPreferences("MyBudget", Context.MODE_PRIVATE)
        val budgetValue: Int = sharedPref.getInt("budget", 0)
        budgetTextView.text = "Presupuesto: $$budgetValue" // Show Budget

        // Create a preset list with an item
        /*val presetList = ShoppingList(1, "Preset List", mutableListOf())
        val presetItem = StoreItem(
            itemId = 1,
            itemName = "Preset Item",
            itemType = "Type",
            measurement = "Unit",
            basePrice = 10,
            itemImageResourceId = R.drawable.amoxilina,
            units = 1.0,
            purchasedFromStore = "Store"
        )
        presetList.items.add(presetItem)
        ShoppingListData.addList(presetList)*/

    }

    fun openUserProfileInterface(view: View) {
        val intent = Intent(this, UserProfileActivity::class.java) // Replace with the name of your new interface activity
        startActivity(intent)
    }

    fun openStoreListInterface(view: View) {
        val intent = Intent(this, StoreListActivity::class.java) // Replace with the name of your new interface activity
        startActivity(intent)
    }

    fun openShopListInterface(view: View) {
        val intent = Intent(this, ShoppingListActivity::class.java) // Replace with the name of your new interface activity
        startActivity(intent)
    }

    fun openInvetoryListInterface(view: View) {
        val intent = Intent(this, InventoryActivity::class.java) // Replace with the name of your new interface activity
        startActivity(intent)
    }
}