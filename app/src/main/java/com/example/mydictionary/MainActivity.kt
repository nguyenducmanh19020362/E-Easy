package com.example.mydictionary

import android.content.Context
import android.icu.text.CaseMap.Fold
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.mydictionary.database.MyDictionaryDatabase
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Word
import com.example.mydictionary.screen.HomeScreen
import com.example.mydictionary.screen.ShowWord
import com.example.mydictionary.screen.setting.NavigationMenu
import com.example.mydictionary.screen.setting.ScreenWord
import com.example.mydictionary.ui.theme.MyDictionaryTheme
import com.example.mydictionary.viewmodels.HomeViewModel
import com.example.mydictionary.viewmodels.TopicViewModel
import com.example.mydictionary.viewmodels.WordViewModel

class MainActivity : ComponentActivity() {
    private lateinit var myDictionaryDatabase: MyDictionaryDatabase
    private val homeViewModel: HomeViewModel by viewModels()
    private val topicViewModel: TopicViewModel by viewModels()
    private val wordViewModel: WordViewModel by viewModels()
    companion object{
        private val Context.dataStore by preferencesDataStore(
            name = "note"
        )
    }
    private val data: DataStore by lazy {
        DataStore(
            dataStore
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDictionaryDatabase = MyDictionaryDatabase.getInstance(applicationContext)
        if(TextSpeech.speech ==  null){
            TextSpeech.speech = Speech(applicationContext)
        }
        setContent {
            MyDictionaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val folderDao = myDictionaryDatabase.folderDao()
                    val topicDao = myDictionaryDatabase.topicDao()
                    val wordDao = myDictionaryDatabase.wordDao()
                    val topicWordDao = myDictionaryDatabase.topicWordDao()
                    NavigationMenu(
                        homeViewModel = homeViewModel,
                        topicViewModel = topicViewModel,
                        wordViewModel = wordViewModel,
                        folderDao = folderDao,
                        topicDao = topicDao,
                        wordDao = wordDao,
                        topicWordDao = topicWordDao,
                        data
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, homeViewModel: HomeViewModel) {
    val listFolder by homeViewModel.listFolder.collectAsState()
    for(item in listFolder){
        Log.e("MainActivity", item.toString())
    }
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyDictionaryTheme {
        //Greeting("Android", )
    }
}