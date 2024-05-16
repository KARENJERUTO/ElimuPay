package com.emt.elimupay.paymentmethods

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emt.elimupay.paymentconfirmation.MpesaConfirmation
import com.emt.elimupay.R

class MpesaPaymentActivity : AppCompatActivity() {
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

    }
    fun showPay(view: View) {
        val intent = Intent(this, MpesaConfirmation::class.java)
        startActivity(intent)
    }
}