package com.emt.elimupay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    private lateinit var editTextPaymentDetails: EditText
    private lateinit var buttonPay: Button // Declare Button variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val radioGroupPaymentMethods: RadioGroup = findViewById(R.id.radioGroupPaymentMethods)
        editTextPaymentDetails = findViewById(R.id.editTextPaymentDetails)

        // Initialize buttonPay using findViewById()
        buttonPay = findViewById(R.id.buttonPay)

        radioGroupPaymentMethods.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMpesa -> editTextPaymentDetails.hint = "Enter Mpesa Number"
                R.id.radioButtonCard -> editTextPaymentDetails.hint = "Enter Card Details"
                R.id.radioButtonBank -> editTextPaymentDetails.hint = "Enter Bank Account Number"
                R.id.radioButtonECitizen -> editTextPaymentDetails.hint = "Enter eCitizen Details"
            }
        }

        // Set OnClickListener for buttonPay
        buttonPay.setOnClickListener {
            val paymentDetails = editTextPaymentDetails.text.toString()
            // Process the payment based on the selected method
            if (paymentDetails.isNotEmpty()) {
                // Proceed with payment processing using paymentDetails
            } else {
                // Show an error message or prompt the user to enter payment details
            }
        }

    }
}
