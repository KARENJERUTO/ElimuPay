package com.emt.elimupay.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.emt.elimupay.DashboardActivity
import com.emt.elimupay.FeeStructureActivity
import com.emt.elimupay.FeesStatementsActivity
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

        viewBalanceTextView = binding.textViewBalance
        welcomeTextView = binding.textViewWelcome

        // Set the welcome message
        welcomeTextView.text = "Welcome!"
        binding.cardview1.setOnClickListener {
            // Start the activity you want to navigate to
            val intent = Intent(activity, DashboardActivity::class.java)
            startActivity(intent)
        }

        // Add click listener to cardviewFeeStructure
        binding.cardviewFeeStructure.setOnClickListener {
            val intent = Intent(activity, FeeStructureActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add click listener to textViewNotifications
        binding.textViewNotifications.setOnClickListener {
            showNotificationsDialog()
        }

        // Add click listener to cardviewFeeStatement
        binding.cardview2.setOnClickListener {
            Log.d("HomeFragment", "Fee statement clicked")
            val intent = Intent(activity, FeesStatementsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showNotificationsDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Notifications")
        builder.setMessage("You have new notifications.")
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