package com.emt.elimupay

import StudentAdapter
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DashboardActivity : AppCompatActivity() {

    private lateinit var RecyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RecyclerView = findViewById(R.id.RecyclerView)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(emptyList())
        RecyclerView.adapter = studentAdapter

        // Fetch data from backend
        FetchDataTask().execute("http://192.168.91.17:8000/api/v1/students/")
    }

    private inner class FetchDataTask : AsyncTask<String, Void, List<Student>>() {
        override fun doInBackground(vararg params: String): List<Student> {
            val urlString = params[0]
            val students = mutableListOf<Student>()

            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?

                while (bufferedReader.readLine().also { line = it } != null) {
                    response.append(line)
                }

                val jsonArray = JSONArray(response.toString())
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val firstName = jsonObject.getString("firstName")
                    val lastName = jsonObject.getString("lastName")
                    val balance = jsonObject.getInt("balance")
                    students.add(Student(firstName, lastName, balance))
                }

                bufferedReader.close()
                inputStream.close()
                connection.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return students
        }

        override fun onPostExecute(result: List<Student>) {
            super.onPostExecute(result)
            studentAdapter.updateData(result)
        }
    }
}
