package com.example.a2023cs128_event_management

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a2023cs128_event_management.databinding.ActivityConfirmationBinding

class ConfirmationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvName.text = intent.getStringExtra("name") ?: ""
        binding.tvPhone.text = intent.getStringExtra("phone") ?: ""
        binding.tvEmail.text = intent.getStringExtra("email") ?: ""
        binding.tvEventType.text = intent.getStringExtra("eventType") ?: ""
        binding.tvDate.text = intent.getStringExtra("date") ?: ""
        binding.tvGender.text = intent.getStringExtra("gender") ?: ""

        val imageUriString = intent.getStringExtra("imageUri")
        if (!imageUriString.isNullOrEmpty()) {
            binding.ivProfileImage.setImageURI(Uri.parse(imageUriString))
        }

        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}
