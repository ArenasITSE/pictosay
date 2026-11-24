
package com.example.pictosay.ui.communication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictosay.data.repository.PicToSayRepository

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val Background = Color(0xFFF7F9FC)

@Suppress("UNUSED_PARAMETER")
@Composable
fun CommunicationScreen(
    onBack: () -> Unit,
    onOpenEnvironments: () -> Unit,
    onSpeak: (String) -> Unit,
    repository: PicToSayRepository
) {

    var customText by remember {
        mutableStateOf("")
    }

    // Conserva los mensajes agregados para construir
    // una frase que podrá utilizarse posteriormente.
    var accumulatedPhrase by remember {
        mutableStateOf("")
    }

    var feedbackMessage by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(18.dp)
    ) {

        // ========================================
        // ENCABEZADO
        // ========================================

        TextButton(
            onClick = onBack
        ) {
            Text(
                text = "← Regresar",
                color = Blue
            )
        }

        Text(
            text = "Mi comunicación 💬",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            text = "Construye lo que quieres expresar.",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // ========================================
        // EXPLORAR AMBIENTES
        // ========================================

        Button(
            onClick = onOpenEnvironments,
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        ) {
            Text(
                text = "🏡 Explorar ambientes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ========================================
        // MENSAJE PERSONALIZADO
        // ========================================

        Text(
            text = "Escribe tu propio mensaje",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = customText,
            onValueChange = {
                customText = it
                feedbackMessage = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 90.dp),
            placeholder = {
                Text("Quiero decir...")
            },
            shape = RoundedCornerShape(20.dp),
            maxLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = DarkBlue,
                unfocusedTextColor = DarkBlue
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ========================================
        // BOTONES DE COMUNICACIÓN
        // ========================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // HABLAR
            Button(
                onClick = {
                    val message = customText.trim()

                    if (message.isNotBlank()) {
                        onSpeak(message)
                    }
                },
                enabled = customText.isNotBlank(),
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                )
            ) {
                Text(
                    text = "🔊 Hablar",
                    fontSize = 16.sp
                )
            }

            // AGREGAR A FRASE
            OutlinedButton(
                onClick = {
                    val message = customText.trim()

                    if (message.isNotBlank()) {

                        accumulatedPhrase = listOf(
                            accumulatedPhrase,
                            message
                        )
                            .filter { it.isNotBlank() }
                            .joinToString(" ")

                        customText = ""

                        feedbackMessage = "Mensaje agregado."
                    }
                },
                enabled = customText.isNotBlank(),
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = "Agregar a frase",
                    fontSize = 14.sp
                )
            }
        }

        // MENSAJE DE CONFIRMACIÓN
        if (feedbackMessage != null) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = feedbackMessage ?: "",
                color = Blue,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
