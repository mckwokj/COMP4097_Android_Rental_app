package com.example.rentalapp.data

import androidx.room.*

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(location: Location)
    @Query("Select * from location")
    suspend fun findAllLocations(): List<Location>
    @Query("Select * from location where estate = :estate")
    suspend fun findLocationByEstate(estate: String): Location
    @Delete
    suspend fun delete(vararg location: Location)
    @Update
    suspend fun update(location: Location)
}