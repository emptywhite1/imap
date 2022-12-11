package com.example.imap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Places::class], version = 1, exportSchema = false)

abstract class PlacesDataBase :RoomDatabase(){
    abstract fun daoPlaces():DAOplaces
    companion object {
        @Volatile
        private var INSTANCE :PlacesDataBase?=null
        fun getDatabase(context:Context):PlacesDataBase{
            val tempInstance= INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this ){
                val instance=Room.databaseBuilder(
                    context.applicationContext,
                    PlacesDataBase::class.java,"places_database"
                )
                    .fallbackToDestructiveMigration() // this is only for testing,
                    //migration will be added for release
                    .allowMainThreadQueries() // this solved the issue but remember that its For testing purpose only
                    .build()
                INSTANCE=instance
                return instance

            }
        }
    }

}