package com.emt.elimupay

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emt.elimupay.models.ResetPasswordRequest
import com.emt.elimupay.models.ResetPasswordResponse
import com.emt.elimupay.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResetPassword : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEditText: EditText = findViewById(R.id.username)
        val resetButton: Button = findViewById(R.id.resetButton)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            if (email.isNotEmpty()) {
                performResetPassword(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performResetPassword(email: String) {
        val resetPasswordRequest = ResetPasswordRequest(email)
        RetrofitClient.apiService.resetPassword(resetPasswordRequest)
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(
                    call: Call<ResetPasswordResponse>,
                    response: Response<ResetPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        val resetPasswordResponse = response.body()
                        if (resetPasswordResponse != null) {
                            Toast.makeText(this@ResetPassword, resetPasswordResponse.message, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@ResetPassword, "Error: Response body is null", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@ResetPassword, "Error: ${errorBody ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    if (t is IOException) {
                        Toast.makeText(this@ResetPassword, "Network Failure: ${t.message}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@ResetPassword, "Conversion Issue: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}
