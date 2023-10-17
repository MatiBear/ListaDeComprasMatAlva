package com.example.listadecomprasmatalv

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.Adapters.ItemListAdapter
import com.example.listadecomprasmatalv.Adapters.ToPurchaseAdapter
import com.example.listadecomprasmatalv.DataClasses.StoreItem
import com.example.listadecomprasmatalv.DataStore.InventoryListData
import com.example.listadecomprasmatalv.DataStore.ShoppingList
import com.example.listadecomprasmatalv.DataStore.ShoppingListData
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList

@Suppress("DEPRECATION")
class ToPurchaseListActivity : AppCompatActivity(), ToPurchaseAdapter.OnRemoveItemClickListener {

    private val toPurchaseItems = ToPurchaseList.getItems()
    private lateinit var toPurchaseItemsAdapter: ToPurchaseAdapter // Declare the adapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.to_purchase_list) // Replace with your layout

        // Initialize the RecyclerView and adapter
        val recyclerView = findViewById<RecyclerView>(R.id.toPurchaseItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        toPurchaseItemsAdapter = ToPurchaseAdapter(toPurchaseItems)
        recyclerView.adapter = toPurchaseItemsAdapter

        // List Button
        val listButton = findViewById<Button>(R.id.moveToShoppingListButton)
        // Inside your activity, where "listButton" is a button
        listButton.setOnClickListener {
            moveItemsToShoppingListDialog()
        }

        // Budget display
        val budgetTextView: TextView = findViewById(R.id.budgetText)
        val sharedPref: SharedPreferences = getSharedPreferences("MyBudget", MODE_PRIVATE)
        val budgetValue: Int = sharedPref.getInt("budget", 0)
        budgetTextView.text = "Presupuesto: $$budgetValue"

        // Back Button
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Purchase Button
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val totalCost = calculateTotalCost()
            val sharedPref = getSharedPreferences("MyBudget", MODE_PRIVATE)
            val budgetValue = sharedPref.getInt("budget", 0)

            if (budgetValue >= totalCost) {
                // Budget is sufficient, show a confirmation dialog
                showConfirmationDialog(totalCost)
            } else {
                // Budget is not sufficient, show a message to the user
                showToast("No tienes suficiente Presupuesto para esta compra.")
            }
        }

        // Set the OnRemoveItemClickListener on the adapter
        toPurchaseItemsAdapter.setOnRemoveItemClickListener(this)

        // Update the total cost and check if the list is empty
        updateTotalCost()
    }

    private fun moveItemsToShoppingListDialog() {
        val dialog = AlertDialog.Builder(this) // Use 'this' for the current activity context
        dialog.setTitle("Agregar Productos a una lista de Compras")

        val shoppingLists = ShoppingListData.getLists()
        val shoppingListNames = shoppingLists.map { it.name }.toMutableList()

        // Add an option to create a new list
        shoppingListNames.add("Crear una Lista")

        val itemsArray = shoppingListNames.toTypedArray()
        dialog.setItems(itemsArray) { _, which ->
            if (which < shoppingLists.size) {
                // User selected an existing shopping list
                val selectedShoppingList = shoppingLists[which]
                val itemsToMove = ToPurchaseList.getItems().toMutableList()

                // Add selected items to the shopping list
                selectedShoppingList.items.addAll(itemsToMove)

            } else {
                // User chose to create a new shopping list
                createNewShoppingListAndMoveItems()
            }
        }

        dialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createNewShoppingListAndMoveItems() {
        val inputEditText = EditText(this)
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Crear una Lista de Compras")

        dialog.setView(inputEditText)
        dialog.setPositiveButton("Crear") { dialog, _ ->
            val listName = inputEditText.text.toString()

            if (listName.isNotEmpty()) {
                val newList = ShoppingList(ShoppingListData.generateUniqueId(), listName, mutableListOf())
                ShoppingListData.addList(newList)
                val itemsToMove = ToPurchaseList.getItems().toMutableList()
                newList.items.addAll(itemsToMove)
                ToPurchaseList.clearItems()
            }

            dialog.dismiss()
        }

        dialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onItemRemoved(item: StoreItem) {
        ToPurchaseList.removeItem(item) // Remove the item from the To Purchase List
        updateTotalCost() // Update the total cost after removal
        toPurchaseItemsAdapter.notifyDataSetChanged()
    }

    private fun calculateTotalCost(): Int {
        var totalCost = 0
        for (item in toPurchaseItems) {
            totalCost += item.calculateTotalPrice()
        }
        return totalCost
    }

    private fun showConfirmationDialog(totalCost: Int) {
        // Deduct the total cost from the budget
        val sharedPref = getSharedPreferences("MyBudget", MODE_PRIVATE)
        val budgetValue = sharedPref.getInt("budget", 0)

        val builder = AlertDialog.Builder(this)
        val budgetAfter = budgetValue - totalCost
        builder.setTitle("Confirmar Compra")
        builder.setMessage("Costo Total: $$totalCost\nPresupuesto Post-Compra: $$budgetAfter\n¿Confirmar compra?")

        builder.setPositiveButton("Confirmar") { _, _ ->
            // Deduct the total cost from the budget
            val newBudget = budgetValue - totalCost

            with(sharedPref.edit()) {
                putInt("budget", newBudget)
                apply()
            }

            addToInventory()

            // Remove all items from the To Purchase list
            ToPurchaseList.clearItems()

            // Show a dialog to indicate the budget change
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Compra completada")
            dialogBuilder.setMessage("Costo Total: $$totalCost\nPresupuesto actual: $$newBudget")
            dialogBuilder.setPositiveButton("Aceptar") { _, _ ->
                // User clicked "Aceptar"
                // Navigate to the main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }

        builder.setNegativeButton("Cancelar") { _, _ ->
            // User canceled the purchase
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Helper method to show a short-duration toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun addToInventory() {

        for (item in toPurchaseItems) {
            // Check if the item already exists in the inventory
            val existingItem = InventoryListData.getItems().find { it.itemId == item.itemId }

            if (existingItem != null) {
                // Item already exists in inventory, update its quantity and total price
                existingItem.amountPurchased += item.amountPurchased
                existingItem.totalPrice += item.calculateTotalPrice()
            } else {
                // Item doesn't exist in inventory, add it
                item.totalPrice = item.calculateTotalPrice()
                InventoryListData.addItem(item)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalCost() {

        // Calculate and update the total cost
        var totalCost = calculateTotalCost()

        val totalCostText = findViewById<TextView>(R.id.totalCostText)
        totalCostText.text = "Costo Total: $$totalCost"

        val noItemsMessage = findViewById<TextView>(R.id.noItemsMessage)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val moveToShoppingListButton = findViewById<Button>(R.id.moveToShoppingListButton)

        if (toPurchaseItems.isEmpty()) {

            // Show the "No items in the list" message and disable buttons
            noItemsMessage.visibility = View.VISIBLE
            confirmButton.isEnabled = false
            moveToShoppingListButton.isEnabled = false
        } else {
            noItemsMessage.visibility = View.GONE
            confirmButton.isEnabled = true
            moveToShoppingListButton.isEnabled = true
        }
    }
}