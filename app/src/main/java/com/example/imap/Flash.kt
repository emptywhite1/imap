package com.example.imap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.imap.databinding.ActivityFlashBinding


class Flash : AppCompatActivity() {
    private lateinit var _binding: ActivityFlashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFlashBinding.inflate(layoutInflater)
        setContentView(_binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1180)


    }


}