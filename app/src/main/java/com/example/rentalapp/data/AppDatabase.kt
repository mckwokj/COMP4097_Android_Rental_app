package com.example.rentalapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Apartment::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
   abstract fun apartmentDao(): ApartmentDao
    companion object{
        private var instance: AppDatabase? = null
        suspend fun getInstace(context: Context): AppDatabase{
            if (instance != null)
                return instance!!

            // build an instance
            instance = Room.databaseBuilder(context, AppDatabase::class.java,
                "rentalapp").build()

            initDB()

            return instance!!
        }

        suspend fun initDB(){
            instance?.clearAllTables() // add this line when you are still debugging
            //
            SampleData.APARTMENT.forEach{instance?.apartmentDao()?.insert(it)}
        }
    }
}