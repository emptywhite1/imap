package com.example.imap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "places_table")
data class Places(
    @PrimaryKey (autoGenerate = true)
    val id:Int,
    val typePlaces:String,
    val name:String,
    val adder:String,
    val v: Double,
    val v1:Double,

)

