package com.example.listadecomprasmatalv

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.Adapters.InventoryListAdapter
import com.example.listadecomprasmatalv.DataStore.InventoryListData

class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_list) // Replace with your layout file

        val backButton = findViewById<Button>(R.id.backButton)

        // Initialize RecyclerView and adapter
        val recyclerView = findViewById<RecyclerView>(R.id.inventoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = InventoryListAdapter(InventoryListData.getItems())
        recyclerView.adapter = adapter

        // Check if the inventory is empty and show the message if it is
        val emptyInventoryMessage = findViewById<TextView>(R.id.emptyInventoryMessage)

        if (InventoryListData.getItems().isEmpty()) {
            emptyInventoryMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyInventoryMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        // Back
        backButton.setOnClickListener {
            finish()
        }
    }
}