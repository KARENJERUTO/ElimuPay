package com.emt.elimupay.paymentconfirmation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emt.elimupay.R

class MpesaConfirmation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mpesa_confirmation)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve data from intent
        val amountPaid = intent.getStringExtra("amount_paid")
        val paymentDate = intent.getStringExtra("payment_date")
        val paymentMode = intent.getStringExtra("paymentmode")
        val reference = intent.getStringExtra("reference")
        val student = intent.getStringExtra("student")

        // Display data
        findViewById<TextView>(R.id.textViewAmountPaid).text = amountPaid
        findViewById<TextView>(R.id.textViewPaymentDate).text = paymentDate
        findViewById<TextView>(R.id.textViewPaymentMode).text = paymentMode
        findViewById<TextView>(R.id.textViewReference).text = reference
        findViewById<TextView>(R.id.textViewStudent).text = student
    }

    fun confirmPayment(view: View) {
        val intent = Intent(this, ConfirmationActivity::class.java)
        startActivity(intent)
    }
}
