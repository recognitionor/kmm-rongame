package com.jhlee.kmm_rongame.android

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.jhlee.kmm_rongame.App
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.data.androidTextToSpeech
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.ui.theme.BackgroundLight

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("jhlee", "onCreate")
        androidTextToSpeech = TextToSpeech(this, this)

        setContent {
            BackHandler {
                if (backKeyListener == null) {
                    finish()
                } else {
                    backKeyListener?.invoke()
                }
            }
            // targetSdk 36 부터는 edge-to-edge 가 강제되어 시스템 바 영역까지 앱이 그린다.
            // MainScreen 은 iOS 와 공유되고 바 높이를 하드코딩하므로 여기서 인셋을 소비한다.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundLight)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                App(AppModule(LocalContext.current))
            }
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
