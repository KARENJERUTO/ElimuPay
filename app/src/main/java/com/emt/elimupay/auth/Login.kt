package com.emt.elimupay.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.emt.elimupay.mainapp.MainActivity
import com.emt.elimupay.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var forgotPasswordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        // Set click listener for forgot password
        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }
    }

    // Handle login process
    fun handleLogin(view: View) {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) usernameEditText.error = "Please enter your username"
            if (password.isEmpty()) passwordEditText.error = "Please enter your password"
            return
        }

        progressBar.visibility = View.VISIBLE

        val url = "http://192.168.88.86:8007/api/v1/parents/parents/login/"
        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    if (responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val status = jsonResponse.getInt("status")
                            val message = jsonResponse.getString("message")

                            if (status == 200) {
                                // Save login info in SharedPreferences
                                saveLoginInfo(username)

                                Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Login failed: $message", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this@LoginActivity, "Login failed: Error parsing response", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: Empty response", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    // Save login info in SharedPreferences
    private fun saveLoginInfo(username: String) {
        val sharedPref = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.putBoolean("isLoggedIn", true) // Mark the user as logged in
        editor.apply()
    }
}
