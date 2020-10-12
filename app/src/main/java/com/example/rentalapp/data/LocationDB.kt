package com.example.rentalapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Location::class), version = 1)
abstract class LocationDB: RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object{
        private var instance: LocationDB? = null
        suspend fun getInstance(context: Context): LocationDB{
            if (instance != null)
                return instance!!

            // build an instance
            instance = Room.databaseBuilder(context, LocationDB::class.java,
                "rentalapp").build()

//            initDB()

            return instance!!
        }

        suspend fun initDB(){
            instance?.clearAllTables() // add this line when you are still debugging
            //
//            SampleData.APARTMENT.forEach{instance?.apartmentDao()?.insert(it)}
        }
    }


}