package com.example.mydictionary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mydictionary.database.table.Word

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Query("SELECT * FROM word WHERE word IN (SELECT word FROM TopicWord WHERE idTopic = :id)")
    fun getWordsByTopic(id: Int): List<Word>

    @Query("SELECT * FROM word WHERE word LIKE '%' || :str || '%'")
    fun getWordsByChar(str: String): List<Word>

    /*@Delete
    suspend fun deleteWord(word:Word)*/

    @Query("DELETE FROM TopicWord WHERE (idTopic = :idTopic AND word = :word)")
    suspend fun deleteWord(idTopic: Int , word: String)
}