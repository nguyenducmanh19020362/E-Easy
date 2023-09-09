package com.example.mydictionary.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word (
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var word: String,
    var type: String,
    var pronounce: String,
    var mean: String,
    var example: String,
    var image: String,
    var id_topic: Int
)