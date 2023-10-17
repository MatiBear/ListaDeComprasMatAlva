package com.example.listadecomprasmatalv.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.DataStore.ShoppingList
import com.example.listadecomprasmatalv.DataStore.ShoppingListData
import com.example.listadecomprasmatalv.R
import com.example.listadecomprasmatalv.ShoppingListActivity
import com.example.listadecomprasmatalv.ShoppingListDetailsActivity

class ShoppingListAdapter(private val shoppingLists: List<ShoppingList>) :
    RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listNameTextView: TextView = itemView.findViewById(R.id.listNameTextView)
        val listItemCountTextView: TextView = itemView.findViewById(R.id.listItemCount)
        val listTotalCostTextView: TextView = itemView.findViewById(R.id.listTotalCost)
        val detailsButton: Button = itemView.findViewById(R.id.detailsButton)
        val removeListButton: Button = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]

        holder.listNameTextView.text = shoppingList.name
        holder.listItemCountTextView.text = "Productos: ${shoppingList.items.size}"

        // Calculate the total cost based on items and display it
        val totalCost = shoppingList.items.sumOf { it.calculateTotalPrice() }
        holder.listTotalCostTextView.text = "Costo Total: $$totalCost"

        // Set a click listener for the navigateButton
        holder.detailsButton.setOnClickListener {
            // Create an Intent to start the DetailedShoppingListActivity
            val intent = Intent(holder.itemView.context, ShoppingListDetailsActivity::class.java)

            // Pass the shopping list ID or any relevant data to the DetailedShoppingListActivity
            intent.putExtra("shoppingListId", shoppingList.id) // You can change "shoppingListId" to your specific data key

            // Start the new activity
            holder.itemView.context.startActivity(intent)
        }

        holder.removeListButton.setOnClickListener {
            // Remove the list from the data store and update the RecyclerView
            ShoppingListData.removeList(shoppingList)
            shoppingLists.toMutableList().remove(shoppingList)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return shoppingLists.size
    }
}