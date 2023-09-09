package com.example.mydictionary.screen.setting


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.Word
import com.example.mydictionary.screen.ListWord
import com.example.mydictionary.viewmodels.WordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWord(topicTitle: String, wordViewModel: WordViewModel, wordDao: WordDao, idTopic: Int,
                    nav: NavController, topicWordDao: TopicWordDao){
    wordViewModel.getAllWordsByTopic(wordDao, idTopic)
    val listWords by wordViewModel.listWords.collectAsState()
    val progress by wordViewModel.progressBar.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = ListItems.list
    var selectedItem by remember { mutableStateOf(items[0]) }
    var count by remember { mutableStateOf(0) }
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                Spacer(Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(Color(0xFF08b42e)),
                        label = { Text(item.nameItem) },
                        selected = item == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = item
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            },
        ){
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                TopBar(topicTitle, drawerState, scope)
                Column(Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    when (selectedItem.nameItem) {
                        "Ôn Tập" -> {
                            wordViewModel.setProgressBar(1)
                            wordViewModel.getAllWordsByTopic(wordDao, idTopic)
                            if(progress == 1){
                                CircularProgressIndicator()
                            }
                            if(progress == 0 && listWords.size >= 4) {
                                ListWord.list = listWords.toMutableList()
                                ListWord.waiting = mutableMapOf()
                                ReviewScreen(listWords.toMutableList(), changeItem = {selectedItem = it})
                            }
                        }
                        "Kiểm Tra" -> {
                            wordViewModel.setProgressBar(1)
                            wordViewModel.getAllWordsByTopic(wordDao, idTopic)
                            if(progress == 1){
                                CircularProgressIndicator()
                            }
                            if(progress == 0 && listWords.size >= 4) {
                                ListWord.list = listWords.toMutableList()
                                ListWord.waiting = mutableMapOf()
                                Test(defaultList = listWords.toMutableList(), changeItem = {selectedItem = it}, merge = {count = it})
                            }
                        }
                        "Học" -> {
                            wordViewModel.setProgressBar(1)
                            wordViewModel.getAllWordsByTopic(wordDao, idTopic)
                            if(progress == 1){
                                CircularProgressIndicator()
                            }
                            if(progress == 0 && listWords.isNotEmpty()){
                                LearnWordScreen(listWords = listWords)
                            }
                        }
                        "Thêm Từ" -> {
                            AddWordScreen(wordViewModel, wordDao, idTopic, topicWordDao)
                        }
                        "Xóa Nhiều Từ" -> {
                            DeleteWordScreen(listWords = listWords, wordViewModel, wordDao, idTopic) {
                                selectedItem = it
                            }
                        }
                        "Trang Chủ" -> {
                            nav.navigate(Route.Home.route){
                                popUpTo(Route.Words.route){
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                        "Completed Review" -> {
                            ResultReview { selectedItem = it }
                        }
                        "Completed Test" -> {
                            ResultTest(count = count, total = listWords.size * 3){selectedItem = it}
                        }
                        else -> {
                            wordViewModel.setProgressBar(1)
                            wordViewModel.getAllWordsByTopic(wordDao, idTopic)
                            if(progress == 1){
                                CircularProgressIndicator()
                            }
                            if(progress == 0) {
                                ListWord(listWords = listWords)
                            }
                        }
                    }
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(topicTitle: String, stateDrawer: DrawerState, scope: CoroutineScope ){
        SmallTopAppBar(
            title = {
                Text(
                    text = topicTitle,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 30.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { stateDrawer.open() }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF08b42e))
        )
}

object ListWord{
    var list = mutableListOf<Word>()
    var waiting = mutableMapOf<Word, MutableList<Int>>()
}
