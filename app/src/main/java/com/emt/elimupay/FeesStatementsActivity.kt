package com.emt.elimupay

import FeeEntityAdapter
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.models.FeeEntity
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FeesStatementsActivity : AppCompatActivity() {

    private lateinit var recyclerViewTransactions: RecyclerView
    private lateinit var adapter: FeeEntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fees_statements)

        recyclerViewTransactions = findViewById(R.id.recyclerViewTransactions)
        recyclerViewTransactions.layoutManager = LinearLayoutManager(this)

        // Make the API call
        FetchFeeEntitiesTask().execute()
    }

    inner class FetchFeeEntitiesTask : AsyncTask<Void, Void, List<FeeEntity>>() {

        override fun doInBackground(vararg params: Void?): List<FeeEntity> {
            val feeEntities = mutableListOf<FeeEntity>()

            try {
                val url = URL("http://192.168.90.64:8000/api/v1/fee/api/v1/fee/get_transactions_for_student/1/")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    // Parse JSON response
                    val jsonArray = JSONArray(response.toString())
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val feeEntity = FeeEntity(
                            jsonObject.getInt("balance"),
                            jsonObject.getInt("credit"),
                            jsonObject.getInt("debit"),
                            jsonObject.getString("description"),
                            jsonObject.getInt("id"),
                            jsonObject.getInt("student_id"),
                            jsonObject.getString("transaction_date")
                        )
                        feeEntities.add(feeEntity)
                    }
                } else {
                    Log.e("FeesStatementsActivity", "Request failed with code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("FeesStatementsActivity", "Error: ${e.message}", e)
            }

            return feeEntities
        }

        override fun onPostExecute(result: List<FeeEntity>?) {
            super.onPostExecute(result)
            if (result != null) {
                adapter = FeeEntityAdapter(result)
                recyclerViewTransactions.adapter = adapter
            } else {
                Log.e("FeesStatementsActivity", "No data received from the server")
            }
        }
    }
}
