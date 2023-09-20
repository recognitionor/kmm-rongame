package com.jhlee.kmm_rongame

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    var size by remember { mutableStateOf(0) }
    var str by remember { mutableStateOf("") }
    var sb  by remember { mutableStateOf(StringBuilder()) }
    val storage = Firebase.storage(Firebase.app)
    Column {
        Button(onClick = {
            coroutineScope.launch {
                // suspend 함수 호출
//                size = storage.reference.listAll().items.size
//                str = str.append(storage.reference.path)
                storage.reference.listAll().items.forEach {
                    size += 1
                    str = it.getDownloadUrl()
                }

            }
        }) {
            Text("$size")
        }
        Text(text = str.toString())
    }

//    val fb = Firebase.initialize(
//        Unit, FirebaseOptions(
//            applicationId = "1:98686887957:ios:f63e9489d678a92e66061c",
//            apiKey = "AIzaSyBfke_QJmGGukiG-h8Snp_1LZvofBxtbBk",
//            storageBucket = "kmm-rongame.appspot.com",
//            projectId = "kmm-rongame"
//        ), "iosApp"
//    )

    storage.reference.name
//
//    val test = Firebase.storage.reference.bucket
    Text(text = "${storage.reference.name}")
    GlobalScope.launch {

    }
}