package com.jhlee.kmm_rongame.android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.jhlee.kmm_rongame.App
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.data.androidTextToSpeech
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        androidTextToSpeech = TextToSpeech(this, this)

        setContent {
            BackHandler {
                if (backKeyListener == null) {
                    finish()
                } else {
                    backKeyListener?.invoke()
                }
            }
            App(AppModule(LocalContext.current))
            ImageStorage.setContext(this)

        }
    }

    override fun onInit(status: Int) {
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
