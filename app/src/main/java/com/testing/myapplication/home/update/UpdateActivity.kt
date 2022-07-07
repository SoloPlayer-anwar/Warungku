package com.testing.myapplication.home.update

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.testing.myapplication.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}