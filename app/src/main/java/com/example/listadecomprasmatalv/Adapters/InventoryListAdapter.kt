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
import com.example.listadecomprasmatalv.DataStore.InventoryListData
import com.example.listadecomprasmatalv.R

class InventoryListAdapter(private val items: List<StoreItem>) : RecyclerView.Adapter<InventoryListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.inventoryItemImage)
        val itemName: TextView = itemView.findViewById(R.id.inventoryItemName)
        val itemType: TextView = itemView.findViewById(R.id.inventoryItemType)
        val measurement: TextView = itemView.findViewById(R.id.inventoryMeasurement)
        val moneySpent: TextView = itemView.findViewById(R.id.inventoryMoneySpent)
        val currentAmount: EditText = itemView.findViewById(R.id.inventoryCurrentAmount)
        val removeButton: Button = itemView.findViewById(R.id.removeInventoryItemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemImage.setImageResource(item.itemImageResourceId)
        holder.itemName.text = item.itemName
        holder.itemType.text = item.itemType
        holder.measurement.text = "Cantidad (${item.measurement}):"
        holder.moneySpent.text = "Gastos: $${item.totalPrice}"

        // Set the amount and add a TextWatcher
        holder.currentAmount.setText(item.amountPurchased.toString())
        holder.currentAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().toDoubleOrNull() ?: 0.0
                item.amountPurchased = input
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().toDoubleOrNull() ?: 0.0
                if (input <= 0) {
                    // Reset the input to 0
                    holder.currentAmount.setText("0")
                }
            }
        })

        holder.removeButton.setOnClickListener {
            // Handle item removal here
            InventoryListData.removeItem(item)
            notifyDataSetChanged()
            itemRemovedListener?.onItemRemoved(item) // Notify the listener
        }
    }

    // Removal Listener
    interface OnItemRemovedListener {
        fun onItemRemoved(item: StoreItem)
    }

    private var itemRemovedListener: OnItemRemovedListener? = null

    fun setOnItemRemovedListener(listener: OnItemRemovedListener) {
        itemRemovedListener = listener
    }


    override fun getItemCount(): Int {
        return items.size
    }
}