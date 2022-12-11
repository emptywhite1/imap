package com.example.imap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imap.databinding.ActivityMain2Binding
import com.example.imap.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private lateinit var _binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(_binding.root)

    }
}