package com.example.mydictionary.screen.setting

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.Word
import com.example.mydictionary.viewmodels.WordViewModel

@Composable
fun DeleteWordScreen(listWords: List<Word>, wordViewModel: WordViewModel,
                     wordDao: WordDao, idTopic: Int, changeItem:(Item) -> Unit){
    val tmp = remember {
        mutableStateMapOf<String, Word>()
    }
    val progress by wordViewModel.progressBar.collectAsState()
     Column {
         LazyColumn(Modifier.weight(9f)){
             items(listWords){
                 DeleteWord(
                     checkBox = tmp[it.word] != null,
                     changeCheckBox = {
                         if(tmp[it.word] != null){
                             tmp.remove(it.word)
                         }else{
                             tmp[it.word] = it
                         }
                     },
                     word = it
                 )
                 Spacer(modifier = Modifier.height(10.dp))
             }
         }
         Row(
             Modifier
                 .weight(1f)
                 .fillMaxWidth(),
             horizontalArrangement = Arrangement.Center
         ) {
             Button(
                 onClick = {
                     wordViewModel.setProgressBar(1)
                     wordViewModel.deleteWords(wordDao, tmp, idTopic)
                     if(progress == 0){
                         changeItem(Item("Danh Sách Từ"))
                     }
                 },
                 colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF08b42e))
             ) {
                 Text(text = "Xóa")
             }
         }
     }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteWord(checkBox: Boolean, changeCheckBox: () -> Unit, word: Word){

    Row (verticalAlignment = Alignment.CenterVertically){
        Checkbox(checked = checkBox, onCheckedChange = {changeCheckBox()})
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = word.word + ": " + word.mean)
    }


}

/*
@Composable
@Preview
fun PreviewDeleteWordScreen(){
    DeleteWordScreen(listWords = listOf(Word(1, "Hello", "N", "", "Xin chào", "", "", 2)))
}*/
