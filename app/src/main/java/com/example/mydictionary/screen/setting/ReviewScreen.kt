package com.example.mydictionary.screen.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydictionary.R
import com.example.mydictionary.TextSpeech
import com.example.mydictionary.database.table.Word

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ReviewScreen(defaultList: MutableList<Word>, changeItem: (Item) -> Unit){
    var i by remember {
        mutableStateOf(0)
    }
    var iOld by remember {
        mutableStateOf(-1)
    }
    var max = defaultList.size * 3
    if(i != iOld) {
        if (i < max) {
            val point = (ListWord.list.indices).random()
            val word = ListWord.list[point]
            if (ListWord.waiting[word] == null) {
                ListWord.waiting[word] = mutableListOf(0, 1, 2)
            }
            val pType = (ListWord.waiting[word]!!.indices).random()
            val type = ListWord.waiting[word]!!.removeAt(pType)
            defaultList.remove(word)
            val word1 = defaultList[(defaultList.indices).random()]
            defaultList.remove(word1)
            val word2 = defaultList[(defaultList.indices).random()]
            defaultList.remove(word2)
            val word3 = defaultList[(defaultList.indices).random()]
            defaultList.remove(word3)
            val list = listOf(word, word1, word2, word3)
            defaultList.addAll(list)
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.weight(1f)) {
                    Question(type = type, word = word)
                }
                Row(modifier = Modifier.weight(1f)) {
                    if (type ==  2 || type == 1) {
                        Answers(listWord = list.toMutableList(), type = 1, word = word, {
                            i++
                            if (ListWord.waiting[word]!!.isEmpty()) {
                                ListWord.list.remove(word)
                            }
                        }, { max++
                            iOld--
                           }, {
                            ListWord.waiting[word]!!.addAll(
                                listOf(type, type)
                            )
                        })
                    } else {
                        Answers(listWord = list.toMutableList(), type = 0, word = word, {
                            i++
                            if (ListWord.waiting[word]!!.isEmpty()) {
                                ListWord.list.remove(word)
                            }
                        }, { max++
                             iOld--
                           }, {
                            ListWord.waiting[word]!!.addAll(
                                listOf(type, type)
                            )
                        })
                    }
                }
            }
        } else {
            Log.w("ReviewScreen", "ReviewScreen Completed Review")
            changeItem(Item("Completed Review"))
        }
    }
}

@Composable
fun Question(type: Int, word: Word){
    Log.w("Answers", word.word + " " + type)
    Row(modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        when(type){
            0 -> {
                Text(
                    text = "Chọn từ có nghĩa: " + word.mean,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 30.sp
                )
            }
            1 -> {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        text = "Chọn từ có phát âm: ",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.small_speaker),
                        contentDescription = "speaker",
                        modifier = Modifier
                            .layoutId("speaker")
                            .size(30.dp)
                            .clickable {
                                TextSpeech.speech?.speakOut(word.word)
                            }
                    )
                }
            }
            2 -> {
                Text(
                    text = "Từ " + word.word + " có nghĩa:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 30.sp
                )
            }
            3->{
                //Select word with Image
            }
        }
    }
}

@Composable
fun Answers(listWord: MutableList<Word>, type: Int, word: Word,
            changI: () -> Unit, changeMax:() -> Unit, addType: () -> Unit){
    val tmp1 = listWord.removeAt((listWord.indices).random())
    val tmp2 = listWord.removeAt((listWord.indices).random())
    val tmp3 = listWord.removeAt((listWord.indices).random())
    val tmp4 = listWord[0]
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(0xFF08b42e))
                .clickable {
                    if (tmp1.id == word.id) {
                        changI()
                    } else {
                        changeMax()
                        addType()
                    }
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if(type == 0){
                    Text(
                        text = tmp1.word,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }else{
                    Text(
                        text = tmp1.mean,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }
            }
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(0xFF08b42e))
                .clickable {
                    if (tmp2.id == word.id) {
                        changI()
                    } else {
                        changeMax()
                        addType()
                    }
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if(type == 0){
                    Text(
                        text = tmp2.word,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }else{
                    Text(
                        text = tmp2.mean,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }
            }
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(0xFF08b42e))
                .clickable {
                    if (tmp3.id == word.id) {
                        changI()
                    } else {
                        changeMax()
                        addType()
                    }
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if(type == 0){
                    Text(
                        text = tmp3.word,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }else{
                    Text(
                        text = tmp3.mean,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }
            }
            Row(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(Color(0xFF08b42e))
                .clickable {
                    if (tmp4.id == word.id) {
                        changI()
                    } else {
                        changeMax()
                        addType()
                    }
                },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if(type == 0){
                    Text(
                        text = tmp4.word,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }else{
                    Text(
                        text = tmp4.mean,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}