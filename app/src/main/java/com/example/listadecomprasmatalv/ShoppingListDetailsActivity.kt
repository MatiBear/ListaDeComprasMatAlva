package com.example.listadecomprasmatalv

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.Adapters.ShoppingItemAdapter
import com.example.listadecomprasmatalv.DataClasses.StoreItem
import com.example.listadecomprasmatalv.DataStore.ShoppingList
import com.example.listadecomprasmatalv.DataStore.ShoppingListData
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList
import com.example.listadecomprasmatalv.R

class ShoppingListDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list_detailed)

        val shoppingListId = intent.getIntExtra("shoppingListId", -1)
        val listNameEditText = findViewById<EditText>(R.id.listNameEditText)
        val itemCountTextView = findViewById<TextView>(R.id.shoppingItemNum)
        val totalCostTextView = findViewById<TextView>(R.id.shoppingTotalCost)
        val purchaseButton = findViewById<Button>(R.id.purchaseButton)
        val backButton = findViewById<Button>(R.id.backButton)

        val itemsRecyclerView = findViewById<RecyclerView>(R.id.itemsRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        itemsRecyclerView.layoutManager = layoutManager

        val shoppingList = ShoppingListData.getLists().find { it.id == shoppingListId }

        shoppingList?.let {
            listNameEditText.setText(it.name)
            itemCountTextView.text = "Productos: ${it.items.size}"
            val totalCost = it.items.sumOf { item -> item.calculateTotalPrice() }
            totalCostTextView.text = "Total Cost: $$totalCost"

            // Create the ShoppingItemAdapter with the list items and ID
            val shoppingListAdapter = ShoppingItemAdapter(it.items, it.id)
            itemsRecyclerView.adapter = shoppingListAdapter
        }

        purchaseButton.setOnClickListener {
            // Check if there are items to purchase
            if (shoppingList?.items?.isNotEmpty() == true) {
                // Move items from the shopping list to the "To Purchase" list
                ToPurchaseList.clearItems()
                shoppingList.items.forEach { item ->
                    ToPurchaseList.addItem(item)
                }

                // Provide a user with feedback (optional)
                showPurchaseSuccessDialog()

                // You can redirect the user to the purchase process or any other action here
            } else {
                // Show a warning dialog if there are no items to purchase
                showNoItemsToPurchaseDialog()
            }
        }

        backButton.setOnClickListener {
            // Handle the button click event to navigate back to the previous activity
            finish()
        }
    }

    private fun showPurchaseSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Articulos agregados")
        builder.setMessage("Los productos se han agregado a la lista de Compra.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    // Function to show a warning dialog when there are no items to purchase
    private fun showNoItemsToPurchaseDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No hay Articulos")
        builder.setMessage("No hay productos para agregar a la lista.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}