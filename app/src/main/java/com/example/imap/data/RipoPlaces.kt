package com.example.imap.data

import androidx.lifecycle.LiveData

class RipoPlaces(private val daoPlaces: DAOplaces) {
    val readAllData:LiveData<List<Places>> = daoPlaces.readAllData()
    suspend fun addPlaces(places: Places){
        daoPlaces.addPlace(places)
    }
}