package com.plasticbag.plasticbaguser.presentation.edit_profile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.plasticbag.plasticbaguser.R
import com.plasticbag.plasticbaguser.databinding.ActivityEditProfileBinding
import com.plasticbag.plasticbaguser.model.UserDetails
import com.plasticbag.plasticbaguser.util.Constants.Companion.EMAIL
import com.plasticbag.plasticbaguser.util.Constants.Companion.IMAGE
import com.plasticbag.plasticbaguser.util.Constants.Companion.NAME
import com.plasticbag.plasticbaguser.util.Constants.Companion.PHONE_NO

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()

    private lateinit var name: String
    private lateinit var image: String
    private lateinit var phoneNo: String

    private var selectedImageUri: Uri? = null

    private var croppedImageUri: Uri? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the cropped image URI.
            croppedImageUri = result.uriContent!!

            Glide.with(this)
                .load(croppedImageUri)
                .into(binding.profileImage)
        } else {
            // An error occurred.
            val exception = result.error
            if (exception != null) {
                Toast.makeText(this, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setIncomingData()

        binding.back.setOnClickListener {
            finish()
        }

        binding.profileImage.setOnClickListener {
            startCrop()
        }

        binding.saveChanges.setOnClickListener {

            binding.saveChanges.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            name = binding.name.editText?.text.toString().trim()
//            email = binding.email.editText?.text.toString().trim()
            phoneNo = binding.phoneNo.editText?.text.toString().trim()

            if (croppedImageUri == null) {
                viewModel.updateTextUserData(name, phoneNo)
            }else {
                croppedImageUri?.let { it1 -> viewModel.updateUserData(name, it1, phoneNo) }
            }
        }

        viewModel.successCallback = {
            finish()
        }

        viewModel.errorCallBack = {
            Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            binding.saveChanges.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun setIncomingData() {
        name = intent.getStringExtra(NAME).toString()
        image = intent.getStringExtra(IMAGE).toString()
//        email= intent.getStringExtra(EMAIL).toString()
        phoneNo = intent.getStringExtra(PHONE_NO).toString()

        binding.name.editText?.setText(name)
//        binding.email.editText?.setText(email)
        binding.phoneNo.editText?.setText(phoneNo)

        if (image.isEmpty() || image == "null") {
            // Load a default profile image or placeholder.
            Glide.with(this)
                .load(R.drawable.profile_place_holder)
                .into(binding.profileImage)
        }else {
            Glide.with(this)
                .load(image)
                .into(binding.profileImage)
        }
    }

    private fun startCrop() {
        cropImage.launch(
            CropImageContractOptions(
                selectedImageUri, CropImageOptions(
                    guidelines = CropImageView.Guidelines.ON_TOUCH,
                    cropShape = CropImageView.CropShape.RECTANGLE,
                    fixAspectRatio = true,
                    aspectRatioY = 1,
                    aspectRatioX = 1,
                    initialCropWindowPaddingRatio = 0f,
                    cropMenuCropButtonTitle = getString(R.string.send),
                    outputRequestSizeOptions = CropImageView.RequestSizeOptions.RESIZE_INSIDE,
                )
            )
        )
    }
}