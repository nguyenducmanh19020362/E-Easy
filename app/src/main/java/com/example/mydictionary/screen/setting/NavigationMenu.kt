package com.example.mydictionary.screen.setting

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mydictionary.DataStore
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.dao.TopicDao
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.Word
import com.example.mydictionary.screen.HomeScreen
import com.example.mydictionary.screen.ListWord
import com.example.mydictionary.viewmodels.HomeViewModel
import com.example.mydictionary.viewmodels.TopicViewModel
import com.example.mydictionary.viewmodels.WordViewModel

@Composable
fun NavigationMenu(homeViewModel: HomeViewModel, topicViewModel: TopicViewModel, wordViewModel: WordViewModel,
                        folderDao: FolderDao, topicDao: TopicDao, wordDao: WordDao, topicWordDao: TopicWordDao,
                        data: DataStore){
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Route.Home.route){
        composable(Route.Home.route){
            HomeScreen(
                homeViewModel = homeViewModel,
                topicViewModel = topicViewModel,
                wordViewModel = wordViewModel,
                folderDao = folderDao,
                topicDao = topicDao,
                wordDao = wordDao ,
                topicWordDao,
                nav,
                data
            )
        }
        composable(Route.Words.route + "/{id}/{name}"){navBackStackEntry->
            val name = navBackStackEntry.arguments?.getString("name")
            val id = navBackStackEntry.arguments?.getString("id")
            id?.let { ScreenWord(topicTitle = name.toString(), wordViewModel = wordViewModel, wordDao = wordDao,
                idTopic = id.toInt(), nav = nav, topicWordDao) }
        }
    }
}
