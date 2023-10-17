package com.example.listadecomprasmatalv

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecomprasmatalv.Adapters.ItemListAdapter
import com.example.listadecomprasmatalv.DataStore.StoreData
import com.example.listadecomprasmatalv.DataStore.ToPurchaseList

class StoreDetailsActivity : AppCompatActivity(), ItemListAdapter.PurchaseListListener {

    private var isToPurchaseListNotEmpty = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_details_menu)

        val storeId = intent.getIntExtra("storeId", -1)
        val selectedStore = StoreData.stores.find { it.id == storeId }

        if (selectedStore != null) {

            val storeDetailImage = findViewById<ImageView>(R.id.storeDetailImage)
            val storeDetailName = findViewById<TextView>(R.id.storeDetailName)
            val storeDetailOpeningClosingTime = findViewById<TextView>(R.id.storeDetailOpeningClosingTime)
            val storeDetailType = findViewById<TextView>(R.id.storeDetailType)
            val storeDetailAddress = findViewById<TextView>(R.id.storeDetailAddress)
            val storeDetailPaymentType = findViewById<TextView>(R.id.storeDetailPaymentType)
            val storeDetailWebsite = findViewById<TextView>(R.id.storeDetailWebsite)
            val storeDetailCustomerSupportEmail = findViewById<TextView>(R.id.storeDetailCustomerSupportEmail)

            storeDetailImage.setImageResource(selectedStore.imageResourceId)
            storeDetailName.text = selectedStore.name
            storeDetailOpeningClosingTime.text = "Horario: ${selectedStore.openingClosingTime}"
            storeDetailType.text = selectedStore.storeType
            storeDetailAddress.text = selectedStore.address
            storeDetailPaymentType.text = "Pagos: ${selectedStore.paymentType}"
            storeDetailWebsite.text = "Website: ${selectedStore.website}"
            storeDetailCustomerSupportEmail.text = "Email de Soporte: ${selectedStore.customerSupportEmail}"

            // Check the current purchase list size and update the purchase button text
            updatePurchaseButtonAmount()

            // Assuming you have a RecyclerView in your layout with the id 'itemRecyclerView'
            val recyclerView: RecyclerView = findViewById(R.id.itemRecycleView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val items = selectedStore.storeItems

            // Create an adapter for the RecyclerView
            val adapter = ItemListAdapter(this, items)
            adapter.purchaseListListener = this

            // Attach the adapter to the RecyclerView
            recyclerView.adapter = adapter
        }

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
        // ----------------------------------------------------------------------------------------
    }

    override fun onResume() {
        super.onResume()
        updatePurchaseButtonAmount()
    }

    override fun onPurchaseListChanged(itemCount: Int) {
        updatePurchaseButtonAmount()
    }

    private fun updatePurchaseButtonAmount() {
        val purchaseButton = findViewById<Button>(R.id.purchaseButton)
        val purchaseListSize = ToPurchaseList.getItems().size
        if (purchaseListSize > 0) {
            purchaseButton.text = "Comprar ($purchaseListSize)"
        } else {
            purchaseButton.text = "Comprar"
        }
    }
}
