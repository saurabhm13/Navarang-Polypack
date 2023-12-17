package com.plasticbag.plasticbaguser.presentation.login_signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.plasticbag.plasticbaguser.R
import com.plasticbag.plasticbaguser.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: LoginSignupViewModel by viewModels()

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSendEmail.setOnClickListener {
            email = binding.email.editText?.text.toString()

            if (email != "") {
                binding.progressbar.visibility = View.VISIBLE
                binding.btnSendEmail.visibility = View.GONE
                viewModel.resetPassword(email)
            }
        }

        viewModel.resetEmailSendCallback = {
            Toast.makeText(this, "Email Send", Toast.LENGTH_LONG).show()
            binding.progressbar.visibility = View.GONE
            binding.btnSendEmail.visibility = View.VISIBLE
        }

        viewModel.errorCallback = {
            Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            binding.progressbar.visibility = View.GONE
            binding.btnSendEmail.visibility = View.VISIBLE
        }

    }
}