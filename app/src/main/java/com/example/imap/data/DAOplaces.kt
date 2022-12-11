package com.example.imap.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DAOplaces {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlace(places: Places)
    @Query("SELECT*FROM PLACES_TABLE ORDER BY ID ASC")
    fun readAllData():LiveData<List<Places>>

}