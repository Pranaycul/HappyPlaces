package com.pranay.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranay.happyplaces.adapters.HappyPlacesAdapter
import com.pranay.happyplaces.database.DatabaseHandler
import com.pranay.happyplaces.databinding.ActivityMainBinding
import com.pranay.happyplaces.models.HappyPlaceModel
import com.pranay.happyplaces.utils.SwipeToEditCallback

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

        val placesAdapter = HappyPlacesAdapter(this,happyPlaceList)

        binding.rvHappyPlaceItem.adapter = placesAdapter

       placesAdapter.setOnItemClickListener(object :HappyPlacesAdapter.OnItemClickListener{
           override fun onItemClick(position: Int,model: HappyPlaceModel) {

               val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
               intent.putExtra(EXTRA_PLACE_DETAILS,model)
               startActivity(intent)
           }
       })
        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter =binding.rvHappyPlaceItem.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQ
                )
            }

        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.rvHappyPlaceItem)

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
        const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}


