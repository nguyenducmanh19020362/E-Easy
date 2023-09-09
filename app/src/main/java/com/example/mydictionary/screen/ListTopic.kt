package com.example.mydictionary.screen

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mydictionary.ScreenSizes
import com.example.mydictionary.database.table.Topic
import com.example.mydictionary.screen.setting.Route

@Composable
fun ListTopics(listTopics: List<Topic>, changeTitle: (String) -> Unit,
               changeSelectedTopic: (Topic) -> Unit, nav: NavController){
    Box(
        modifier = Modifier
            .height(ScreenSizes.height().dp * 2 / 3)
    ) {
        var visibleIndex by remember { mutableStateOf(-1) }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            itemsIndexed(listTopics){index, topic ->
                AnimatedVisibility(
                    visible = index <= visibleIndex,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(modifier = Modifier.clickable {
                        /*changeTitle(topic.topic_name)
                        changeSelectedTopic(topic)*/
                        nav.navigate(Route.Words.route + "/${topic.id}/${topic.topic_name}"){
                            popUpTo(Route.Home.route){
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }) {
                        Folders(nameFolders = topic.topic_name)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        if (visibleIndex < listTopics.size - 1) {
            visibleIndex++
        }
    }
}