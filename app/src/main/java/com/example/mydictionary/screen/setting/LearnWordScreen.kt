package com.example.mydictionary.screen.setting

import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.size.Size
import com.example.mydictionary.*
import com.example.mydictionary.R
import com.example.mydictionary.database.table.Word

@Composable
fun LearnWordScreen(listWords: List<Word>){
    var point by remember {
        mutableStateOf(0)
    }
    Column (
        modifier = Modifier.padding(start = 5.dp, end = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ShowLearnWord(word = listWords[point])
        Row(Modifier.fillMaxWidth()){
            Row(Modifier.weight(1f)
                .clickable {
                    if(point > 0){
                        point--;
                    }
                },
                horizontalArrangement = Arrangement.End) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier
                        .background(Color(0xFF08b42e))
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Row(Modifier.weight(1f)
                .clickable {
                    if(point < listWords.size - 1){
                        point++;
                    }
                },
                horizontalArrangement = Arrangement.Start) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Arrow Forward",
                    modifier = Modifier
                        .background(Color(0xFF08b42e))
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun ShowLearnWord(word: Word){
    var state by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier
        .border(BorderStroke(2.dp, Color(0xFF08b42e)))
        .fillMaxWidth()
        .height((ScreenSizes.height() / 2 + 100).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        if(state == 0){
            Row {
                Text(
                    text = word.word + "  /" + word.type + "/",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 30.sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.small_speaker),
                    contentDescription = "speaker",
                    modifier = Modifier.layoutId("speaker")
                        .clickable {
                            TextSpeech.speech?.speakOut(word.word)
                        }
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = word.pronounce,
                color = Color.Black,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = word.example,
                color = Color.Black,
                fontSize = 25.sp
            )
        }else{
            Image(
                painter = rememberAsyncImagePainter(
                    Uri.parse(word.image),
                    ImageLoader(context = LocalContext.current)
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Ảnh",
                modifier = Modifier
                    .fillMaxWidth()
                    .height((ScreenSizes.height() / 2).dp)
                    .clip(RectangleShape)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = word.mean,
                color = Color.Black,
                fontSize = 25.sp
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Arrow Forward",
            modifier = Modifier.clickable { 
                state = 1 - state
            }
        )
    }
}