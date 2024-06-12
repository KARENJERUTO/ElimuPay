package com.emt.elimupay.paymentmethods

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emt.elimupay.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class BankPaymentActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bank_payment)

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.BankPaymentActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the AutoCompleteTextView with account options
        val accounts = arrayOf("Account 1", "Account 2", "Account 3")
        val accountSelectionAutoComplete: MaterialAutoCompleteTextView = findViewById(R.id.accountSelectionAutoComplete)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, accounts)
        accountSelectionAutoComplete.setAdapter(adapter)

        // Set up Pay button click listener
        findViewById<MaterialButton>(R.id.payButton).setOnClickListener {
            showPaymentDialog()
        }
    }

    private fun showPaymentDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Payment Type")

        val paymentTypes = arrayOf("Full Payment", "Partial Payment")
        builder.setItems(paymentTypes) { dialog, which ->
            when (which) {
                0 -> {
                    // Full Payment
                    confirmPayment("full")
                }
                1 -> {
                    // Partial Payment
                    showPartialPaymentDialog()
                }
            }
        }

        builder.show()
    }

    private fun showPartialPaymentDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Partial Payment Amount")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)

        builder.setPositiveButton("Confirm") { dialog, _ ->
            val amount = input.text.toString()
            if (amount.isNotEmpty() && amount.toDoubleOrNull() != null) {
                confirmPayment("partial", amount.toDouble())
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun confirmPayment(type: String, amount: Double = 0.0) {
        val message = if (type == "full") {
            "Confirm Full Payment"
        } else {
            "Confirm Partial Payment of $amount"
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Payment Confirmation")
        builder.setMessage(message)

        builder.setPositiveButton("Confirm") { dialog, _ ->
            // Handle the payment confirmation here
            Toast.makeText(this, "Payment confirmed", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
