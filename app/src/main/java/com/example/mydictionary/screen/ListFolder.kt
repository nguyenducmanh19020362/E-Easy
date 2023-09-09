package com.example.mydictionary.screen

import android.util.Log
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
import com.example.mydictionary.ScreenSizes
import com.example.mydictionary.database.dao.FolderDao
import com.example.mydictionary.database.table.Folder
import com.example.mydictionary.viewmodels.HomeViewModel

@Composable
fun ListFolders(listFolder: List<Folder>, changeTitle: (String) -> Unit, changeFolder: (Folder) -> Unit){
    Box(
        modifier = Modifier
            .height(ScreenSizes.height().dp * 2 / 3)
    ) {
        var visibleIndex by remember { mutableStateOf(-1) }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            itemsIndexed(listFolder){index, folder ->
                AnimatedVisibility(
                    visible = index <= visibleIndex,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(modifier = Modifier.clickable {
                        changeTitle(folder.folder_name)
                        changeFolder(folder)
                    }) {
                        Folders(nameFolders = folder.folder_name)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        if (visibleIndex < listFolder.size - 1) {
            visibleIndex++
        }
    }
}