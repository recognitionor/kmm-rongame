package com.jhlee.kmm_rongame.android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jhlee.kmm_rongame.App
import com.jhlee.kmm_rongame.core.data.androidTextToSpeech

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidTextToSpeech = TextToSpeech(this, this)

        setContent {
            App()
        }
    }

    override fun onInit(status: Int) {
    }
}