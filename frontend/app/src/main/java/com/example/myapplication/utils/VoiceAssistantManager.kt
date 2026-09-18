package com.example.myapplication.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceAssistantManager(private val context: Context) : TextToSpeech.OnInitListener, RecognitionListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var speechRecognizer: SpeechRecognizer? = null
    private var isTtsReady = false

    private var onSpeechResult: ((String) -> Unit)? = null
    private var onSpeechError: ((String) -> Unit)? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.ENGLISH)
            isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        } else {
            Log.e("VoiceAssistant", "TextToSpeech initialization failed.")
        }
    }

    fun speak(text: String, languageCode: String = "en") {
        if (!isTtsReady || tts == null) {
            Log.w("VoiceAssistant", "TTS not ready yet.")
            return
        }

        val locale = when (languageCode.lowercase()) {
            "hi" -> Locale("hi", "IN")
            "bn" -> Locale("bn", "IN")
            "as" -> Locale("as", "IN")
            else -> Locale("en", "IN")
        }

        val langResult = tts?.setLanguage(locale)
        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.setLanguage(Locale.ENGLISH)
        }

        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "MDoNER_TTS_ID")
    }

    fun stopSpeaking() {
        tts?.stop()
    }

    fun startListening(
        languageCode: String = "en",
        onResult: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Speech recognition is not available on this device.")
            return
        }

        this.onSpeechResult = onResult
        this.onSpeechError = onError

        stopListening()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(this@VoiceAssistantManager)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            val language = when (languageCode.lowercase()) {
                "hi" -> "hi-IN"
                "bn" -> "bn-IN"
                "as" -> "as-IN"
                else -> "en-IN"
            }
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }

        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    fun shutdown() {
        stopSpeaking()
        tts?.shutdown()
        tts = null
        stopListening()
    }

    // RecognitionListener Implementations
    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}

    override fun onError(error: Int) {
        val errorMessage = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No speech match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown speech error"
        }
        onSpeechError?.invoke(errorMessage)
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            onSpeechResult?.invoke(matches[0])
        } else {
            onSpeechError?.invoke("No voice input captured.")
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
}
