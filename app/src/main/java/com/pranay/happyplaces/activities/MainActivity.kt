package com.pranay.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pranay.happyplaces.adapters.HappyPlacesAdapter
import com.pranay.happyplaces.database.DatabaseHandler
import com.pranay.happyplaces.databinding.ActivityMainBinding
import com.pranay.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getHappyPlacesListFromLocalDatabase()

        binding.fabHappyPlace.setOnClickListener {
            val intent = Intent(this, AddPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQ)
        }
    }

    private fun getHappyPlacesListFromLocalDatabase() {
        val dbHandler = DatabaseHandler(this)
        val happyPlacesList = dbHandler.getHappyPlaceList()


        if (happyPlacesList.size > 0) {
            binding.rvHappyPlaceItem.visibility = View.VISIBLE
            setupHappyPlacesRecyclerView(happyPlacesList)
        } else {
            binding.rvHappyPlaceItem.visibility = View.GONE
        }
    }

    private fun setupHappyPlacesRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>) {
        binding.rvHappyPlaceItem.layoutManager = LinearLayoutManager(this)
        binding.rvHappyPlaceItem.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(happyPlaceList)

        binding.rvHappyPlaceItem.adapter = placesAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_PLACE_ACTIVITY_REQ){
            if(resultCode ==Activity.RESULT_OK){
                getHappyPlacesListFromLocalDatabase()
            }else{
                Log.i("Activity" ,"Cancel")
            }
        }
    }

    companion object {
        const val ADD_PLACE_ACTIVITY_REQ = 1
    }
}