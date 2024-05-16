package com.emt.elimupay.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emt.elimupay.FeesStatementsActivity
import com.emt.elimupay.databinding.FragmentHomeBinding
import com.emt.elimupay.models.BalanceResponse
import com.emt.elimupay.paymentmethods.PaymentMethods
import com.emt.elimupay.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val statementsTextView = binding.feeStatement
        statementsTextView.setOnClickListener {
            val intent = Intent(activity, FeesStatementsActivity::class.java)
            startActivity(intent)
        }
        val viewBalanceTextView = binding.textViewBalance
        _binding.cardview1.setOnClickListener {
            fetchBalance(viewBalanceTextView)
        }

        val paymentButton = binding.button5
        paymentButton.setOnClickListener {
            val intent = Intent(activity, PaymentMethods::class.java)
            startActivity(intent)
        }

        return root
    }
  private fun fetchBalance(viewBalanceTextView: TextView) {
      val retrofit = Retrofit.Builder()
          .baseUrl("http://192.168.88.188:8000/api/v1/")
          .addConverterFactory(GsonConverterFactory.create())
          .build()

      val apiService = retrofit.create(ApiService::class.java)
      val call = apiService.getBalance()

      call.enqueue(object : Callback<BalanceResponse> {
          override fun onResponse(call: Call<BalanceResponse>, response: Response<BalanceResponse>) {
              if (response.isSuccessful) {
                  val balanceResponse = response.body()
                  if (balanceResponse != null) {
                      viewBalanceTextView.text = balanceResponse.total_fee.toString()
                      binding.textViewBalance.text = "Total Fee: ${balanceResponse.total_fee}"
                  }
              } else {
                  Toast.makeText(activity, "Failed to fetch balance", Toast.LENGTH_SHORT).show()
              }
          }

          override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
              Toast.makeText(activity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
          }
      })
  }
}
