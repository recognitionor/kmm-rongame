package com.jhlee.kmm_rongame.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.jhlee.kmm_rongame.App
import com.jhlee.kmm_rongame.Firebase
import com.jhlee.kmm_rongame.FirebaseApp
import com.jhlee.kmm_rongame.FirebaseStorage
import com.jhlee.kmm_rongame.Greeting
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.initialize
import com.jhlee.kmm_rongame.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App(AppModule(LocalContext.current))
                }
            }
        }
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
