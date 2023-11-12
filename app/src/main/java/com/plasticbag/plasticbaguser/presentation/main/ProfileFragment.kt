package com.plasticbag.plasticbaguser.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.plasticbag.plasticbaguser.R
import com.plasticbag.plasticbaguser.databinding.FragmentProductBinding
import com.plasticbag.plasticbaguser.databinding.FragmentProfileBinding
import com.plasticbag.plasticbaguser.model.UserDetails
import com.plasticbag.plasticbaguser.presentation.add_edit_address.AddEditAddressActivity
import com.plasticbag.plasticbaguser.presentation.edit_profile.EditProfileActivity
import com.plasticbag.plasticbaguser.presentation.login_signup.LoginActivity
import com.plasticbag.plasticbaguser.presentation.orders.OrderActivity
import com.plasticbag.plasticbaguser.util.Constants.Companion.EMAIL
import com.plasticbag.plasticbaguser.util.Constants.Companion.IMAGE
import com.plasticbag.plasticbaguser.util.Constants.Companion.NAME
import com.plasticbag.plasticbaguser.util.Constants.Companion.PHONE_NO

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var userDetails: UserDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        viewModel.getUserDetails()
        viewModel.userDetailsLiveData.observe(viewLifecycleOwner) {
            if (it.image.isNotBlank()) {
                activity?.let { it1 ->
                    Glide.with(it1)
                        .load(it.image)
                        .into(binding.profileImageProfile)
                }
            }

            binding.nameProfile.text = it.name
//            binding.address.text = it.address

            userDetails = it
        }

        binding.profileLayout.setOnClickListener {
            val intoEditProfile = Intent(activity, EditProfileActivity::class.java)
            intoEditProfile.putExtra(NAME, userDetails.name)
            intoEditProfile.putExtra(EMAIL, userDetails.email)
            intoEditProfile.putExtra(PHONE_NO, userDetails.phoneNo)
            intoEditProfile.putExtra(IMAGE, userDetails.image)
            startActivity(intoEditProfile)
        }

//        binding.address.setOnClickListener {
//            val intoAddEdit = Intent(activity, AddEditAddressActivity::class.java)
//            startActivity(intoAddEdit)
//        }

        binding.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intoLogin = Intent(activity, LoginActivity::class.java)
            startActivity(intoLogin)
            activity?.finish()
        }

        binding.orders.setOnClickListener {
            val intoOrder = Intent(activity, OrderActivity::class.java)
            startActivity(intoOrder)
        }

        return binding.root
    }

    private fun addDataFields() {

    }

}