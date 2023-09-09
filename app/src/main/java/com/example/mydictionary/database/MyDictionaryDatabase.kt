package com.example.mydictionary.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.dao.TopicDao
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Topic
import com.example.mydictionary.database.table.TopicWord
import com.example.mydictionary.database.table.Word
import java.io.*

@Database(entities = [(Folder::class), (Topic::class), (Word::class), (TopicWord::class)], version = 8)
abstract class MyDictionaryDatabase: RoomDatabase(){
    abstract fun folderDao(): FolderDao
    abstract fun topicDao(): TopicDao
    abstract fun wordDao(): WordDao
    abstract fun topicWordDao(): TopicWordDao

    companion object {
        @Volatile
        private var INSTANCE: MyDictionaryDatabase? = null
        private var DATABASE_NAME = "dictionary_database"
        fun getInstance(context: Context): MyDictionaryDatabase {
            // only one thread of execution at a time can enter this block of code
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    createDataBase(context)
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDictionaryDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }

        @Throws(IOException::class)
        fun copyDataBase(context: Context) {
            try {
                val myInput: InputStream = context.assets.open(DATABASE_NAME)
                context.getDatabasePath(DATABASE_NAME).createNewFile()
                val outputFileName = context.getDatabasePath(DATABASE_NAME)
                val myOutput: OutputStream = FileOutputStream(outputFileName)
                val buffer = ByteArray(1024)
                var length: Int
                while (myInput.read(buffer).also { length = it } > 0) {
                    myOutput.write(buffer, 0, length)
                }
                myOutput.flush()
                myOutput.close()
                myInput.close()
            } catch (e: Exception) {
                Log.e("tle99 - copyDatabase", e.message!!)
            }
        }

        private fun checkDataBase(context: Context): Boolean {
            val databasePath: File = context.getDatabasePath(DATABASE_NAME)
            return databasePath.exists()
        }

        private fun createDataBase(context: Context) {
            val dbExist = checkDataBase(context)
            if (dbExist) {
            } else {
                try {
                    copyDataBase(context)
                } catch (e: IOException) {
                    Log.e("tle99 - create", e.message!!)
                }
            }
        }
    }
}