package com.plasticbag.plasticbaguser.presentation.add_edit_address

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.plasticbag.plasticbaguser.R
import com.plasticbag.plasticbaguser.databinding.ActivityAddEditAddressBinding

class AddEditAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditAddressBinding
    private val viewModel: AddEditAddressViewModel by viewModels()

    private lateinit var flat: String
    private lateinit var area: String
    private lateinit var landmark: String
    private lateinit var pincode: String
    private lateinit var city: String
    private lateinit var state: String
    private lateinit var country: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveChanges.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            flat = binding.flat.editText?.text.toString()
            area = binding.area.editText?.text.toString()
            landmark = binding.landmark.editText?.text.toString()
            pincode = binding.pincode.editText?.text.toString()
            city = binding.city.editText?.text.toString()
            state = binding.state.editText?.text.toString()
            country = binding.country.editText?.text.toString()

            if (flat != "" && area != "" && pincode != "" && city != "" && state != "" && country != "") {
                val address = "$flat,$area'$city,$state$pincode'$country"
                viewModel.saveAddress(address)
            }else {
                Toast.makeText(this, "Add required fields", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.successCallback = {
            Toast.makeText(this, "Address Saved!!", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
            clearDataField()
            finish()
        }

        viewModel.errorCallback = {
            Toast.makeText(this, "Error Occurred!!", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun clearDataField() {
        binding.flat.editText?.setText("")
        binding.area.editText?.setText("")
        binding.landmark.editText?.setText("")
        binding.pincode.editText?.setText("")
        binding.city.editText?.setText("")
        binding.state.editText?.setText("")
        binding.country.editText?.setText("")
    }


}