package com.example.mydictionary

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*
import kotlin.coroutines.coroutineContext

class Speech(context: Context): TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    init {
        tts = TextToSpeech(context, this)
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            Log.e("a", "status success")
            val result = tts!!.setLanguage(Locale.US)
            tts!!.setSpeechRate(1.0f)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "error")
            }
        }
    }
    fun speakOut(text: String) {
        if(tts == null){
            Log.e("a", "speakOut")
        }
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }
    fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
    }
}

object TextSpeech{
    var speech: Speech? = null
}