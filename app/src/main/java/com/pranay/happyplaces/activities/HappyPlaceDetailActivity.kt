package com.pranay.happyplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pranay.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import com.pranay.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding
    private lateinit var happyPlaceDetailModel: HappyPlaceModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            happyPlaceDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)!!

        }


        setSupportActionBar(binding.toolbarHappyPlaceDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.title = happyPlaceDetailModel.title

        binding.toolbarHappyPlaceDetail.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.ivPlaceImage.setImageURI(Uri.parse(happyPlaceDetailModel.image))
        binding.tvDescription.text = happyPlaceDetailModel.description
        binding.tvLocation.text = happyPlaceDetailModel.location
    }
}