
package com.example.pictosay

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.example.pictosay.data.local.PicToSayDatabase
import com.example.pictosay.data.repository.PicToSayRepository
import com.example.pictosay.navigation.PicToSayNavigation
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var database: PicToSayDatabase
    private lateinit var repository: PicToSayRepository
    private var textToSpeech: TextToSpeech? = null
    private var ttsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Room
        database = PicToSayDatabase.getDatabase(applicationContext)

        // Inicializar repositorio
        repository = PicToSayRepository(
            database.picToSayDao()
        )

        // Inicializar voz en español de México
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(
                    Locale("es", "MX")
                )

                ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }

        // Mostrar aplicación
        setContent {
            MaterialTheme {
                Surface(
                    color = Color(0xFFF7F9FC)
                ) {
                    PicToSayNavigation(
                        repository = repository,
                        onSpeak = { message ->
                            speak(message)
                        }
                    )
                }
            }
        }
    }

    // Función disponible para reproducir mensajes
    private fun speak(text: String) {
        if (text.isNotBlank() && ttsReady) {
            textToSpeech?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "pictosay_message"
            )
        }
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        super.onDestroy()
    }
}
