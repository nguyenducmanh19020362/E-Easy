package com.example.mydictionary.screen

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.mydictionary.R
import com.example.mydictionary.ScreenSizes
import com.example.mydictionary.TextSpeech
import com.example.mydictionary.database.table.Word
import com.example.mydictionary.screen.setting.Item

@Composable
fun ListWord(listWords: List<Word>, changeItem: (Item) -> Unit, changeWord: (Word) -> Unit, changeState: (Boolean) -> Unit){
    Box(
        modifier = Modifier
            .height(ScreenSizes.height().dp * 2 / 3)
    ) {
        var visibleIndex by remember { mutableStateOf(-1) }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            itemsIndexed(listWords){index, word ->
                AnimatedVisibility(
                    visible = index <= visibleIndex,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {
                    Box(modifier = Modifier.clickable {
                    }) {
                        ShowWord(word = word, changeItem, changeWord, changeState)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        if (visibleIndex < listWords.size - 1) {
            visibleIndex++
        }
    }
}

@Composable
fun ShowWord(word: Word, changeItem: (Item) -> Unit, changeWord: (Word) -> Unit, changeState: (Boolean) -> Unit){
    val constraintsSet = ConstraintSet {
        val wordSet = createRefFor("word")
        val sound = createRefFor("sound")
        val speaker = createRefFor("speaker")
        val mean = createRefFor("mean")
        val modifier = createRefFor("modifier")

        constrain(wordSet) {
            top.linkTo(parent.top, margin = 5.dp)
            start.linkTo(parent.start, margin = 5.dp)
        }

        constrain(sound) {
            top.linkTo(wordSet.bottom, margin = 5.dp)
            start.linkTo(parent.start, margin = 5.dp)
        }

        constrain(speaker){
            top.linkTo(wordSet.bottom)
            end.linkTo(parent.end, margin = 50.dp)
        }

        constrain(modifier){
            top.linkTo(wordSet.bottom)
            end.linkTo(speaker.start, margin = 20.dp)
        }

        constrain(mean){
            top.linkTo(sound.bottom, margin = 5.dp)
            start.linkTo(parent.start, margin = 5.dp)
        }
    }

    ConstraintLayout(
        constraintsSet,
        Modifier
            .drawBehind {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 3.dp.toPx()
                )
            }
            .padding(3.dp)
    ) {
        Text(
            text = word.word + "   " + word.pronounce,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .layoutId("word")
                .fillMaxWidth()
        )

        Text(
            text = word.type,
            color = Color.Black,
            modifier = Modifier.layoutId("sound")
        )

        Icon(
            painter = painterResource(id = R.drawable.small_speaker),
            contentDescription = "speaker",
            modifier = Modifier.layoutId("speaker")
                .clickable {
                    TextSpeech.speech?.speakOut(word.word)
                }
        )
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "speaker",
            modifier = Modifier.layoutId("modifier")
                .clickable {
                    changeItem(Item("Thêm Từ"))
                    changeWord(word)
                    changeState(false)
                    TextSpeech.speech?.speakOut(word.word)
                }
        )
        Text(
            text = word.mean,
            modifier = Modifier.layoutId("mean")
        )
    }
}

/*
@Preview
@Composable
fun PreviewWord(){
    ShowWord(Word(1, "Hello", "N", "/helo/","Hello", "", "", 1))
}*/
