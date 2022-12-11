package com.example.imap.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelPlaces(application: Application):AndroidViewModel(application) {
    private val readAllData:LiveData<List<Places>>
    private val ripo:RipoPlaces
    init {
        val placesDao=PlacesDataBase.getDatabase(application).daoPlaces()
        ripo= RipoPlaces(placesDao)
        readAllData=ripo.readAllData
    }
    fun addPlaces(places:Places){
        viewModelScope.launch (Dispatchers.IO){
            ripo.addPlaces(places)
        }
    }

}