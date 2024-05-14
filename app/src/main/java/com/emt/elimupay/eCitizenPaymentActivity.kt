package com.emt.elimupay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class eCitizenPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ecitizen_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.eCitizenPaymentActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val buttonPay = findViewById<View>(R.id.buttonPay)
        buttonPay.setOnClickListener {

            // Construct the eCitizen payment URL with parameters
            val ecitizenPaymentUrl =
                "https://example.com/payment?"

            // Create a Uri object from the URL string
            val uri = Uri.parse(ecitizenPaymentUrl)

            // Create an Intent with ACTION_VIEW and the payment Uri
            val intent = Intent(Intent.ACTION_VIEW, uri)

            // Verify that the intent resolves to a browser app
            if (intent.resolveActivity(packageManager) != null) {
                // Launch the browser activity to redirect to eCitizen for payment
                startActivity(intent)
            } else {
                // Handle the case where no browser app is available
                // You can display an error message to the user or take alternative actions
            }
        }
    }
}