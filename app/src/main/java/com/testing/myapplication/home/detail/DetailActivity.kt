package com.testing.myapplication.home.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.testing.myapplication.R
import com.testing.myapplication.databinding.ActivityDetailBinding
import com.testing.myapplication.home.update.UpdateActivity
import com.testing.myapplication.response.product.Data

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Data>("data")
        initView(data)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.idEdit.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java).putExtra("data", data))
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initView(data: Data?) {
        Glide.with(this)
            .load(data?.photoWarung)
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.ivProduct)

        binding.tvNameWarung.text = data?.nameWarung
        binding.tvAlamatAddress.text = data?.alamatWarung
        binding.tvKordinat.text = "${data?.lat}, ${data?.long}"
    }
}