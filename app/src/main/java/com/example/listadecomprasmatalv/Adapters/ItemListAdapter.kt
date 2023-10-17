package com.example.listadecomprasmatalv.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.DataClasses.StoreItem
import com.example.listadecomprasmatalv.DataStore.ShoppingList
import com.example.listadecomprasmatalv.DataStore.ShoppingListData
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList
import com.example.listadecomprasmatalv.R

@Suppress("DEPRECATION")
class ItemListAdapter(private val context: Context, private val items: List<StoreItem>) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemType: TextView = itemView.findViewById(R.id.itemType)
        val unitPrice: TextView = itemView.findViewById(R.id.unitPrice)
        val amountInput: EditText = itemView.findViewById(R.id.amountInput)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val addToPurchaseButton: Button = itemView.findViewById(R.id.addToPurchaseButton)
        val saveIntoListButton: Button = itemView.findViewById(R.id.saveIntoListButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Populate item details
        holder.itemImage.setImageResource(item.itemImageResourceId)
        holder.itemName.text = item.itemName
        holder.itemType.text = item.itemType
        holder.unitPrice.text = "${item.units} ${item.measurement}: $${item.basePrice}"

        // Check if the item is in the To Purchase list
        val existingItem = ToPurchaseList.getItems().find { it.itemId == item.itemId && it.purchasedFromStore == item.purchasedFromStore }

        if (existingItem != null) {
            // If the item is in the To Purchase list, display the purchased amount
            holder.amountInput.setText(existingItem.amountPurchased.toString())
            holder.totalPrice.text = "Total: $${item.calculateTotalPrice()}"
        } else {
            // If the item is not in the To Purchase list, set the amount input as 0
            holder.amountInput.setText("0")
        }

        // Set a text change listener for amount input
        holder.amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                // Handle text changes and calculate total price
                val input = s.toString().toDoubleOrNull() ?: 0.0
                if (input < 0) {
                    holder.amountInput.setText("0")
                }

                item.amountPurchased = input
                holder.totalPrice.text = "Total: $${item.calculateTotalPrice()}"
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // PURCHASING BUTTON
        // Set a click listener for addToPurchaseButton
        holder.addToPurchaseButton.setOnClickListener {
            val input = holder.amountInput.text.toString().toDoubleOrNull() ?: 0.0

            val existingItem = ToPurchaseList.getItems().find { it.itemId == item.itemId
                    && it.purchasedFromStore == item.purchasedFromStore }

            if (item.amountPurchased > 0) {
                if (existingItem != null) {
                    // Update the existing item's amount
                    existingItem.amountPurchased = item.amountPurchased
                    holder.addToPurchaseButton.text = "¡Actualizado!" // Temporarily change text to "Updated!"
                    // Notify the activity of the change
                    purchaseListListener?.onPurchaseListChanged(ToPurchaseList.getItems().size)
                } else {
                    // Add the new item to the Product List
                    ToPurchaseList.addItem(item)
                    holder.addToPurchaseButton.text = "¡Agregado!" // Temporarily change text to "Added!"
                    // Notify the activity of the change
                    purchaseListListener?.onPurchaseListChanged(ToPurchaseList.getItems().size)
                }
            } else {
                // If the new amount is 0 or less, remove the existing item (if found)
                existingItem?.let {
                    ToPurchaseList.removeItem(it)
                    holder.addToPurchaseButton.text = "¡Removido!" // Temporarily change text to "Removed!"
                    // Notify the activity of the change
                    purchaseListListener?.onPurchaseListChanged(ToPurchaseList.getItems().size)
                }
            }

            // Use a Handler to revert the text back to the original text after 3 seconds
            Handler().postDelayed({
                holder.addToPurchaseButton.text = "Comprar" // Replace with the original text
            }, 3000) // Change back after 3 seconds (adjust the duration as needed)
        }

        // Set a click listener for saveIntoListButton
        holder.saveIntoListButton.setOnClickListener {
            showListSelectionDialog(item)
        }
    }

    private fun showListSelectionDialog(item: StoreItem) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Agregar Producto a Lista")

        val lists = ShoppingListData.getLists()
        val listNames = lists.map { it.name }.toMutableList()

        // Add an option to create a new list
        listNames.add("Crear una Lista")

        val itemsArray = listNames.toTypedArray()
        dialog.setItems(itemsArray) { _, which ->
            if (which < lists.size) {
                // User selected an existing list
                val selectedList = lists[which]
                ShoppingListData.addItemToList(selectedList.id, item)
            } else {
                // User chose to create a new list
                createNewListAndAddItem(item)
            }
        }

        dialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun createNewListAndAddItem(item: StoreItem) {
        val inputEditText = EditText(context)
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Crear una Lista")

        dialog.setView(inputEditText)
        dialog.setPositiveButton("Crear") { dialog, _ ->
            val listName = inputEditText.text.toString()

            if (listName.isNotEmpty()) {
                val newList = ShoppingList(ShoppingListData.generateUniqueId(), listName, mutableListOf())
                ShoppingListData.addList(newList)
                ShoppingListData.addItemToList(newList.id, item)
            }

            dialog.dismiss()
        }

        dialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        dialog.show()
    }


    var purchaseListListener: PurchaseListListener? = null
    interface PurchaseListListener {
        fun onPurchaseListChanged(itemCount: Int)
    }

    override fun getItemCount(): Int {
        return items.size // Get the item count from the global list
    }
}



/* THIS CODE WORKS AT THIS CURRENT ITERATION - COPY BACK IF IT FAILS
* package com.example.listadecomprasmatalv.Adapters

import android.annotation.SuppressLint
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
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList
import com.example.listadecomprasmatalv.R

class ItemListAdapter(private val items: List<StoreItem>) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemType: TextView = itemView.findViewById(R.id.itemType)
        val unitPrice: TextView = itemView.findViewById(R.id.unitPrice)
        val amountInput: EditText = itemView.findViewById(R.id.amountInput)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
        val addToPurchaseButton: Button = itemView.findViewById(R.id.addToPurchaseButton)
        val saveForLaterButton: Button = itemView.findViewById(R.id.saveForLaterButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Populate item details
        holder.itemImage.setImageResource(item.itemImageResourceId)
        holder.itemName.text = item.itemName
        holder.itemType.text = item.itemType
        holder.unitPrice.text = "${item.units} ${item.measurement}: $${item.basePrice}"

        // Set a text change listener for amount input
        holder.amountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Handle text changes and calculate total price
                val input = s.toString().toDoubleOrNull() ?: 0.0
                if (input < 0) {
                    holder.amountInput.setText("0")
                }
                item.amountPurchased = input
                holder.totalPrice.text = "Total: $${item.calculateTotalPrice()}"
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun getItemCount(): Int {
        return items.size // Get the item count from the global list
    }
}
* */