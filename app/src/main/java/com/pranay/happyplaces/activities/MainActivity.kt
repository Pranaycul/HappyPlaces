package com.pranay.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pranay.happyplaces.database.DatabaseHandler
import com.pranay.happyplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getHappyPlacesListFromLocalDatabase()

        binding.fabHappyPlace.setOnClickListener {
            val intent= Intent(this, AddPlaceActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getHappyPlacesListFromLocalDatabase(){
        val dbHandler = DatabaseHandler(this)
        val happyPlacesList = dbHandler.getHappyPlaceList()

        if(happyPlacesList.size > 0){
            for(i in happyPlacesList){
                Log.i("Title",i.title)
            }
        }
    }
}