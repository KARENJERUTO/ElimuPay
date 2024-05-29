package com.emt.elimupay.ui.students

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.emt.elimupay.databinding.FragmentStudentsBinding
import com.emt.elimupay.models.MyStudentsResponse
import com.emt.elimupay.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentsFragment : Fragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(StudentsViewModel::class.java)

        _binding = FragmentStudentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.etPaidFees

        // Fetch students data
        fetchStudentsData(textView)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchStudentsData(textView: TextView) {
        val apiService =
            RetrofitClient.apiService // Obtain ApiService instance from RetrofitClient
        val call = apiService.getStudents() // Call getStudents() on the ApiService instance

        call.enqueue(object : Callback<List<MyStudentsResponse>> {
            override fun onResponse(
                call: Call<List<MyStudentsResponse>>,
                response: Response<List<MyStudentsResponse>>
            ) {
                if (response.isSuccessful) {
                    val studentsList = response.body()
                    if (studentsList != null && studentsList.isNotEmpty()) {
                        // Displaying the first student's ID in the TextView
                        textView.text = "Student ID: ${studentsList[0].studentID}"
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<MyStudentsResponse>>, t: Throwable) {
                // Handle network failure
            }
        })
    }
}