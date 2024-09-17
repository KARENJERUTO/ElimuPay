package com.emt.elimupay.paymentmethods

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emt.elimupay.R

class PaymentMethods : AppCompatActivity() {

    private lateinit var radioGroupPaymentMethods: RadioGroup
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_methods)

        // Initialize UI elements
        radioGroupPaymentMethods = findViewById(R.id.radioGroupPaymentMethods)
        confirmButton = findViewById(R.id.btnConfirmPayment)

        // Set a click listener on the "Confirm Payment" button
        confirmButton.setOnClickListener {
            val selectedRadioButtonId = radioGroupPaymentMethods.checkedRadioButtonId

            // Check if a radio button is selected
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                handlePaymentMethod(selectedRadioButton.text.toString())
            } else {
                // No payment method selected
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This function handles the selected payment method and starts the corresponding activity
    private fun handlePaymentMethod(paymentMethod: String) {
        when (paymentMethod) {
            "M-Pesa" -> startPaymentActivity(MpesaPayment::class.java)
            "Credit / Debit Card" -> startPaymentActivity(CardPaymentActivity::class.java)
            "eCitizen" -> startPaymentActivity(eCitizenPaymentActivity::class.java)
            "Bank Account" -> startPaymentActivity(BankPaymentActivity::class.java)
            "Lipa Mdogo Mdogo" -> startPaymentActivity(LipaMdogoMdogo::class.java)
            else -> Toast.makeText(this, "Invalid payment method", Toast.LENGTH_SHORT).show()
        }
    }

    // Helper function to start the appropriate activity
    private fun startPaymentActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}
