package com.example.rentalapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Apartment(
    @PrimaryKey
    val id: Int,
    val title: String,
    val estate: String,
    val bedrooms: Int,
    val rent: Int,
    val tenants: Int,
    val area: Int,
    var moveIn: Boolean,
    val latitude: Double,
    val longitude: Double,
    val img: String
)
{
}