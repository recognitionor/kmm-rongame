package com.jhlee.kmm_rongame.core.data

expect fun speakTextToSpeech(text: String)
expect fun isTextToSpeechSpeaking(): Boolean
expect fun stopTextToSpeech()