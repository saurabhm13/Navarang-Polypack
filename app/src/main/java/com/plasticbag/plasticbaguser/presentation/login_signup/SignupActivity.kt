package com.plasticbag.plasticbaguser.presentation.login_signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.plasticbag.plasticbaguser.R
import com.plasticbag.plasticbaguser.databinding.ActivitySignupBinding
import com.plasticbag.plasticbaguser.presentation.main.MainActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: LoginSignupViewModel by viewModels()

    private var name: String? = null
    private var email: String? = null
    private var phoneNo: String? = null
    private var password: String? = null
    private var conformPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLogin.setOnClickListener {
            val intoLogin = Intent(this, LoginActivity::class.java)
            startActivity(intoLogin)
            finish()
        }

        binding.btnSignup.setOnClickListener {

            binding.btnSignup.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE

            name = binding.nameSignup.editText?.text.toString()
            email = binding.emailSignup.editText?.text.toString()
            phoneNo = binding.phoneNoSignup.editText?.text.toString()
            password = binding.passwordSignup.editText?.text.toString()
            conformPassword = binding.conformPassSignup.editText?.text.toString()

            if (name.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Name", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else if (email.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Email", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else if (password.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Password", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else if (phoneNo.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Phone Number", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else if (conformPassword.isNullOrEmpty()) {
                Toast.makeText(this, "Enter Valid Conform Password", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else if (password != conformPassword) {
                Toast.makeText(this, "Password don't match", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else if (password!!.count() < 6) {
                Toast.makeText(this, "Password should be more than 8 digits", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
                binding.btnSignup.visibility = View.VISIBLE
            }else {

                viewModel.registerUser(email!!, password!!, name!!, phoneNo!!)
                viewModel.authCallback = {
                    val intoMain = Intent(this, MainActivity::class.java)
                    startActivity(intoMain)
                    finish()
                    binding.progressBar.visibility = View.GONE
                }
            }

            viewModel.verificationError.observe(this) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }
        }

    }
}