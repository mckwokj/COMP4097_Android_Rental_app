package com.example.rentalapp.data

import java.net.HttpCookie

data class User(
    val id: Int,
    val username: String,
    val avatar: String,
    var cookie: String
) {
}