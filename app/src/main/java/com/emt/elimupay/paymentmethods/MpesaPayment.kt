package com.emt.elimupay.paymentmethods

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emt.elimupay.R
import com.emt.elimupay.paymentconfirmation.PaymentSuccessfulActivity
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
    private lateinit var studentUniqueIdInput: TextInputEditText
    private lateinit var amountInput: TextInputEditText
    private lateinit var phoneNumberInput: TextInputEditText
    private lateinit var payButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mpesa_payment)

        studentUniqueIdInput = findViewById(R.id.editTextStudentUniqueID)
        amountInput = findViewById(R.id.editTextAmount)
        phoneNumberInput = findViewById(R.id.editTextPhoneNumber)
        payButton = findViewById(R.id.mpesapay)

        payButton.setOnClickListener {
            showPay()
        }
    }

    private fun showPay() {
        val studentUniqueId = studentUniqueIdInput.text.toString().trim()
        val amount = amountInput.text.toString().trim()
        val phoneNumber = phoneNumberInput.text.toString().trim()

        if (studentUniqueId.isEmpty() || amount.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val amountValue = amount.toDouble()
            if (amountValue <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentRequest = PaymentRequest(studentUniqueId, amount, phoneNumber)
        processPayment(paymentRequest)
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
                val url = URL("http://192.168.91.228:8000/api/v1/payfee/fee-collection/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val requestBody = JSONObject().apply {
                    put("student", request.studentUniqueId)
                    put("phone_number", request.phoneNumber)
                    put("amount_paid", request.amount)
                }.toString()



                OutputStreamWriter(connection.outputStream).use {
                    it.write(requestBody)
                    it.flush()
                }

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    val jsonResponse = JSONObject(response)

                    val student = jsonResponse.getString("student")
                    val phoneNumber = jsonResponse.getString("phone_number")
                    val amountPaid = jsonResponse.getString("amount_paid")

                    PaymentResponse(true, student, phoneNumber, amountPaid, null)
                } else {
                    PaymentResponse(false, "", "", "", "Payment failed with status code: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                PaymentResponse(false, "", "", "", "Payment failed due to an error")
            }
        }
    }

    private fun handlePaymentResponse(response: PaymentResponse) {
        if (response.success) {
            val paymentDetails = "Payment successful: ${response.amountPaid} paid by ${response.student}. Phone number: ${response.phoneNumber}"
            val intent = Intent(this, PaymentSuccessfulActivity::class.java).apply {
                putExtra("paymentDetails", paymentDetails)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
        }
    }
}

data class PaymentRequest(val studentUniqueId: String, val amount: String, val phoneNumber: String)
data class PaymentResponse(val success: Boolean, val student: String, val phoneNumber: String, val amountPaid: String, val message: String?)
