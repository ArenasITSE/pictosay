
package com.example.pictosay.ui.communication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)

@Composable
fun CommunicationScreen(
    onBack: () -> Unit,
    onOpenEnvironments: () -> Unit,
    onSpeak: (String) -> Unit
) {
    var sentence by remember { mutableStateOf("") }
    var customText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(18.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Regresar", color = Blue)
        }

        Text(
            "Mi comunicación 💬",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            "Construye lo que quieres expresar.",
            color = Color.Gray
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    "Mi frase",
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    sentence.ifBlank { "Selecciona pictogramas para comenzar..." },
                    fontSize = 19.sp,
                    color = DarkBlue
                )

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onSpeak(sentence) },
                        enabled = sentence.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue
                        )
                    ) {
                        Text("🔊 Escuchar")
                    }

                    OutlinedButton(
                        onClick = { sentence = "" }
                    ) {
                        Text("Limpiar")
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onOpenEnvironments,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        ) {
            Text("🏡 Explorar ambientes", fontSize = 18.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Escribe tu propio mensaje",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = customText,
            onValueChange = { customText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Quiero decir...") },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    sentence = customText.trim()
                    onSpeak(sentence)
                },
                enabled = customText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue
                )
            ) {
                Text("Hablar")
            }

            OutlinedButton(
                onClick = {
                    sentence = listOf(sentence, customText.trim())
                        .filter { it.isNotBlank() }
                        .joinToString(" ")
                    customText = ""
                },
                enabled = customText.isNotBlank()
            ) {
                Text("Agregar a frase")
            }
        }
    }
}
