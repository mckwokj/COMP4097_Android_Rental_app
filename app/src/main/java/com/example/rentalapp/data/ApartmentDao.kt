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
    @Query("Select * from apartment where bedrooms < :bedrooms")
    suspend fun findApartmentsLTBedroomNum(bedrooms: Int): List<Apartment>
    @Query("Select * from apartment where bedrooms > :bedrooms")
    suspend fun findApartmentsGTBedroomNum(bedrooms: Int): List<Apartment>
    @Query("Select distinct estate from apartment")
    suspend fun findAllEstateName(): List<String>
    @Query("Update apartment set occupied = :occupied where id = :id")
    suspend fun updateOccupiedByID(id: Int, occupied: Boolean)
    @Query("Update apartment set latitude = :latitude, longitude = :longitude where id=:id")
    suspend fun updateLatLong(id: Int, latitude: Double, longitude: Double)
//    @Query("Select latitude, longitude from apartment where id = :id")
//    suspend fun getLatLongById(id: Int): List<Double>
    @Delete
    suspend fun delete(vararg apartment: Apartment)
    @Update
    suspend fun update(apartment: Apartment)
}