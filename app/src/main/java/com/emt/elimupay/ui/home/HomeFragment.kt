package com.emt.elimupay.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.emt.elimupay.student.DashboardActivity
import com.emt.elimupay.feestructure.FeeStructureActivity
import com.emt.elimupay.feestatement.FeesStatementsActivity
import com.emt.elimupay.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewBalanceTextView: TextView
    private lateinit var welcomeTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewBalanceTextView = binding.textViewStudents
        welcomeTextView = binding.textViewWelcome

        // Simulate saving the username to SharedPreferences
        saveUserName("John Doe") // Replace "John Doe" with the actual user name or dynamic value

        // Retrieve the username from SharedPreferences
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("userName", "User") // Default is "User" if not found

        // Set the welcome message with the retrieved user name
        welcomeTextView.text = "Welcome, $userName!"

        binding.cardview1.setOnClickListener {
            val intent = Intent(activity, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.cardviewFeeStructure.setOnClickListener {
            val intent = Intent(activity, FeeStructureActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    private fun saveUserName(userName: String) {
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPref?.edit()?.apply {
            putString("userName", userName)
            apply()
        }
    }

    private fun fetchPaidFees(): String {
        // Simulate fetching paid fees from a data source
        return "Term 1: Sh500\nTerm 2: Sh400"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paidFees = fetchPaidFees()

        // Add click listener to textViewNotifications
        binding.textViewNotifications.setOnClickListener {
            showNotificationsDialog(paidFees)
        }

        binding.cardview2.setOnClickListener {
            Log.d("HomeFragment", "Fee statement clicked")
            val intent = Intent(activity, FeesStatementsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showNotificationsDialog(paidFees: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Paid Fees Notification")
        builder.setMessage("You have paid the following fees:\n\n$paidFees")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
