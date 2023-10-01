package com.example.mydictionary.database.dao

import androidx.room.Dao
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

    @Query("SELECT * FROM word WHERE word LIKE '%' || :str || '%' GROUP BY word, mean")
    fun getWordsByChar(str: String): List<Word>

    /*@Delete
    suspend fun deleteWord(word:Word)*/
    @Query("UPDATE word SET id = :id, word = :word, type = :type, pronounce = :pronounce, mean = :mean," +
            " example = :example, image = :image, id_topic = :id_topic WHERE id = :id")
    fun updateWord(id: Int, word: String, type: String, pronounce: String, mean: String, example: String, image: String, id_topic: Int)
    @Query("DELETE FROM TopicWord WHERE (idTopic = :idTopic AND word = :word)")
    suspend fun deleteWord(idTopic: Int , word: String)

    @Query("DELETE FROM TopicWord")
    suspend fun deleteAllWord()

    @Query("DELETE FROM TopicWord WHERE (idTopic = :idTopic)")
    suspend fun deleteWordTopic(idTopic: Int)
}