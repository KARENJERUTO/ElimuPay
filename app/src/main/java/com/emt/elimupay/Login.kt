package com.emt.elimupay

import LoginRequest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emt.elimupay.di.AppModule
import com.emt.elimupay.models.login
import com.emt.elimupay.retrofit.ApiService
import com.emt.elimupay.retrofit.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Retrofit

        apiService = RetrofitClient.apiService

        // Example login button click listener
        val loginButton = findViewById<View>(R.id.login)
        loginButton.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.username).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.password).text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }
    }





    private fun login(email: String, password: String, cont: Context) {
       // val call = apiService.login(LoginRequest(email, password))
        val retrofit = AppModule().apiCalls("")
        val loginCall = retrofit.login(LoginRequest(email, password))

        loginCall.enqueue(object: Callback<login>{
            override fun onResponse(call: Call<login>, response: Response<login>) {
                if(response.isSuccessful){
                    if(response.body() != null){
                        Toast.makeText(cont, response.body().toString(), Toast.LENGTH_LONG).show()
                        val intent = Intent(cont, MainActivity::class.java)
                        cont.startActivity(intent)
                    }else{
                        Toast.makeText(cont, "Please enter email and password", Toast.LENGTH_LONG).show()

                    }
                }else{
                    Toast.makeText(cont, "response not successful", Toast.LENGTH_LONG).show()

                }
            }
            override fun onFailure(call: Call<login>, t: Throwable) {
                Toast.makeText(cont,t.message, Toast.LENGTH_LONG).show()

            }

        })

    }

    fun onForgotPasswordClicked(view: View) {
        val intent = Intent(this, ResetPassword::class.java)
        startActivity(intent)
    }
}

data class LoginResponse(
    val success: Boolean,
    val message: String
)