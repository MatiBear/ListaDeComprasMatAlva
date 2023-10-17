package com.example.listadecomprasmatalv

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.Adapters.ItemListAdapter
import com.example.listadecomprasmatalv.Adapters.ShoppingListAdapter
import com.example.listadecomprasmatalv.DataStore.ShoppingList
import com.example.listadecomprasmatalv.DataStore.ShoppingListData
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list)

        val newListButton: Button = findViewById(R.id.newListButton)
        recyclerView = findViewById(R.id.shopListRecycleView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val lists = ShoppingListData.getLists()

        // Create an adapter for the RecyclerView
        val adapter = ShoppingListAdapter(lists)

        // Attach the adapter to the RecyclerView
        recyclerView.adapter = adapter

        newListButton.setOnClickListener {
            createNewShoppingList() // Handle "New List" button click event
            adapter.notifyDataSetChanged() // Notify the adapter that data has changed
        }

        // ----------------------------------------------------------------------------------------
        // Header
        val backButton = findViewById<Button>(R.id.backButton)
        val budgetText = findViewById<TextView>(R.id.budgetText)
        val purchaseButton = findViewById<Button>(R.id.purchaseButton)

        // Back
        backButton.setOnClickListener {
            finish()
        }

        // Budget
        val sharedPref: SharedPreferences = getSharedPreferences("MyBudget", MODE_PRIVATE)
        val budgetValue: Int = sharedPref.getInt("budget", 0)
        budgetText.text = "Presupuesto: $$budgetValue"

        // To Purchase
        purchaseButton.setOnClickListener {
            val intent = Intent(this, ToPurchaseListActivity::class.java)
            startActivity(intent)
        }

        val purchaseListSize = ToPurchaseList.getItems().size
        if (purchaseListSize > 0) {
            purchaseButton.text = "Comprar ($purchaseListSize)"
        } else {
            purchaseButton.text = "Comprar"
        }
        // ----------------------------------------------------------------------------------------
    }

    override fun onResume() {
        super.onResume()

        val updatedLists = ShoppingListData.getLists()
        adapter = ShoppingListAdapter(updatedLists)
        val recyclerView: RecyclerView = findViewById(R.id.shopListRecycleView)
        recyclerView.adapter = adapter
    }


    private fun createNewShoppingList() {
        val uniqueId = ShoppingListData.generateUniqueId()
        val defaultName = "Lista Nueva $uniqueId"
        val newList = ShoppingList(uniqueId, defaultName, mutableListOf()) // Use mutableListOf()
        ShoppingListData.addList(newList)

        adapter.notifyDataSetChanged()
    }
}