package com.emt.elimupay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.adapters.FeeEntityAdapter
import com.emt.elimupay.models.FeeEntity
import com.emt.elimupay.models.FeesResponse
import com.emt.elimupay.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeesStatementsActivity : AppCompatActivity() {

    private lateinit var recyclerViewTransactions: RecyclerView
    private lateinit var adapter: FeeEntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fees_statements)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerViewTransactions = findViewById(R.id.recyclerViewTransactions)
        recyclerViewTransactions.layoutManager = LinearLayoutManager(this)

        // Initialize Retrofit
        val apiService = RetrofitClient.apiService

        // Make the API call
        val call = apiService.getFees()
        call.enqueue(object : Callback<FeesResponse> {
            override fun onResponse(call: Call<FeesResponse>, response: Response<FeesResponse>) {
                if (response.isSuccessful) {
                    val feesResponse = response.body()
                    if (feesResponse != null) {
                        // Assuming FeesResponse.entity is a list of FeeEntity
                        @Suppress("UNCHECKED_CAST")
                        val feeEntities = feesResponse.entity as List<FeeEntity>
                        adapter = FeeEntityAdapter(feeEntities)
                        recyclerViewTransactions.adapter = adapter
                    } else {
                        println("FeesResponse is null")
                    }
                } else {
                    println("Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FeesResponse>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }
}
