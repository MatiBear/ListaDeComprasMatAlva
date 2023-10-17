package com.example.listadecomprasmatalv.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.DataClasses.StoreItem
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList
import com.example.listadecomprasmatalv.R

class ToPurchaseAdapter(private val items: List<StoreItem>) :
    RecyclerView.Adapter<ToPurchaseAdapter.ViewHolder>() {

    interface OnRemoveItemClickListener {
        fun onItemRemoved(item: StoreItem)
    }

    private var removeItemClickListener: OnRemoveItemClickListener? = null

    fun setOnRemoveItemClickListener(listener: OnRemoveItemClickListener) {
        removeItemClickListener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemType: TextView = itemView.findViewById(R.id.itemType)
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val pricePerUnit: TextView = itemView.findViewById(R.id.pricePerUnit)
        val currentAmount: TextView = itemView.findViewById(R.id.currentAmount)
        val currentCost: TextView = itemView.findViewById(R.id.currentCost)
        val removeItemButton: Button = itemView.findViewById(R.id.removeItemButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_purchase_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Populate item details
        holder.itemImage.setImageResource(item.itemImageResourceId)
        holder.itemName.text = item.itemName
        holder.itemType.text = item.itemType
        holder.storeName.text = "De: ${item.purchasedFromStore}"
        holder.pricePerUnit.text = "${item.units} ${item.measurement}: $${item.basePrice}"
        holder.currentAmount.text = "Cantidad: ${item.amountPurchased} ${item.measurement}"
        holder.currentCost.text = "Total: $${item.calculateTotalPrice()}"

        // Remove button click
        holder.removeItemButton.setOnClickListener {
            // Remove the item from the To Purchase List and update the view
            ToPurchaseList.removeItem(item)
            removeItemClickListener?.onItemRemoved(item)
            notifyDataSetChanged() // Notify the adapter that the data has changed
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
