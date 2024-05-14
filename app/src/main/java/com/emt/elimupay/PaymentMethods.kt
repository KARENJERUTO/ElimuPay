package com.emt.elimupay

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentMethods : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_methods)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.paymentmethods)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    fun payWithMpesa(view: View) {
        val intent = Intent(this, MpesaPaymentActivity::class.java)
        startActivity(intent)
    }

    fun payWithCard(view: View) {
        val intent = Intent(this, CardPaymentActivity::class.java)
        startActivity(intent)
    }

    fun payWitheCitizen(view: View) {
        val intent = Intent(this, eCitizenPaymentActivity::class.java)
        startActivity(intent)
    }

    fun payWithBankAccount(view: View) {
        val intent = Intent(this, BankPaymentActivity::class.java)
        startActivity(intent)
    }



}