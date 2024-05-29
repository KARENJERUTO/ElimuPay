package com.emt.elimupay.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emt.elimupay.FeesStatementsActivity
import com.emt.elimupay.databinding.FragmentHomeBinding
import com.emt.elimupay.paymentmethods.PaymentMethods
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewBalanceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewBalanceTextView = binding.textViewBalance

        val statementsTextView = binding.feeStatement
        statementsTextView.setOnClickListener {
            Log.d("HomeFragment", "Fee statement clicked")
            val intent = Intent(activity, FeesStatementsActivity::class.java)
            startActivity(intent)
        }

        val paymentButton = binding.button5
        paymentButton.setOnClickListener {
            Log.d("HomeFragment", "Payment button clicked")
            val intent = Intent(activity, PaymentMethods::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchBalance()
    }

    private fun fetchBalance() {
        Log.d("HomeFragment", "Fetching balance")

        val url = "http://192.168.90.64:8000/api/v1/fee/api/v1/fee/get_total_balance_for_student/1/"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("HomeFragment", "Error: ${e.message}", e)
                activity?.runOnUiThread {
                    Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        val totalBalance = jsonResponse.getDouble("total_balance")
                        activity?.runOnUiThread {
                            viewBalanceTextView.text = "Total Fee: $totalBalance"
                        }
                    } catch (e: Exception) {
                        Log.e("HomeFragment", "Error parsing response", e)
                        activity?.runOnUiThread {
                            Toast.makeText(activity, "Error parsing response", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Failed to fetch balance: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
