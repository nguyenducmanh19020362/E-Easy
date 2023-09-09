package com.example.mydictionary.screen.setting

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultReview(mergeItem: (Item) -> Unit){
    Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Bạn đã hoàn thành ôn tập",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { mergeItem(Item("Danh Sách Từ")) }) {
            Text(
                text = "Quay Lại",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}

@Composable
fun ResultTest(count: Int, total: Int, mergeItem: (Item) -> Unit){
    Column(modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center) {
        Text(
            text = "Điểm: $count/$total",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { mergeItem(Item("Danh Sách Từ")) }) {
            Text(
                text = "Quay Lại",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}