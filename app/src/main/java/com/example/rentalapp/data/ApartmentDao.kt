package com.example.rentalapp.data

import androidx.room.*

@Dao
interface ApartmentDao {
    @Insert
    suspend fun insert(apartment: Apartment)
    @Query("Select * from apartment")
    suspend fun findAllApartments(): List<Apartment>
    @Query("Select * from apartment where id = :id")
    suspend fun findApartmentByID(id: Int): Apartment
    @Query("Select * from apartment where estate = :estate")
    suspend fun findApartmentsByEstateName(estate: String): List<Apartment>
    // changes needed for selection criteria
    @Query("Select * from apartment where bedrooms = :bedrooms")
    suspend fun findApartmentsByBedroomNum(bedrooms: Int): List<Apartment>
    @Query("Select distinct estate from apartment")
    suspend fun findAllEstateName(): List<String>
    @Delete
    suspend fun delete(vararg apartment: Apartment)
    @Update
    suspend fun update(apartment: Apartment)
}