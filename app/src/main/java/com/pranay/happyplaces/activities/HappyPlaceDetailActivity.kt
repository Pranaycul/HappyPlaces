package com.pranay.happyplaces.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pranay.happyplaces.databinding.ActivityHappyPlaceDetailBinding

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHappyPlaceDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarHappyPlaceDetail.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}