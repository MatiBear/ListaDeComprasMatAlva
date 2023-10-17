package com.example.listadecomprasmatalv.Adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.DataClasses.StoreItem
import com.example.listadecomprasmatalv.DataStore.ShoppingListData
import com.example.listadecomprasmatalv.R

class ShoppingItemAdapter(private var items: List<StoreItem>, private val shoppingListId: Int) : RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.shoppingItemImage)
        val itemName: TextView = itemView.findViewById(R.id.shoppingItemName)
        val itemType: TextView = itemView.findViewById(R.id.shoppingItemType)
        val basePrice: TextView = itemView.findViewById(R.id.shoppingPrice)
        val storePurchased: TextView = itemView.findViewById(R.id.shoppingItemType2)
        val currentAmount: EditText = itemView.findViewById(R.id.shoppingCurrentAmount)
        val totalCost: TextView = itemView.findViewById(R.id.shoppingTotalCost)
        val removeButton: Button = itemView.findViewById(R.id.removeItemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_detailed_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemImage.setImageResource(item.itemImageResourceId)
        holder.itemName.text = item.itemName
        holder.itemType.text = item.itemType
        holder.basePrice.text = "Precio por ${item.measurement}: $${item.basePrice}"
        holder.storePurchased.text = item.purchasedFromStore

        // Set the amount from the item and update it in the text field
        holder.currentAmount.setText(item.amountPurchased.toString())

        // Update the total cost based on the amount and display it
        val totalCost = item.calculateTotalPrice()
        holder.totalCost.text = "Costo: $$totalCost"

        holder.removeButton.setOnClickListener {
            // Handle item removal here
            ShoppingListData.removeItemFromList(shoppingListId, item)
            notifyDataSetChanged()
        }

        holder.currentAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().toDoubleOrNull() ?: 0.0
                item.amountPurchased = input
                val newTotalCost = item.calculateTotalPrice()
                holder.totalCost.text = "Costo: $$newTotalCost"
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().toDoubleOrNull() ?: 0.0
                if (input <= 0) {
                    // Remove the item if the input is 0 or negative
                    ShoppingListData.removeItemFromList(shoppingListId, item)
                    notifyDataSetChanged()
                }
            }
        })
    }

    fun setItems(items: List<StoreItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}