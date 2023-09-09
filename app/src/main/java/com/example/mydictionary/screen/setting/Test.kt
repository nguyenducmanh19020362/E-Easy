package com.example.mydictionary.screen.setting

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mydictionary.database.table.Word

@Composable
fun Test(defaultList: MutableList<Word>, changeItem: (Item) -> Unit, merge: (Int) -> Unit){
    var count by remember{
        mutableStateOf(0)
    }
    var i by remember {
        mutableStateOf(0)
    }
    val iOld by remember {
        mutableStateOf(-1)
    }
    val max = defaultList.size * 3
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
                    if (type == 2 || type == 1) {
                        Answers(listWord = list.toMutableList(), type = 1, word = word, {
                            i++;
                            if (ListWord.waiting[word]!!.isEmpty()) {
                                ListWord.list.remove(word)
                            }
                            count++
                        }, {
                            i++;
                            if (ListWord.waiting[word]!!.isEmpty()) {
                                ListWord.list.remove(word)
                            }
                        }, {})
                    } else {
                        Answers(listWord = list.toMutableList(), type = 0, word = word, {
                            i++
                            if (ListWord.waiting[word]!!.isEmpty()) {
                                ListWord.list.remove(word)
                            }
                            count++
                        }, {
                            i++;
                            if (ListWord.waiting[word]!!.isEmpty()) {
                                ListWord.list.remove(word)
                            }
                        }, {})
                    }
                }
            }
        } else {
            Log.w("ReviewScreen", "ReviewScreen Completed Review")
            merge(count)
            changeItem(Item("Completed Test"))
        }
    }
}