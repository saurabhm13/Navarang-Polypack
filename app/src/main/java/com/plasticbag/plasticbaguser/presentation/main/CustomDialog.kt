package com.plasticbag.plasticbaguser.presentation.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.plasticbag.plasticbaguser.databinding.CustomDialogLayoutBinding
import com.plasticbag.plasticbaguser.model.ProductDetails

class CustomDialog(context: Context, private val productDetails: ProductDetails) : Dialog(context) {

    private lateinit var binding: CustomDialogLayoutBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = CustomDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.alertQuantity.editText?.setText(productDetails.quantity)

        binding.save.setOnClickListener {
            val newQuantity = binding.alertQuantity.editText?.text.toString()
            viewModel.editQuantity(productDetails, newQuantity)
            dismiss()
            Toast.makeText(context, "Quantity saved!", Toast.LENGTH_SHORT).show()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}