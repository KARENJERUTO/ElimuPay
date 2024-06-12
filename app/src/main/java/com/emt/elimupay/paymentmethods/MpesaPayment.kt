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
            showPaymentDialog()
        }
    }

    private fun showPaymentDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Payment Type")
        val paymentTypes = arrayOf("Full Payment", "Partial Payment")
        builder.setItems(paymentTypes) { _, which ->
            when (which) {
                0 -> processPayment(PaymentRequest(phoneNumberInput.text.toString().trim()))
                1 -> showPartialPaymentDialog()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun showPartialPaymentDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Partial Amount")
        val input = TextInputEditText(this)
        builder.setView(input)
        builder.setPositiveButton("Confirm") { dialog, _ ->
            val amount = input.text.toString().trim()
            if (amount.isNotEmpty()) {
                processPayment(PaymentRequest(phoneNumberInput.text.toString().trim(), amount))
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

    private fun processPayment(request: PaymentRequest) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = makePaymentAsync(request)
            handlePaymentResponse(response)
        }
    }

    private suspend fun makePaymentAsync(request: PaymentRequest): PaymentResponse {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.1.163:8000/api/v1/payfee/fee-collection/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val requestBody = JSONObject().apply {
                    put("phone_number", request.phoneNumber)
                    put("amount", request.amount) // Add amount for partial payment
                }.toString()

                OutputStreamWriter(connection.outputStream).use {
                    it.write(requestBody)
                    it.flush()
                }

                if (connection.responseCode == HttpURLConnection.HTTP_CREATED) { // 201 Created
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonResponse = JSONObject(response)

                    val phoneNumber = jsonResponse.getString("phone_number")

                    PaymentResponse(true, phoneNumber, null)
                } else {
                    PaymentResponse(false, "", "Payment failed with status code: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                PaymentResponse(false, "", "Payment failed due to an error: ${e.message}")
            }
        }
    }

    private fun handlePaymentResponse(response: PaymentResponse) {
        if (response.success) {
            val paymentDetails = "Payment successful for phone number: ${response.phoneNumber}"
            Toast.makeText(this, paymentDetails, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }
}

data class PaymentRequest(val phoneNumber: String, val amount: String? = null) // Add amount field
data class PaymentResponse(val success: Boolean, val phoneNumber: String, val message: String?)
