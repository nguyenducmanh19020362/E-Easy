package com.example.mydictionary.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import com.example.mydictionary.DataStore
import com.example.mydictionary.R
import com.example.mydictionary.ScreenSizes
import com.example.mydictionary.SizeElement
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.dao.TopicDao
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Topic
import com.example.mydictionary.viewmodels.HomeViewModel
import com.example.mydictionary.viewmodels.TopicViewModel
import com.example.mydictionary.viewmodels.WordViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, topicViewModel: TopicViewModel, wordViewModel: WordViewModel,
               folderDao: FolderDao, topicDao: TopicDao, wordDao: WordDao, topicWordDao: TopicWordDao, nav: NavController, data: DataStore
){

    var title by remember {
        mutableStateOf(Title("App", "My Dictionary"))
    }

    var folderSelected by remember {
        mutableStateOf(Folder(-1, ""))
    }

    var topicSelected by remember{
        mutableStateOf(Topic(-1, "", -1))
    }

    var state by remember { mutableStateOf(0) }
    var textPopUp by remember { mutableStateOf("")}

    val listFolder by homeViewModel.listFolder.collectAsState()
    val listTopic by topicViewModel.listTopic.collectAsState()

    val constraintsSet = ConstraintSet {
        val topBarHome= createRefFor("topBarHome")
        val botBarHome= createRefFor("botBarHome")
        val listFolders = createRefFor("body")
        val deFolder = createRefFor("deleteFolder")
        val addFolder = createRefFor("addFolder")
        val menu = createRefFor("menu")
        val setting = createRefFor("setting")

        constrain(topBarHome) {
            top.linkTo(parent.top)
        }

        constrain(botBarHome) {
            bottom.linkTo(parent.bottom)
        }

        constrain(listFolders){
            top.linkTo(topBarHome.bottom, margin = 50.dp)
            bottom.linkTo(addFolder.top)
        }

        constrain(addFolder){
            bottom.linkTo(botBarHome.top)
            start.linkTo(parent.start)
        }

        constrain(deFolder){
            bottom.linkTo(botBarHome.top)
            end.linkTo(parent.end)
        }
        constrain(menu){
            top.linkTo(parent.top, margin = 7.dp)
            end.linkTo(parent.end)
        }
        constrain(setting){
            top.linkTo(parent.top, margin = 10.dp)
            end.linkTo(parent.end)
        }
    }

    ConstraintLayout(
        constraintsSet,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row (modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .background(Color(0xFF08b42e))
            .layoutId("topBarHome"),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ){
            if(title.type != "App"){
                IconButton(
                    onClick = {
                        if(state == 0){
                            if(title.type == "Folder"){
                                title = Title("App", "My Dictionary")
                            }
                            if(title.type == "Topic"){
                                title = Title("Folder", folderSelected.folder_name)
                            }
                        }else{
                            state = 0;
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.size(30.dp),
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = title.name,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 30.sp
            )
        }

        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "hi",
            Modifier
                .layoutId("setting")
                .clickable {
                    state = 3
                }
        )

        /*if(title.type == "Topic"){
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "back",
                modifier = Modifier
                    .size(30.dp)
                    .layoutId("menu"),
                tint = Color.White
            )
        }*/

        Spacer(modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .background(Color(0xFF08b42e))
            .layoutId("botBarHome"))

        Box(modifier = Modifier.layoutId("body")) {
            if(state == 0){
                if(title.type == "App"){
                    homeViewModel.getAllFolders(folderDao)
                    textPopUp = ""
                    ListFolders(listFolder, {title = Title("Folder", it)}, {folderSelected = it})
                }
                if(title.type == "Folder"){
                    topicViewModel.getAllTopics(topicDao, folderSelected.id!!)
                    textPopUp = ""
                    ListTopics(listTopic, { title = Title("Topic", it) }, {topicSelected = it}, nav)
                }
            }
            if(state == 1){
                Log.e("Home", state.toString() + " " + title.type + "1")
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    if(title.type == "App"){
                        PopUpAddFolder({textPopUp = it}, { state = it },
                            textPopUp, homeViewModel, folderDao)
                    }
                    if(title.type == "Folder") {
                        PopUpAddTopic(
                            { textPopUp = it },
                            { state = it },
                            textPopUp,
                            topicViewModel,
                            topicDao,
                            folderSelected
                        )
                    }
                }
            }

            if(state == 2){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    if(title.type == "App"){
                        PopUpDeleteFolder(listFolder, {state = it}, homeViewModel, folderDao)
                    }
                    if(title.type == "Folder"){
                        PopUpDeleteTopic(listTopic, {state = it}, topicViewModel, topicDao, topicWordDao)
                    }
                }
            }

            if(state == 3){
                var flag by remember{
                    mutableStateOf(0)
                }
                var note by remember{
                    mutableStateOf("")
                }
                var stateSwitch by remember{
                    mutableStateOf(false)
                }
                var time by remember{
                    mutableStateOf("")
                }
                LaunchedEffect(key1 = null){
                    note = data.readNote()
                    stateSwitch = data.readState()
                    time = data.readTime()
                    flag = 1
                    Log.e("a", "$note $stateSwitch")
                }
                Log.e("b", "$note $stateSwitch")
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    if(flag == 1){
                        SetTimeScreen(stateSwitch, note, time, data) { state = 0 }
                    }
                }
            }
        }

        if(state == 0 && title.type != "Topic"){
            Button(
                onClick = { state = 1 },
                modifier = Modifier
                    .layoutId("addFolder")
                    .width((ScreenSizes.weight() / 2).dp)
                    .height(SizeElement.addAndDeButton)
                    .padding(10.dp),
                shape = RectangleShape,
                colors = ButtonDefaults
                    .buttonColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Text(
                    text = "Thêm",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
            Button(
                onClick = { state = 2 },
                modifier = Modifier
                    .layoutId("deleteFolder")
                    .width((ScreenSizes.weight() / 2).dp)
                    .height(SizeElement.addAndDeButton)
                    .padding(10.dp),
                shape = RectangleShape,
                colors = ButtonDefaults
                    .buttonColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Text(
                    text = "Xóa",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }

}

@Composable
fun Folders(nameFolders: String){
    Row(modifier = Modifier
        .width(((ScreenSizes.weight() * 3) / 4).dp)
        .height(65.dp)
        .clip(shape = RoundedCornerShape(20.dp))
        .background(Color(0xFF08b42e)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nameFolders,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 20.sp)
    }
}


