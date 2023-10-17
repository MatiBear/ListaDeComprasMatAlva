package com.example.listadecomprasmatalv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecomprasmatalv.Adapters.StoreListAdapter
import com.example.listadecomprasmatalv.DataStore.StoreData
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList

class StoreListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_list_menu)

        val storeListView: ListView = findViewById(R.id.storeListView)
        val storeAdapter = StoreListAdapter(this, StoreData.stores) { store ->
            openStoreDetailsActivity(store.id)
        }
        storeListView.adapter = storeAdapter

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

    private fun openStoreDetailsActivity(storeId: Int) {
        val intent = Intent(this, StoreDetailsActivity::class.java)
        intent.putExtra("storeId", storeId)
        startActivity(intent)
    }
}
