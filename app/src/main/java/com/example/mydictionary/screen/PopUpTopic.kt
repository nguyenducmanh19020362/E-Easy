package com.example.mydictionary.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mydictionary.ScreenSizes
import com.example.mydictionary.SizeElement
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.dao.TopicDao
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.database.table.Topic
import com.example.mydictionary.viewmodels.HomeViewModel
import com.example.mydictionary.viewmodels.TopicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Composable
fun PopUpAddTopic(onTextChange: (String) -> Unit, onState: (Int) -> Unit, text: String,
                   topicViewModel: TopicViewModel, topicDao: TopicDao, folder: Folder
){
    val progressBar by topicViewModel.progressBar.collectAsState()
    if(progressBar == 1){
        CircularProgressIndicator()
    }
    Column(
        modifier = Modifier
            .width(((ScreenSizes.weight() * 3) / 4).dp)
            .padding(10.dp)
    ) {
        Text(
            text = "Nhập tên chủ đề",
            fontWeight = FontWeight.Bold,
            fontSize = SizeElement.folderTextSize
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box{
            TextField(
                value = text,
                onValueChange = onTextChange,
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth(),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row{
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(SizeElement.sizeButtonOnPopUp)
                    .border(1.dp, Color.Black)
                    .clickable {
                        topicViewModel.setProgressBar()
                        topicViewModel.addTopic(topicDao, Topic(null, text, folder.id!!))
                        if (progressBar == 0) {
                            runBlocking {
                                delay(500)
                                onState(0)
                            }
                        }
                    },
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Thêm",
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(SizeElement.sizeButtonOnPopUp)
                    .border(1.dp, Color.Black)
                    .clickable {
                        onState(0)
                    },
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Hủy",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopUpDeleteTopic(listTopics: List<Topic>, onState: (Int) -> Unit,
                      topicViewModel: TopicViewModel, topicDao: TopicDao, topicWordDao: TopicWordDao
){
    val tmp: MutableList<Boolean> = MutableList(listTopics.size){false}
    val checked = remember {
        tmp.toMutableStateList()
    }

    val progressBar by topicViewModel.progressBar.collectAsState()

    if(progressBar == 1){
        CircularProgressIndicator()
    }

    Column(
        modifier = Modifier
            .width(((ScreenSizes.weight() * 3) / 4).dp)
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = "Chọn thư mục cần xóa:",
            fontWeight = FontWeight.Bold,
            fontSize = SizeElement.folderTextSize
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn (Modifier.height(ScreenSizes.height().dp * 1 / 2)){
            items(listTopics){ topic ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked[listTopics.indexOf(topic)],
                        onCheckedChange = { isChecked ->
                            checked[listTopics.indexOf(topic)] = isChecked
                            //Log.e("deletePopUp", checked.toString())
                        }
                    )
                    Text(
                        text = topic.topic_name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row{
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(SizeElement.sizeButtonOnPopUp)
                    .border(1.dp, Color.Black)
                    .clickable {
                        topicViewModel.setProgressBar()
                        topicViewModel.deleteTopics(
                            topicDao,
                            topicViewModel.getDeletedFolders(listTopics, checked),
                            topicWordDao
                        )
                        if (progressBar == 0) {
                            runBlocking {
                                delay(500)
                                onState(0)
                            }
                        }
                    },
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Xóa",
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(SizeElement.sizeButtonOnPopUp)
                    .border(1.dp, Color.Black)
                    .clickable {
                        onState(0)
                    },
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Hủy",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}