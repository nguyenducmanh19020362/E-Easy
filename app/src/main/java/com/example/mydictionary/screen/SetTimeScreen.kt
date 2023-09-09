package com.example.mydictionary.screen

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.*
import com.example.mydictionary.DataStore
import com.example.mydictionary.R
import com.example.mydictionary.ui.theme.Teal200
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SetTimeScreen(state: Boolean, note: String, time: String, data: DataStore, changeState: () -> Unit){
    var switchOn by remember {
        mutableStateOf(state)
    }
    val mTime = remember { mutableStateOf(time) }
    var hour = 0
    var minute = 0
    val timeDelay = remember{ mutableStateOf(0L) }
    val mContext = LocalContext.current
    var text by remember {
        mutableStateOf(note)
    }
    
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bật/Tắt nhắc nhở: ",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 20.sp
            )
            Switch(
                checked = switchOn,
                onCheckedChange = { flag ->
                    Log.e("a", mTime.value + " " + timeDelay.toString())
                    if(!flag){
                        cancelWork(mContext)
                        GlobalScope.launch(Dispatchers.IO) {
                            data.note(false, text, mTime.value)
                        }
                    }else if(mTime.value != ""){
                        GlobalScope.launch(Dispatchers.IO) {
                            data.note(true, text, mTime.value)
                        }
                        createWorkDelay(timeDelay.value, mContext, text)
                    }
                    switchOn = flag
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Teal200,
                    checkedTrackColor = Teal200
                )
            )
        }
        Text(
            text = "Mục đích học Tiếng Anh của bạn",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            shape = RoundedCornerShape(20.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Bạn muốn học Tiếng Anh khi nào",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 20.sp
        )
        IconButton(onClick = {
            var mCalendar = Calendar.getInstance()
            var mHour = mCalendar[Calendar.HOUR_OF_DAY]
            var mMinute = mCalendar[Calendar.MINUTE]
            val mTimePickerDialog = TimePickerDialog(
                mContext,
                {_, mHour2 : Int, mMinute2: Int ->
                    run {
                        mCalendar = Calendar.getInstance()
                        mHour = mCalendar[Calendar.HOUR_OF_DAY]
                        mMinute = mCalendar[Calendar.MINUTE]
                        hour = mHour2
                        minute = mMinute2
                        timeDelay.value = if((hour > mHour) || (mHour == hour && minute > mMinute)){
                            ((hour - mHour) * 60 + minute  - mMinute).toLong()
                        }else{
                            ((24 - mHour + hour) * 60 + (60 - mMinute + minute)).toLong()
                        }
                        mTime.value = "$mHour2:$mMinute2"
                        Log.e("a", timeDelay.value.toString())
                    }
                }, mHour, mMinute, false
            )
            mTimePickerDialog.show()
        }){
            Icon(
                painter = painterResource(id = R.drawable.iamge),
                contentDescription = null,
            )
        }
        
        if(mTime.value != ""){
            Text(
                text = mTime.value,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        /*Row{
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .width(100.dp),
                onClick = {
                    if(mTime.value != ""){
                        changeState()
                        //createWorkDelay(timeDelay, mContext)
                    }
                }
            ) {
                Text(
                    text = "Bật",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .width(100.dp),
                onClick = {
                    changeState()
                    cancelWork(mContext)
                }
            ) {
                Text(
                    text = "Tắt",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }*/
        IconButton(
            onClick = { changeState() },
            Modifier
                .clip(CircleShape)
                .background(Color(0xFF08b42e)),
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

class MyWorker(appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {

    override fun doWork(): Result {
        Log.e("MyWorker", "Hello from MyWorker!")
        val note = inputData.getString("note")
        createNotification(applicationContext, note.toString())
        return Result.success()
    }
}

fun createWorkDelay(timeDelay: Long, context: Context, note: String){
    Log.e("a", "$timeDelay $note")
    val data = Data.Builder()
    data.putString("note", note)
    val myWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(
        15, TimeUnit.MINUTES
    ).setInputData(data.build())
        .setInitialDelay(timeDelay, TimeUnit.MINUTES).build()
    WorkManager.getInstance(context).enqueue(myWorkRequest)
}

fun cancelWork(context: Context){
    WorkManager.getInstance(context).cancelAllWork()
}