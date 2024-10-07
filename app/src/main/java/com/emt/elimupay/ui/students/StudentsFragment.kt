package com.emt.elimupay.ui.students

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emt.elimupay.adapters.StudentDataAdapter
import com.emt.elimupay.databinding.FragmentStudentsBinding
import com.emt.elimupay.models.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class StudentsFragment : Fragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var studentDataAdapter: StudentDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up the RecyclerView
        binding.rvStudentList.layoutManager = LinearLayoutManager(requireContext())
        studentDataAdapter = StudentDataAdapter(emptyList())
        binding.rvStudentList.adapter = studentDataAdapter

        // Fetch students data
        val url = "http://172.16.9.74:8001/api/v1/studentsparents/students-info-by-parent/12345678/"
        fetchData(url)

        return root
    }

    private fun fetchData(urlString: String) {
        // Use coroutines for background thread operation
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(urlString).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBodyString = response.body?.string()
                    responseBodyString?.let {
                        Log.d("StudentsFragment", "Response Body: $it")  // Log the raw response
                        val jsonObject = JSONObject(it)

                        // Parse the JSON data
                        val message = jsonObject.optString("message", "No message")
                        val statusCode = jsonObject.optInt("status_code", 0)
                        val dataArray = jsonObject.optJSONArray("data")

                        val students = mutableListOf<Data>()
                        if (dataArray != null) {
                            for (i in 0 until dataArray.length()) {
                                val studentObject = dataArray.getJSONObject(i)

                                // Create a Data object using your specified structure
                                val student = Data(
                                    admNumber = studentObject.optString("admNumber", "Unknown"),
                                    firstName = studentObject.optString("firstName", "Unknown"),
                                    lastName = studentObject.optString("lastName", "Unknown"),
                                    studentGender = studentObject.optString("studentGender", "Unknown"),
                                    healthStatus = studentObject.opt("healthStatus"),  // Handle missing fields safely
                                    grade = studentObject.optInt("grade", 0),
                                    stream = studentObject.optString("stream", "Unknown"),
                                    upiNumber = studentObject.optString("upiNumber", "Unknown")
                                )
                                students.add(student)
                            }

                            // Update the adapter on the main thread
                            withContext(Dispatchers.Main) {
                                Log.d("StudentsFragment", "Updating adapter with ${students.size} students")
                                studentDataAdapter.updateData(students) // Pass the correct data to the adapter
                            }
                        } else {
                            Log.e("StudentsFragment", "Data array is null")
                        }
                    }
                } else {
                    Log.e("StudentsFragment", "Failed to fetch data: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e("StudentsFragment", "Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
