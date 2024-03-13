package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onForgotPasswordClicked(view: View) {
        println("Clicked")
        val intent = Intent(this, com.example.test.ResetPassword::class.java)
        startActivity(intent)
    }

    fun testPage(view: View) {
        println("Okay")

    }

    fun showLogin(view: View) {
        val intent = Intent(this, com.example.test.Home::class.java)
        startActivity(intent)
    }
}