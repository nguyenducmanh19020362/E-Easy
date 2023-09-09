package com.example.mydictionary.screen.setting

import android.graphics.drawable.Icon
import android.net.Uri
import android.util.Log
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.mydictionary.R
import com.example.mydictionary.SizeElement
import com.example.mydictionary.database.dao.TopicWordDao
import com.example.mydictionary.database.dao.WordDao
import com.example.mydictionary.database.table.TopicWord
import com.example.mydictionary.database.table.Word
import com.example.mydictionary.screen.ShowWord
import com.example.mydictionary.viewmodels.WordViewModel

@Composable
fun AddWordScreen(wordViewModel: WordViewModel, wordDao: WordDao, idTopic: Int, topicWordDao: TopicWordDao){
    Log.e("", idTopic.toString())
    val listWordSearch by wordViewModel.wordSearch.collectAsState()
    
    var error by remember{
        mutableStateOf(0)
    }
    var textSearch by remember {
        mutableStateOf("")
    }
    var word by remember {
        mutableStateOf("")
    }
    var typeWord by remember {
        mutableStateOf("")
    }
    var mean by remember {
        mutableStateOf("")
    }
    var example by remember {
        mutableStateOf("")
    }
    var uri by rememberSaveable {
        mutableStateOf(Uri.EMPTY)
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            if (uriList.isNotEmpty()) {
                uri = uriList[0]
            }
        }
    
    val progress by wordViewModel.progressBar.collectAsState()
    
    if(progress == 2){
        AlertDialog(
            onDismissRequest = { wordViewModel.setProgressBar(0) },
            title = { Text(text = "Thông báo") },
            text = { Text(text = "Thêm từ thành công") },
            confirmButton = {
                TextButton(onClick = {
                    wordViewModel.setProgressBar(0)
                }) {
                    Text("OK")
                }
            }
        )
    }
    if(progress == 1){
        CircularProgressIndicator()
    }
    if(error == 1){
        AlertDialog(
            onDismissRequest = { error = 0 },
            title = { Text(text = "Thông báo") },
            text = { Text(text = "Thêm từ thất bại. Kiểm tra trường bắt buộc") },
            confirmButton = {
                TextButton(onClick = {
                    error = 0
                }) {
                    Text("OK")
                }
            }
        )
    }
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally){
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row (verticalAlignment = Alignment.CenterVertically){
                TextField(
                    value = textSearch,
                    onValueChange = {textSearch = it},
                    shape = RoundedCornerShape(20.dp),
                    trailingIcon = { androidx.compose.material3.Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search"
                    )}
                )
            }
            if(textSearch != ""){
                wordViewModel.search(wordDao, textSearch)
                if(listWordSearch.isNotEmpty()){
                    Column {
                        for (w in listWordSearch){
                            Spacer(modifier = Modifier.height(10.dp))
                            ItemWord(content = "${w.word}: ${w.mean}", topicWordDao, w, wordViewModel, idTopic) {
                                textSearch = ""
                            }
                        }
                    }
                }
            }else{
                Spacer(modifier = Modifier.height(10.dp))
                FieldInput(type = "Từ", text = word, changeText = {word = it}, flag = true)
                Spacer(modifier = Modifier.height(20.dp))
                FieldInput(type = "Loại từ", text = typeWord, changeText = {typeWord = it}, false)
                Spacer(modifier = Modifier.height(20.dp))
                FieldInput(type = "Nghĩa", text = mean, changeText = {mean = it}, true)
                Spacer(modifier = Modifier.height(20.dp))
                FieldInput(type = "Ví dụ", text = example, changeText = {example = it}, false)
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ){
                    Text(
                        text = "Ảnh minh họa",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(onClick = {
                        galleryLauncher.launch("image/*")
                    }){
                        Icon(
                            painter = painterResource(id = R.drawable.iamge),
                            contentDescription = null,
                        )
                    }
                }
                if(uri != Uri.EMPTY){
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = rememberAsyncImagePainter(
                            uri,
                            ImageLoader(context = LocalContext.current)
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "thêm ảnh",
                        modifier = Modifier
                            .padding(16.dp, 8.dp)
                            .width(SizeElement.widthImage)
                            .height(SizeElement.heightImage)
                            .clip(RectangleShape)
                            .border(1.5.dp, Color.Black, RectangleShape)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if(word == "" || mean == ""){
                            error = 1
                        }else{
                            wordViewModel.setProgressBar(1)
                            val newWord = Word(null, word, typeWord, "", mean, example, uri.toString(), idTopic)
                            wordViewModel.addWord(
                                wordDao,
                                topicWordDao,
                                newWord,
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF08b42e))
                ) {
                    Text(text = "Thêm")
                }
            }
        }
    }
}

@Composable
fun FieldInput(type: String, text: String, changeText: (String) -> Unit, flag: Boolean){
    Column(modifier = Modifier.padding(5.dp)) {
        Text(
            text = type,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = text,
            onValueChange = changeText,
            shape = RoundedCornerShape(20.dp),
        )
        if(flag){
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Trường bắt buộc",
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ItemWord(content: String, topicWordDao: TopicWordDao, w: Word, wordViewModel: WordViewModel, idTopic: Int, resetSearch: () -> Unit,){
    val constraints = ConstraintSet{
        val content1 = createRefFor("content")
        val button = createRefFor("button")

        constrain(content1) {
            start.linkTo(parent.start, margin = 10.dp)
        }
        
        constrain(button){
            end.linkTo(parent.end, margin = 10.dp)
        }
    }
    ConstraintLayout(constraints,
    modifier = Modifier.fillMaxSize()){
        Text(
            modifier = Modifier.layoutId("content"),
            text = content,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Icon(imageVector = Icons.Default.Add,
            contentDescription = "add",
            modifier = Modifier
                .layoutId("button")
                .clickable {
                    wordViewModel.addTopicWord(topicWordDao, TopicWord(null, idTopic, w.word))
                    wordViewModel.setProgressBar(1)
                    resetSearch()
                }
        )
    }
}
