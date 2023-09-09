package com.example.mydictionary.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mydictionary.database.table.Folder

@androidx.room.Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFolder(folder: Folder)

    @Query("SELECT * FROM folder")
    fun getAllFolders(): List<Folder>

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("DELETE FROM folder")
    fun deleteAll()
}