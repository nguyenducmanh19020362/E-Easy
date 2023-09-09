package com.example.mydictionary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Topic

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTopic(topic: Topic)

    @Query("SELECT * FROM topic WHERE id_folder = :id")
    fun getTopicsByFolder(id: Int): List<Topic>

    @Delete
    suspend fun deleteTopic(topic: Topic)
}