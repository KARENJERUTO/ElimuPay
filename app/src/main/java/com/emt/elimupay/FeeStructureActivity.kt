package com.emt.elimupay

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.emt.elimupay.adapters.FeeCategoryAdapter
import com.emt.elimupay.databinding.ActivityFeeStructureBinding
import com.emt.elimupay.models.FeeCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class FeeStructureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeeStructureBinding
    private lateinit var adapter: FeeCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFeeStructureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = FeeCategoryAdapter(emptyList())
        binding.recyclerView.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerView.adapter = adapter

        // Make network request on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            fetchFeeStructure()
        }
    }

    private fun fetchFeeStructure() {
        val url = "http://192.168.90.20:8000/api/v1/fee/api/v1/fee/fee structure/get_fee_structure/5/"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@FeeStructureActivity,
                        "Failed to fetch fee structure: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("FeeStructureActivity", "Failed to fetch fee structure", e)
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.d("FeeStructureActivity", "Response: $responseBody")
                if (responseBody != null) {
                    runOnUiThread {
                        try {
                            // Parse and display the fee structure
                            val responseJson = JSONObject(responseBody)
                            val feeCategoryArray =
                                responseJson.getJSONArray("student_fee_categories")
                            val feeCategories = mutableListOf<FeeCategory>()

                            for (i in 0 until feeCategoryArray.length()) {
                                val categoryObject = feeCategoryArray.getJSONObject(i)
                                val categoryId = categoryObject.getInt("category_id")
                                val categoryName = categoryObject.getString("category_name")
                                val amount = categoryObject.getInt("amount")
                                val feeCategory = FeeCategory(categoryId, categoryName, amount)
                                feeCategories.add(feeCategory)
                            }

                            Log.d("FeeStructureActivity", "Parsed fee categories: $feeCategories")
                            adapter.updateData(feeCategories)
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@FeeStructureActivity,
                                "Error parsing fee structure: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("FeeStructureActivity", "Error parsing fee structure", e)
                        } catch (e: JSONException) {
                            Toast.makeText(
                                this@FeeStructureActivity,
                                "Error parsing fee structure: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("FeeStructureActivity", "Error parsing fee structure", e)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@FeeStructureActivity,
                            "Failed to fetch fee structure: Empty response",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("FeeStructureActivity", "Failed to fetch fee structure: Empty response")
                    }
                }
            }
        })
    }
}
