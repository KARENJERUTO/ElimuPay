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
    private lateinit var notificationCounterTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewBalanceTextView = binding.textViewStudents
        welcomeTextView = binding.textViewWelcome
        notificationCounterTextView = binding.notificationCounter

        // Set initial notification counter state
        updateNotificationCounter()

        // Retrieve the username from SharedPreferences
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPref?.getString("userName", "User") // Default is "User" if not found
        welcomeTextView.text = "Welcome, $userName!"

        binding.buttonStudents.setOnClickListener {
            val intent = Intent(activity, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.buttonFeeStructure.setOnClickListener {
            val intent = Intent(activity, FeeStructureActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val paidFees = fetchPaidFees()

        // Add click listener to the notification icon
        binding.imageViewNotificationIcon.setOnClickListener {
            showNotificationsDialog(paidFees)
            // Once notifications are read, mark them as read and update the counter
            markNotificationsAsRead()
        }

        binding.buttonFeeStatement.setOnClickListener {
            Log.d("HomeFragment", "Fee statement clicked")
            val intent = Intent(activity, FeesStatementsActivity::class.java)
            startActivity(intent)
        }

        // Update the notification counter when paid fees change
        updateNotificationCounter()
    }

    private fun fetchPaidFees(): String {
        // Fetching paid fees logic
        val fees = "Term 1: Sh15000" // Change this value as needed
        // Logic to update the unread notifications count if fees change
        if (fees.isNotEmpty()) {
            // Update the unread notifications count in SharedPreferences
            val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPref?.edit()?.apply {
                putInt("unreadNotifications", 1) // Set the unread notifications count to 1 if fees are updated
                apply()
            }
            // Update the notification counter UI
            updateNotificationCounter()
        }
        return fees
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

    private fun markNotificationsAsRead() {
        // Update SharedPreferences to mark notifications as read
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPref?.edit()?.apply {
            putInt("unreadNotifications", 0) // Mark all notifications as read
            apply()
        }

        // Update the notification counter immediately after marking as read
        updateNotificationCounter()
    }

    private fun updateNotificationCounter() {
        // Retrieve the unread notifications count from SharedPreferences
        val sharedPref = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val unreadNotifications = sharedPref?.getInt("unreadNotifications", 0) // Default to 0 if not found

        // Set the visibility and text of the notification counter based on unread notifications
        if (unreadNotifications != null && unreadNotifications > 0) {
            notificationCounterTextView.text = unreadNotifications.toString()
            notificationCounterTextView.visibility = View.VISIBLE
        } else {
            notificationCounterTextView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
