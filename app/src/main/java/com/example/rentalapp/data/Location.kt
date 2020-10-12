package com.example.rentalapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey
    val estate: String,
    val latitude: Double,
    val longitude: Double
)
{}