package com.example.mydictionary

import android.content.Context
import android.util.Log
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.dao.TopicDao
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Topic
import com.example.mydictionary.database.table.TopicWord
import com.example.mydictionary.database.table.Word
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/*E/: 1 Cơ Bản
E/: 2 Toeic
E/: 3 Ielts*/


/*E/: 1 Introduce Yourself
E/: 2 Family
E/: 3 House
E/: 4 Things
E/: 5 Human Body
E/: 6 School
E/: 7 Sport
E/: 8 Job
E/: 9 Daily Routine
E/: 10 Traffic
E/: 11 Business
E/: 12 Employment
E/: 13 Technology
E/: 14 Travel
E/: 15 Environment
E/: 16 Health and fitness
E/: 17 Education
E/: 18 Entertainment
E/: 19 Education
E/: 20 Environment
E/: 21 Health
E/: 22 Technology
E/: 23 Society
E/: 24 Work and Career
E/: 25 Arts and Culture
E/: 26 Global Issue*/
suspend fun initData(wordDao: WordDao, folderDao: FolderDao, topicDao: TopicDao, topicWordDao: TopicWordDao, application: Context){
    coroutineScope {
        /*val list = topicDao.getTopicsByFolder(1)
        val list2 = topicDao.getTopicsByFolder(2)
        val list3 = topicDao.getTopicsByFolder(3)
        for(folder in list){
            Log.e("", ""+ folder.id + " " + folder.topic_name)
        }
        for(folder in list2){
            Log.e("", ""+ folder.id + " " + folder.topic_name)
        }
        for(folder in list3){
            Log.e("", ""+ folder.id + " " + folder.topic_name)
        }*/
        /*if(folderDao.getAllFolders().isEmpty()){
            folderDao.addFolder(Folder(null, "Cơ Bản"))
            folderDao.addFolder(Folder(null, "Toeic"))
            folderDao.addFolder(Folder(null, "Ielts"))
        }*/
        /*if(topicDao.getTopicsByFolder(2).isEmpty()){
            topicDao.addTopic(Topic(null, "Business", 2))
            topicDao.addTopic(Topic(null, "Employment", 2))
            topicDao.addTopic(Topic(null, "Technology", 2))
            topicDao.addTopic(Topic(null, "Travel", 2))
            topicDao.addTopic(Topic(null, "Environment", 2))
            topicDao.addTopic(Topic(null, "Health and fitness", 2))
            topicDao.addTopic(Topic(null, "Education", 2))
            topicDao.addTopic(Topic(null, "Entertainment", 2))
        }*/
        /*if(topicDao.getTopicsByFolder(3).isEmpty()){
            topicDao.addTopic(Topic(null, "Education", 3))
            topicDao.addTopic(Topic(null, "Environment", 3))
            topicDao.addTopic(Topic(null, "Health", 3))
            topicDao.addTopic(Topic(null, "Technology", 3))
            topicDao.addTopic(Topic(null, "Society", 3))
            topicDao.addTopic(Topic(null, "Work and Career", 3))
            topicDao.addTopic(Topic(null, "Arts and Culture", 3))
            topicDao.addTopic(Topic(null, "Global Issue", 3))
        }*/
        var i = 0
        var word: String = ""
        var type: String = ""
        var pronounce: String = ""
        var mean: String = ""
        var example: String = ""
        var image: String = ""
        var id_topic: Int = 1
        /*try {
            File("input.txt").useLines {
                    lines -> lines.forEach {
                if(i % 2 == 0){
                    val strs = it.split("-").toTypedArray()
                    val str1s = strs[0].split(" ").toTypedArray()
                    word = str1s[0].trim()
                    type = str1s[1].trim()
                    pronounce = strs[1].trim()
                    mean = strs[2].trim()
                }
                if(i % 2 == 1){
                    Log.e("", " i lẻ")
                    example = it.trim()
                    wordDao.addWord(Word(null, word, type, pronounce, mean, example, image, id_topic))
                    topicWordDao.addTopicWord(TopicWord(null, 1, word))
                }
                i += 1
            }
            }
        }catch (e: Exception){
            Log.e("", e.message.toString())
        }*/
        val file_name = "input.txt"
        val json_string = application.assets.open(file_name).bufferedReader().use{
            it.forEachLine {line ->
                /*if(i % 2 == 0){
                    val strs = line.split("-").toTypedArray()
                    val str1s = strs[0].split(" ").toTypedArray()
                    word = str1s[0].trim()
                    type = str1s[1].trim()  + " " + str1s[2].trim()
                    pronounce = strs[1].trim()
                    mean = strs[2].trim()
                }
                if(i % 2 == 1){
                    Log.e("", " i lẻ")
                    example = line.trim()
                    runBlocking {
                        wordDao.addWord(Word(null, word, type, pronounce, mean, example, image, id_topic))
                        topicWordDao.addTopicWord(TopicWord(null, 1, word))
                    }
                }
                i += 1*/
                val strs = line.split("-").toTypedArray()
                var w = ""
                var t = ""
                var flag = false
                for (i1 in strs[0].indices){
                    if(strs[0][i1] == '('){
                        flag = true
                    }
                    if(flag){
                        t += strs[0][i1]
                    }else{
                        w += strs[0][i1]
                    }
                }
                word = w
                type = t
                pronounce = strs[1].trim()
                mean = strs[2].trim()
                example = strs[3].trim()
                runBlocking {
                    wordDao.addWord(Word(null, word, type, pronounce, mean, example, image, id_topic))
                    topicWordDao.addTopicWord(TopicWord(null, id_topic, word))
                }
            }
        }
    }
}

suspend fun delete(wordDao: WordDao, folderDao: FolderDao, topicDao: TopicDao, topicWordDao: TopicWordDao){
    coroutineScope {
        wordDao.deleteWordTopic(1)
        topicWordDao.deleteTopicWord(1)
    }
}
