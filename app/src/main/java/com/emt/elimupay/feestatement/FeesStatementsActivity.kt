package com.emt.elimupay.feestatement

import FeeEntityAdapter
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.pdf.PdfGenerator
import com.emt.elimupay.R
import com.emt.elimupay.models.FeeEntity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FeesStatementsActivity : AppCompatActivity() {

    private lateinit var recyclerViewTransactions: RecyclerView
    private lateinit var adapter: FeeEntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fees_statements)

        recyclerViewTransactions = findViewById(R.id.recyclerViewTransactions)
        recyclerViewTransactions.layoutManager = LinearLayoutManager(this)

        // Make the API call to fetch fee transactions
        FetchFeeEntitiesTask().execute()
    }

    // Method to handle downloading the fee statement as a PDF
    fun downloadFeeStatement(view: View) {
        if (::adapter.isInitialized) { // Check if adapter is initialized
            val pdfGenerator = PdfGenerator()
            val feeEntities = adapter.getFeeEntities() // Get the fee entities from the adapter
            val outputFile = File(getExternalFilesDir(null), "fee_statement.pdf")

            try {
                val outputStream = FileOutputStream(outputFile)
                pdfGenerator.generatePdf(feeEntities, outputStream)
                outputStream.close()

                // Notify the system that a new file has been created
                val intent = Intent(Intent.ACTION_VIEW)
                val uri = FileProvider.getUriForFile(this, "${packageName}.provider", outputFile)
                intent.setDataAndType(uri, "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)

                Toast.makeText(this, "Fee statement downloaded successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to download fee statement", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Data is not ready for download", Toast.LENGTH_SHORT).show()
        }
    }

    // AsyncTask to fetch the fee entities from the API
    private inner class FetchFeeEntitiesTask : AsyncTask<Void, Void, List<FeeEntity>>() {

        override fun doInBackground(vararg params: Void?): List<FeeEntity> {
            val feeEntities = mutableListOf<FeeEntity>()
            val client = OkHttpClient()

            try {
                val url = "http://172.16.9.74:8007/api/v1/fee/api/v1/fee/get_transactions_for_student/1/"
                val request = Request.Builder().url(url).build()
                val response: Response = client.newCall(request).execute()

                // Check if the response is successful
                if (response.isSuccessful) {
                    val jsonData = response.body?.string()
                    if (jsonData != null) {
                        val jsonArray = JSONArray(jsonData)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val feeEntity = FeeEntity(
                                id = jsonObject.getInt("id"),
                                student_id = jsonObject.getInt("student_id"),
                                description = jsonObject.getString("description"),
                                debit = jsonObject.getInt("debit"),
                                credit = jsonObject.getInt("credit"),
                                balance = jsonObject.getInt("balance"),
                                transaction_date = jsonObject.getString("transaction_date"),
                                firstName = jsonObject.optString("firstName", "Unknown"),
                                middleName = jsonObject.optString("middleName", "Unknown")
                            )
                            feeEntities.add(feeEntity)
                        }
                    }
                } else {
                    Log.e("FeesStatementsActivity", "Unsuccessful response: ${response.message}")
                }
            } catch (e: IOException) {
                Log.e("FeesStatementsActivity", "Error: ${e.message}", e)
            }

            return feeEntities
        }

        override fun onPostExecute(result: List<FeeEntity>) {
            super.onPostExecute(result)
            if (result.isNotEmpty()) {
                adapter = FeeEntityAdapter(result)
                recyclerViewTransactions.adapter = adapter
            } else {
                Toast.makeText(this@FeesStatementsActivity, "No transactions found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
