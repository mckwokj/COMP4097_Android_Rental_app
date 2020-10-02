package com.example.rentalapp.data

data class Choice(
    val id: String,
    val title: String,
    val estate: String,
    val bedrooms: Int,
    val rent: Int,
    val tenants: Int,
    val area: Int
){
}