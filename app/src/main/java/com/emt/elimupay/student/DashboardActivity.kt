package com.emt.elimupay.student

import com.emt.elimupay.adapters.StudentAdapter
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emt.elimupay.R
import com.emt.elimupay.models.Student
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        recyclerView = findViewById(R.id.RecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(emptyList())
        recyclerView.adapter = studentAdapter

        val url = "http://192.168.90.244:8007/api/v1/studentsparents/students-by-parent/12345678/"
        FetchDataTask().execute(url)

    }


    private inner class FetchDataTask : AsyncTask<String, Void, List<Student>>() {
        override fun doInBackground(vararg params: String): List<Student> {
            val urlString = params[0]
            val students = mutableListOf<Student>()

            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(urlString)
                    .build()

                val response: Response = client.newCall(request).execute()
                val responseBodyString = response.body?.string()

                if (response.isSuccessful && !responseBodyString.isNullOrEmpty()) {
                    val jsonObject = JSONObject(responseBodyString)
                    val dataArray = jsonObject.getJSONArray("data")

                    for (i in 0 until dataArray.length()) {
                        val studentObject = dataArray.getJSONObject(i)
                        val firstName = studentObject.getString("firstName")
                        val lastName = studentObject.getString("lastName")
                        val balance = studentObject.getInt("balance")
                        students.add(Student(firstName, lastName, balance))
                    }

                } else {
                    Log.e("FetchDataTask", "Failed to fetch data: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("FetchDataTask", "Exception: ${e.message}")
                e.printStackTrace()
            }

            return students
        }

        override fun onPostExecute(result: List<Student>) {
            super.onPostExecute(result)
            // Update UI with the fetched data
            studentAdapter.updateData(result)
        }
    }
}