package com.example.listadecomprasmatalv.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.listadecomprasmatalv.DataClasses.Store
import com.example.listadecomprasmatalv.R

class StoreListAdapter(
    private val context: Context,
    private val stores: List<Store>,
    private val onStoreItemClick: (store: Store) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int {
        return stores.size
    }
    override fun getItem(position: Int): Any {
        return stores[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.store_list_item, null)

        val store = stores[position]

        val storeImage = view.findViewById<ImageView>(R.id.storeImage)
        val storeName = view.findViewById<TextView>(R.id.storeName)
        val openingClosingTime = view.findViewById<TextView>(R.id.openingClosingTime)
        val storeType = view.findViewById<TextView>(R.id.storeType)
        val storeAddress = view.findViewById<TextView>(R.id.storeAddress)
        val viewDetailsButton = view.findViewById<Button>(R.id.viewDetailsButton)

        storeImage.setImageResource(store.imageResourceId)
        storeName.text = store.name
        openingClosingTime.text = "Horario: ${store.openingClosingTime}"
        storeType.text = "${store.storeType}"
        storeAddress.text = "${store.address}"

        viewDetailsButton.setOnClickListener {
            onStoreItemClick(store) // Trigger the callback when the button is clicked
        }

        return view
    }
}