package com.example.mydictionary.viewmodels

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.TopicWord
import com.example.mydictionary.database.table.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WordViewModel: ViewModel() {
    private val _listWords = MutableStateFlow(listOf<Word>())
    val listWords = _listWords.asStateFlow()

    private val _wordSearch = MutableStateFlow(listOf<Word>())
    val wordSearch = _wordSearch.asStateFlow()

    private val _progressBar = MutableStateFlow(0)
    val progressBar = _progressBar.asStateFlow()

    fun getAllWordsByTopic(wordDao: WordDao, idWord: Int){
        viewModelScope.launch (Dispatchers.IO){
            _listWords.tryEmit(wordDao.getWordsByTopic(idWord))
            _progressBar.tryEmit(0)
        }
    }

    fun addWord(wordDao: WordDao, topicWordDao: TopicWordDao, word: Word){
        Log.e("a", "addWord")
        viewModelScope.launch(Dispatchers.IO){
            wordDao.addWord(word)
            topicWordDao.addTopicWord(TopicWord(null, word.id_topic, word.word))
            _progressBar.tryEmit(2)
        }
    }

    private fun deleteWord(wordDao: WordDao, word: Word, idTopic: Int){
        viewModelScope.launch(Dispatchers.IO){
            Log.e("", ""+ idTopic + " " + word.word)
            wordDao.deleteWord(idTopic, word.word)
        }
    }

    fun deleteWords(wordDao: WordDao, listWords: SnapshotStateMap<String, Word>, idTopic: Int){
        runBlocking {
            for(word in listWords){
                deleteWord(wordDao, word.value, idTopic)
            }
            _progressBar.tryEmit(0)
        }
    }

    fun setProgressBar(value: Int){
        viewModelScope.launch (Dispatchers.IO){
            _progressBar.emit(value)
        }
    }

    fun getDeletedWords(listWords: List<Word>, checked: SnapshotStateList<Boolean>): List<Word>{
        val list = mutableListOf<Word>()
        for(word in listWords){
            if(checked[listWords.indexOf(word)]){
                list.add(word)
            }
        }
        return list.toList()
    }

    fun search(wordDao: WordDao, text: String){
        viewModelScope.launch (Dispatchers.IO){
            _wordSearch.tryEmit(wordDao.getWordsByChar(text))
        }
    }

    fun addTopicWord(topicWordDao: TopicWordDao, topicWord: TopicWord){
        viewModelScope.launch(Dispatchers.IO) {
            topicWordDao.addTopicWord(topicWord)
            _progressBar.tryEmit(2)
        }
    }

    fun updateWord(wordDao: WordDao, word: Word){
        viewModelScope.launch (Dispatchers.IO){
            wordDao.updateWord(word.id!!, word.word, word.type, word.pronounce, word.mean, word.example, word.image, word.id_topic)
            _progressBar.tryEmit(2)
        }
    }
}