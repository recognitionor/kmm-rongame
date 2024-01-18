package com.jhlee.kmm_rongame.core.data

import platform.AVFAudio.AVSpeechBoundary
import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechSynthesizerDelegateProtocol
import platform.AVFAudio.AVSpeechUtterance
import platform.NaturalLanguage.NLLanguageRecognizer
import platform.darwin.NSObject

class TextToSpeechManager : NSObject(), AVSpeechSynthesizerDelegateProtocol {
    private var synthesizer: AVSpeechSynthesizer = AVSpeechSynthesizer()
    var isSpeaking = false

    init {
        synthesizer.delegate = this
    }

    fun speak(text: String) {
        isSpeaking = true
        synthesizer.delegate = this

        val utterance = AVSpeechUtterance.speechUtteranceWithString(text)
        utterance.voice = AVSpeechSynthesisVoice.voiceWithLanguage("en-US") // default to english

        // Detect language
        val recognizer = NLLanguageRecognizer()
        recognizer.processString(text)
        val language = recognizer.dominantLanguage
        language ?: run {
            utterance.voice = AVSpeechSynthesisVoice.voiceWithLanguage(language)
        }

        println("TextToSpeechManager speak: $text")
        synthesizer.speakUtterance(utterance)
    }

    override fun speechSynthesizer(
        synthesizer: AVSpeechSynthesizer,
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") //https://youtrack.jetbrains.com/issue/KT-43791/cocoapods-generated-code-with-same-parameter-types-and-order-but-different-names#focus=Comments-27-4574011.0-0
        didFinishSpeechUtterance: AVSpeechUtterance
    ) {
        isSpeaking = false
    }

    fun stopSpeaking() {
        synthesizer.stopSpeakingAtBoundary(AVSpeechBoundary.AVSpeechBoundaryImmediate)
    }

    fun isSpeaking(): Boolean {
        return isSpeaking
    }
}
// Implementation #1 - uses `TextToSpeechManager` natively in Kotlin
var textToSpeechManager: TextToSpeechManager = TextToSpeechManager()
actual fun speakTextToSpeech(text: String) {  // gives runtime error: [catalog] Unable to list voice folder
    textToSpeechManager.speak(text) // Cant use this from Kotlin due to unresolved Build Error
}
actual fun stopTextToSpeech() {
    textToSpeechManager.stopSpeaking()
}
actual fun isTextToSpeechSpeaking(): Boolean {
    return textToSpeechManager.isSpeaking
}