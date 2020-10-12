package com.example.rentalapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Apartment(
    @PrimaryKey
    val id: Int,
    val property_title: String,
    val estate: String,
    val bedrooms: Int,
    val rent: Int,
    val expected_tenants: Int,
    val gross_area: Int,
    var occupied: Boolean,
    val latitude: Double?,
    val longitude: Double?,
    val image_URL: String
)
{
}