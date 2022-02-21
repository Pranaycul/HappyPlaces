package com.pranay.happyplaces.models

data class HappyPlaceModel(
    val id: Int,
    val title: String,
    val image: Int,
    val description: String,
    val location: String,
    val date: String,
    val latitude: Double,
    val longitude: Double
)
