
package com.example.pictosay.ui.emotions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ========================================
// MODELO DE EMOCIÓN
// ========================================

private data class Emotion(
    val name: String,
    val emoji: String,
    val message: String
)

// ========================================
// LISTA DE EMOCIONES
// ========================================

private val emotions = listOf(
    Emotion("Feliz", "😄", "Estoy feliz"),
    Emotion("Triste", "😢", "Estoy triste"),
    Emotion("Enojado", "😡", "Estoy enojado"),
    Emotion("Asustado", "😨", "Tengo miedo"),
    Emotion("Cansado", "😴", "Estoy cansado"),
    Emotion("Tranquilo", "😌", "Estoy tranquilo"),
    Emotion("Emocionado", "🤩", "Estoy emocionado"),
    Emotion("Confundido", "😕", "No entiendo")
)

// ========================================
// COLORES
// ========================================

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val Background = Color(0xFFF7F9FC)
private val LightBlue = Color(0xFFE8F0FF)
private val LightYellow = Color(0xFFFFF2C9)

// ========================================
// PANTALLA DE EMOCIONES
// ========================================

@Composable
fun EmotionsScreen(
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onAddPhrase: (String) -> Unit
) {

    var selectedEmotion by remember {
        mutableStateOf<Emotion?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(18.dp)
    ) {

        // ====================================
        // ENCABEZADO
        // ====================================

        TextButton(
            onClick = onBack
        ) {
            Text(
                "← Regresar",
                color = Blue
            )
        }

        Text(
            "¿Cómo me siento? 😊",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            "Todas las emociones son importantes.",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(20.dp))

        // ====================================
        // CUADRÍCULA DE EMOCIONES
        // ====================================

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {

            items(emotions) { emotion ->

                val isSelected = selectedEmotion == emotion

                Card(
                    onClick = {

                        // Guardar emoción seleccionada
                        selectedEmotion = emotion

                        // Agregar frase al constructor
                        onAddPhrase(emotion.message)

                        // REPRODUCIR VOZ AUTOMÁTICAMENTE
                        onSpeak(emotion.message)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            LightBlue
                        } else {
                            Color.White
                        }
                    ),
                    border = if (isSelected) {
                        androidx.compose.foundation.BorderStroke(
                            2.dp,
                            Blue
                        )
                    } else {
                        null
                    },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            emotion.emoji,
                            fontSize = 52.sp
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            emotion.name,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium,
                            color = DarkBlue
                        )
                    }
                }
            }
        }

        // ====================================
        // EMOCIÓN SELECCIONADA
        // ====================================

        selectedEmotion?.let { emotion ->

            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LightYellow
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "${emotion.emoji} ${emotion.message}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        // Repetir mensaje
                        Button(
                            onClick = {
                                onSpeak(emotion.message)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue
                            )
                        ) {
                            Text("🔊 Escuchar")
                        }

                        // Agregar nuevamente al constructor
                        OutlinedButton(
                            onClick = {
                                onAddPhrase(emotion.message)
                            }
                        ) {
                            Text("Usar frase")
                        }
                    }
                }
            }
        }
    }
}
