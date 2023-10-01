package com.example.mydictionary.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mydictionary.database.table.TopicWord

@Dao
interface TopicWordDao {
    @Insert
    suspend fun addTopicWord(topicWord: TopicWord)

    @Query("DELETE FROM TopicWord WHERE idTopic = :idTopic")
    suspend fun deleteTopicWord(idTopic: Int)

    @Query("DELETE FROM TopicWord")
    suspend fun deleteAllTopicWord()
}