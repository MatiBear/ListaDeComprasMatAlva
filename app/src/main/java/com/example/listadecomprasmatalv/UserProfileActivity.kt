package com.example.listadecomprasmatalv

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.listadecomprasmatalv.DataClasses.StoreItem

@Suppress("DEPRECATION")
class UserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile)

        val saveButton: Button = findViewById(R.id.saveBudgetButton)
        val budgetEditText: EditText = findViewById(R.id.budgetEditText)

        val sharedPref: SharedPreferences = getSharedPreferences("MyBudget", Context.MODE_PRIVATE)
        val budgetValue: Int = sharedPref.getInt("budget", 0)

        budgetEditText.setText(budgetValue.toString()) // Set the initial value of the EditText

        saveButton.setOnClickListener {
            val budgetText = budgetEditText.text.toString()
            if (budgetText.isNotEmpty()) {
                val newBudgetValue = budgetText.toIntOrNull()

                if (newBudgetValue != null && newBudgetValue > 0) {
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putInt("budget", newBudgetValue)
                    editor.apply()

                    // Change the button text to 'Saved'
                    saveButton.text = "Guardado"

                    // Delay changing the button text back to normal after a few seconds
                    val handler = Handler()
                    handler.postDelayed({
                        saveButton.text = "Guardar Presupuesto" // Change it back to 'Save'
                    }, 2000) // Delay for 2 seconds (adjust this as needed)

                    // Navigate to other interfaces or activities here
                } else {
                    // Change the button text to 'Saved'
                    saveButton.text = "No es Posible Guardar"

                    // Delay changing the button text back to normal after a few seconds
                    val handler = Handler()
                    handler.postDelayed({
                        saveButton.text = "Guardar Presupuesto" // Change it back to 'Save'
                    }, 2000) // Delay for 2 seconds (adjust this as needed)
                }
            }
        }
    }

    fun openMainMenu(view: View) {
        finish()
    }
}