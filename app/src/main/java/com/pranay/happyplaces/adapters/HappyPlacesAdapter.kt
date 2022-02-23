package com.pranay.happyplaces.adapters


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pranay.happyplaces.activities.AddPlaceActivity
import com.pranay.happyplaces.activities.MainActivity
import com.pranay.happyplaces.databinding.ItemHappyPlaceBinding
import com.pranay.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdapter(  private val context: Context,private var list:ArrayList<HappyPlaceModel>) : RecyclerView.Adapter<HappyPlacesAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int,model: HappyPlaceModel)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener =listener
    }

    class ViewHolder(binding: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivImage = binding.ivPlaceImage
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription




    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHappyPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model= list[position]


            holder.ivImage.setImageURI(Uri.parse(model.image))
            holder.tvTitle.text = model.title
            holder.tvDescription.text= model.description

        holder.itemView.setOnClickListener {
            mListener.onItemClick(position,model)
        }



    }
    fun notifyEditItem(activity:Activity, position: Int, requestCode:Int){
        val intent= Intent( context, AddPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent,requestCode)

        notifyItemChanged(position)

    }

    override fun getItemCount(): Int {
       return list.size
    }

}


