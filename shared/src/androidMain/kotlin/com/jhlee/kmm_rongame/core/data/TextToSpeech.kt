package com.jhlee.kmm_rongame.core.data

import android.speech.tts.TextToSpeech

var androidTextToSpeech: TextToSpeech? = null  // Android specific TextToSpeech
var unspokenText: String? = null // used to speak text in chunks
actual fun speakTextToSpeech(text: String) {
    if (!isTextToSpeechSpeaking()) {
        androidTextToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "freds_markers")
    }
}

actual fun isTextToSpeechSpeaking(): Boolean {
    return androidTextToSpeech?.isSpeaking ?: false
}

actual fun stopTextToSpeech() {
    androidTextToSpeech?.stop()
    unspokenText = ""
}
