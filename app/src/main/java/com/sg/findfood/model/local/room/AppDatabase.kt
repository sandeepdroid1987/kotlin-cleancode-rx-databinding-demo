package com.sg.findfood.model.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * created by sandeep gupta on 17/3/19
 */

@Database(entities = [FavouriteVenue::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteVenueDao(): FavouriteVenueDao

    companion object {
        val DB_NAME = "appDB"
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
                }
            }
            return INSTANCE
        }

        //not used for now
        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}