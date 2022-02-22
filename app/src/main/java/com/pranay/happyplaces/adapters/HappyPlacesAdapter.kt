package com.pranay.happyplaces.adapters


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.pranay.happyplaces.databinding.ItemHappyPlaceBinding
import com.pranay.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdapter(private var list:ArrayList<HappyPlaceModel>) : RecyclerView.Adapter<HappyPlacesAdapter.ViewHolder>() {

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

    }

    override fun getItemCount(): Int {
       return list.size
    }
}