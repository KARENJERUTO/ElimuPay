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
import com.emt.elimupay.models.MpesaResponse
import com.emt.elimupay.paymentconfirmation.MpesaConfirmation
import com.emt.elimupay.retrofit.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MpesaPaymentActivity : AppCompatActivity() {

    private lateinit var editTextStudentUniqueID: TextInputEditText
    private lateinit var editTextAmount: TextInputEditText
    private lateinit var editTextPhoneNumber: TextInputEditText

    private val apiService: ApiService = RetrofitClient.apiService

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

        // Check if any of the fields are empty
        if (studentUniqueID.isEmpty() || amount.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Error: Some fields are empty. Please fill out all the required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Log input data for debugging
        Log.d("MpesaPayment", "Initiating payment with StudentUniqueID: $studentUniqueID, Amount: $amount, PhoneNumber: $phoneNumber")

        // Make API call to initiate payment
        apiService.initiateMpesaPayment(studentUniqueID, amount, phoneNumber).enqueue(object : Callback<MpesaResponse> {
            override fun onResponse(call: Call<MpesaResponse>, response: Response<MpesaResponse>) {
                if (response.isSuccessful) {
                    // Payment initiated successfully, navigate to confirmation screen
                    val mpesaResponse = response.body()
                    Log.d("MpesaPayment", "Payment initiated successfully: $mpesaResponse")

                    // Pass data to confirmation activity
                    val intent = Intent(this@MpesaPaymentActivity, MpesaConfirmation::class.java).apply {
                        putExtra("amount_paid", mpesaResponse?.amount_paid)
                        putExtra("payment_date", mpesaResponse?.payment_date)
                        putExtra("paymentmode", mpesaResponse?.paymentmode)
                        putExtra("reference", mpesaResponse?.reference)
                        putExtra("student", mpesaResponse?.student)
                    }
                    startActivity(intent)
                } else {
                    Log.e("MpesaPayment", "Unsuccessful response: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MpesaPaymentActivity, "Payment initiation failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MpesaResponse>, t: Throwable) {
                Log.e("MpesaPayment", "Network error: ${t.message}")
                Toast.makeText(this@MpesaPaymentActivity, "Network error. Please check your connection and try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
