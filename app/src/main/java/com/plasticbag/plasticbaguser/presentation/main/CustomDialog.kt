package com.plasticbag.plasticbaguser.presentation.main

//import android.content.Context
//import android.view.LayoutInflater
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import com.google.android.material.textfield.TextInputLayout
//import com.plasticbag.plasticbaguser.R
//
//class CustomDialog(context: Context) {
//
//    private val dialog: AlertDialog
//
//    init {
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Custom Dialog")
//        val inflater = LayoutInflater.from(context)
//        val view = inflater.inflate(R.layout.custom_dialog_layout, null)
//
//        val dText = view.findViewById<TextInputLayout>(R.id.alertQuantity)
//
//        builder.setView(view)
//            .setPositiveButton("Save") { d, _ ->
//                val enteredText = dText.editText?.text.toString()
//
//                // Example condition check
//                if (enteredText == "1") {
//                    // Update the UI or perform any action
//                    // For example, you can update a TextView with the entered text
//                    // textView.text = enteredText
//                    // Note: Replace "your_condition" with your actual condition
//
//                    // You can close the dialog if the condition is met
//                    d.dismiss()
//                } else {
//                    // Show a Toast message if the condition is not met
//                    Toast.makeText(context, "Condition not matched", Toast.LENGTH_SHORT).show()
//                    d.cancel()
//
//                    // Keep the dialog open
//                }
//            }
//            .setNegativeButton("Cancel") { _, _ ->
//                // Handle cancel button click (if needed)
//            }
//
//        dialog = builder.create()
//    }
//
//    fun show() {
//        dialog.show()
//    }
//}



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