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
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.viewmodels.HomeViewModel
import com.example.mydictionary.viewmodels.TopicViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

//Pop up to add folder
@Composable
fun PopUpAddFolder(onTextChange: (String) -> Unit, onState: (Int) -> Unit, text: String,
                homeViewModel: HomeViewModel, folderDao: FolderDao){

    val progressBar by homeViewModel.progressBar.collectAsState()
    if(progressBar == 1){
        CircularProgressIndicator()
    }
    Column(
        modifier = Modifier
            .width(((ScreenSizes.weight() * 3) / 4).dp)
            .padding(10.dp)
    ) {
        Text(
            text = "Nhập tên thư mục",
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
                        homeViewModel.setProgressBar()
                        homeViewModel.addFolder(folderDao, Folder(null, text))
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
fun PopUpDeleteFolder(listFolders: List<Folder>, onState: (Int) -> Unit,
                homeViewModel: HomeViewModel, folderDao: FolderDao){
    val tmp: MutableList<Boolean> = MutableList(listFolders.size){false}
    val checked = remember {
        tmp.toMutableStateList()
    }

    val progressBar by homeViewModel.progressBar.collectAsState()

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
            items(listFolders){ folder ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked[listFolders.indexOf(folder)],
                        onCheckedChange = { isChecked ->
                            checked[listFolders.indexOf(folder)] = isChecked
                            //Log.e("deletePopUp", checked.toString())
                        }
                    )
                    Text(
                        text = folder.folder_name,
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
                        homeViewModel.setProgressBar()
                        homeViewModel.deleteFolders(
                            folderDao,
                            homeViewModel.getDeletedFolders(listFolders, checked)
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
