package com.example.mydictionary.viewmodels

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydictionary.database.dao.TopicDao
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Topic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopicViewModel: ViewModel() {
    private val _listTopic = MutableStateFlow(listOf<Topic>())
    val listTopic = _listTopic.asStateFlow()

    private val _progressBar = MutableStateFlow(0)
    val progressBar = _progressBar.asStateFlow()

    fun getAllTopics(topicDao: TopicDao, idFolder: Int){
        viewModelScope.launch (Dispatchers.IO){
            _listTopic.tryEmit(topicDao.getTopicsByFolder(idFolder))
        }
    }

    fun addTopic(topicDao: TopicDao, topic: Topic){
        viewModelScope.launch(Dispatchers.IO){
            topicDao.addTopic(topic)
            _progressBar.tryEmit(0)
        }
    }

    private fun deleteTopic(topicDao: TopicDao, topic: Topic, topicWordDao: TopicWordDao){
        viewModelScope.launch(Dispatchers.IO){
            topicDao.deleteTopic(topic)
            topicWordDao.deleteTopicWord(topic.id!!)
        }
    }

    fun deleteTopics(topicDao: TopicDao,  listTopics: List<Topic>, topicWordDao: TopicWordDao){
        for(topic in listTopics){
            deleteTopic(topicDao, topic, topicWordDao)
        }
        _progressBar.tryEmit(0)
    }

    fun setProgressBar(){
        viewModelScope.launch (Dispatchers.IO){
            _progressBar.emit(1)
        }
    }

    fun getDeletedFolders(listTopics: List<Topic>, checked: SnapshotStateList<Boolean>): List<Topic>{
        val list = mutableListOf<Topic>()
        for(folder in listTopics){
            if(checked[listTopics.indexOf(folder)]){
                list.add(folder)
            }
        }
        return list.toList()
    }
}