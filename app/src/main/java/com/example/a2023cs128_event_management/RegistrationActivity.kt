package com.example.a2023cs128_event_management

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.a2023cs128_event_management.databinding.ActivityRegistrationBinding
import java.util.Calendar

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private var selectedDate: String = ""
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.ivProfilePreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                showConfirmationDialog()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            selectedDate = "%02d/%02d/%d".format(d, m + 1, y)
            binding.tvSelectedDate.text = selectedDate
        }, year, month, day).show()
    }

    private fun validateForm(): Boolean {
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, getString(R.string.err_name), Toast.LENGTH_SHORT).show()
            return false
        }

        val phone = binding.etPhone.text.toString().trim()
        if (phone.isEmpty() || phone.length < 7) {
            Toast.makeText(this, getString(R.string.err_phone), Toast.LENGTH_SHORT).show()
            return false
        }

        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, getString(R.string.err_email), Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.spinnerEventType.selectedItemPosition == 0) {
            Toast.makeText(this, getString(R.string.err_event_type), Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, getString(R.string.err_date), Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.err_gender), Toast.LENGTH_SHORT).show()
            return false
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, getString(R.string.err_image), Toast.LENGTH_SHORT).show()
            return false
        }

        if (!binding.cbTerms.isChecked) {
            Toast.makeText(this, getString(R.string.err_terms), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(R.string.dialog_confirm)) { _, _ ->
                proceedToConfirmation()
            }
            .setNegativeButton(getString(R.string.dialog_cancel), null)
            .show()
    }

    private fun proceedToConfirmation() {
        val genderId = binding.rgGender.checkedRadioButtonId
        val gender = when (genderId) {
            R.id.rbMale -> getString(R.string.radio_male)
            R.id.rbFemale -> getString(R.string.radio_female)
            else -> getString(R.string.radio_other)
        }

        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra("name", binding.etName.text.toString().trim())
            putExtra("phone", binding.etPhone.text.toString().trim())
            putExtra("email", binding.etEmail.text.toString().trim())
            putExtra("eventType", binding.spinnerEventType.selectedItem.toString())
            putExtra("date", selectedDate)
            putExtra("gender", gender)
            putExtra("imageUri", selectedImageUri.toString())
        }
        startActivity(intent)
    }
}
