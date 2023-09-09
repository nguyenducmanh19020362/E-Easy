package com.example.mydictionary.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TopicWord")
data class TopicWord (
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var idTopic: Int?,
    var word: String?
)