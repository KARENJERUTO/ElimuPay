package com.emt.elimupay.ui.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emt.elimupay.auth.LoginActivity
import com.emt.elimupay.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout with View Binding
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        // Load saved data
        loadUserData()

        // Set up save button for user profile
        binding.btnSave.setOnClickListener {
            saveUserData()
        }
        showPaymentHistory()

        // Set up payment methods button
        binding.btnPaymentMethods.setOnClickListener {
            showPaymentMethods()
        }

        // Set up security settings button
        binding.btnSecuritySettings.setOnClickListener {
            showSecuritySettings()
        }

        // Set up support button
        binding.btnFAQs.setOnClickListener {
            showSupportInfo()
        }

        binding.btnSupport.setOnClickListener {
            showSupportInfo()
        }

        binding.btnSupport.setOnClickListener {
            showSupportInfo()
        }

        // Set up logout button
        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        return root
    }

    // Save user profile data to SharedPreferences
    private fun saveUserData() {
        val name = binding.etUserName.text.toString()
        val email = binding.etUserEmail.text.toString()
        val phone = binding.etUserPhone.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
            with(sharedPreferences.edit()) {
                putString("userName", name)
                putString("userEmail", email)
                putString("userPhone", phone)
                apply()
            }
            Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    // Load saved user data
    private fun loadUserData() {
        val name = sharedPreferences.getString("userName", "")
        val email = sharedPreferences.getString("userEmail", "")
        val phone = sharedPreferences.getString("userPhone", "")

        binding.etUserName.setText(name)
        binding.etUserEmail.setText(email)
        binding.etUserPhone.setText(phone)
    }

    // Show payment history (this is a placeholder for now)
    private fun showPaymentHistory() {
        // This can be expanded to show a list of payments fetched from the backend or local storage
    }

    // Show available payment methods (this is a placeholder for now)
    private fun showPaymentMethods() {

        // This can be expanded to display a list of payment methods
    }

    // Show security settings (this is a placeholder for now)
    private fun showSecuritySettings() {

        // This can be expanded to offer options like password change, 2FA, etc.
    }

    // Show support info (this is a placeholder for now)
    private fun showSupportInfo() {

        // You can expand this to show contact info or navigate to a support page
    }

    // Log the user out and clear SharedPreferences
    private fun logoutUser() {
        with(sharedPreferences.edit()) {
            clear() // Clear all saved data
            apply()
        }
        // Redirect to login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
