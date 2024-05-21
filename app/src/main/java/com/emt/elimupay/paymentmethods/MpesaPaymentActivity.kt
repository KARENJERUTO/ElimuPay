package com.emt.elimupay.paymentmethods

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emt.elimupay.R
import com.emt.elimupay.api.ApiService
import com.emt.elimupay.di.App2Module
import com.emt.elimupay.models.MpesaResponse
import com.emt.elimupay.paymentconfirmation.MpesaConfirmation
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MpesaPaymentActivity : AppCompatActivity() {

    private lateinit var editTextStudentUniqueID: TextInputEditText
    private lateinit var editTextAmount: TextInputEditText
    private lateinit var editTextPhoneNumber: TextInputEditText

    private val apiService: ApiService by lazy {
        App2Module.apiService
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mpesa_payment2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MpesaPaymentActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextStudentUniqueID = findViewById(R.id.editTextStudentUniqueID)
        editTextAmount = findViewById(R.id.editTextAmount)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
    }

    fun showPay(view: View) {
        val studentUniqueID = editTextStudentUniqueID.text.toString().trim()
        val amount = editTextAmount.text.toString().trim()
        val phoneNumber = editTextPhoneNumber.text.toString().trim()

        if (studentUniqueID.isEmpty() || amount.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Error: Some fields are empty. Please fill out all the required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val requestBody = JSONObject().apply {
            put("studentUniqueID", studentUniqueID)
            put("amount", amount)
            put("phoneNumber", phoneNumber)
        }

        apiService.initiateMpesaPayment(requestBody).enqueue(object : Callback<MpesaResponse> {
            override fun onResponse(call: Call<MpesaResponse>, response: Response<MpesaResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { mpesaResponse ->
                        Log.d("MpesaPayment", "Payment initiated successfully: $mpesaResponse")
                        Intent(this@MpesaPaymentActivity, MpesaConfirmation::class.java).apply {
                            putExtra("amount_paid", mpesaResponse.amount_paid)
                            putExtra("payment_date", mpesaResponse.payment_date)
                            putExtra("paymentmode", mpesaResponse.paymentmode)
                            putExtra("reference", mpesaResponse.reference)
                            putExtra("student", mpesaResponse.student)
                        }.also {
                            startActivity(it)
                        }
                    } ?: run {
                        Log.e("MpesaPayment", "Response body is null")
                        Toast.makeText(this@MpesaPaymentActivity, "Payment initiation failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<MpesaResponse>, t: Throwable) {
                Log.e("MpesaPayment", "Network error: ${t.message}")
                Toast.makeText(this@MpesaPaymentActivity, "Network error. Please check your connection and try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleError(response: Response<MpesaResponse>) {
        try {
            val errorMessage = response.errorBody()?.string()
            Log.e("MpesaPayment", "Unsuccessful response: $errorMessage")
            Toast.makeText(this, "Payment initiation failed. Error: $errorMessage", Toast.LENGTH_SHORT).show()
        } catch (ioEx: IOException) {
            Log.e("MpesaPayment", "Error reading error message: ", ioEx)
            Toast.makeText(this, "An error occurred while trying to read the error message.", Toast.LENGTH_SHORT).show()
        }
    }
}
