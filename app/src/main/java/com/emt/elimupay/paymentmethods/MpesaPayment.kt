package com.emt.elimupay.paymentmethods

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emt.elimupay.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MpesaPayment : AppCompatActivity() {
    private lateinit var phoneNumberInput: TextInputEditText
    private lateinit var payButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa_payment)

        phoneNumberInput = findViewById(R.id.editPhoneNumber)
        payButton = findViewById(R.id.mpesapay)

        payButton.setOnClickListener {
            val phoneNumber = phoneNumberInput.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                showPaymentDialog(phoneNumber)
                phoneNumberInput.setText("") // Clear input after initiating dialog
            } else {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPaymentDialog(phoneNumber: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Payment Type")
        val paymentTypes = arrayOf("Full Payment", "Partial Payment")
        builder.setItems(paymentTypes) { _, which ->
            when (which) {
                0 -> processPayment(PaymentRequest(phoneNumber)) // Full payment
                1 -> showPartialPaymentDialog(phoneNumber)      // Partial payment
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showPartialPaymentDialog(phoneNumber: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Partial Amount")
        val input = TextInputEditText(this)
        builder.setView(input)
        builder.setPositiveButton("Confirm") { dialog, _ ->
            val amount = input.text.toString().trim()
            if (amount.isNotEmpty()) {
                processPayment(PaymentRequest(phoneNumber, amount))
            } else {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Step 1: Initiate Payment Process
    private fun processPayment(request: PaymentRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = initiateMpesaPayment(request)
            if (response.success) {
                Toast.makeText(this@MpesaPayment, "Payment request sent. Please confirm on your phone.", Toast.LENGTH_LONG).show()
                pollPaymentStatus(request) // Start polling the backend to check payment status
            } else {
                Toast.makeText(this@MpesaPayment, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Step 2: Call your backend to initiate payment
    private suspend fun initiateMpesaPayment(request: PaymentRequest): PaymentResponse {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("http://172.16.9.74:8007/api/v1/mpesa/lipa_na_mpesa/254704754722/1") // Update with your backend API
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val requestBody = JSONObject().apply {
                    put("phone_number", request.phoneNumber)
                    put("amount", request.amount ?: "1000") // Default amount if not provided
                }.toString()

                OutputStreamWriter(connection.outputStream).use {
                    it.write(requestBody)
                    it.flush()
                }

                if (connection.responseCode == HttpURLConnection.HTTP_CREATED) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonResponse = JSONObject(response)
                    PaymentResponse(true, jsonResponse.getString("phone_number"), null)
                } else {
                    PaymentResponse(false, "", "Payment request failed with status code: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                PaymentResponse(false, "", "Payment failed due to an error: ${e.message}")
            }
        }
    }

    // Step 3: Poll payment status (or handle via a backend push notification system)
    private fun pollPaymentStatus(request: PaymentRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            var isPaymentComplete = false
            while (!isPaymentComplete) {
                val paymentStatus = checkPaymentStatus(request)
                if (paymentStatus.success) {
                    Toast.makeText(this@MpesaPayment, "Payment Successful!", Toast.LENGTH_LONG).show()
                    isPaymentComplete = true
                } else if (paymentStatus.message != null) {
                    Toast.makeText(this@MpesaPayment, paymentStatus.message, Toast.LENGTH_SHORT).show()
                    isPaymentComplete = true // Stop polling after failure
                }

            }
        }
    }

    // Check the payment status from your backend
    private suspend fun checkPaymentStatus(request: PaymentRequest): PaymentResponse {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.1.105:8003/api/v1/mpesa/payment_status/${request.phoneNumber}") // Update with your status check endpoint
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonResponse = JSONObject(response)
                    PaymentResponse(
                        success = jsonResponse.getBoolean("success"),
                        phoneNumber = request.phoneNumber,
                        message = if (jsonResponse.getBoolean("success")) null else "Payment failed"
                    )
                } else {
                    PaymentResponse(false, request.phoneNumber, "Failed to retrieve payment status")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                PaymentResponse(false, request.phoneNumber, "Error retrieving payment status: ${e.message}")
            }
        }
    }

}

data class PaymentRequest(val phoneNumber: String, val amount: String? = null)
data class PaymentResponse(val success: Boolean, val phoneNumber: String, val message: String?)
