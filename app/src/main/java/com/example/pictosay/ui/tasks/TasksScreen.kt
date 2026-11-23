
package com.example.pictosay.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class Routine(
    val title: String,
    val description: String,
    val emoji: String,
    val stars: Int = 1
)

@Composable
fun TasksScreen(
    onBack: () -> Unit
) {
    val routines = remember {
        listOf(
            Routine("Despertarme", "Comenzar mi día", "🌞"),
            Routine("Cepillarme los dientes", "Cuidar mi sonrisa", "🪥"),
            Routine("Desayunar", "Comer algo rico", "🥛"),
            Routine("Preparar mi mochila", "Alistar mis cosas", "🎒"),
            Routine("Hacer mi tarea", "Aprender algo nuevo", "📚"),
            Routine("Dormir", "Descansar y soñar", "🌙")
        )
    }

    var completed by remember {
        mutableStateOf(setOf<String>())
    }

    val stars = completed.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(18.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Regresar", color = Color(0xFF3978D5))
        }

        Text(
            "Mis rutinas 📅",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF092B70)
        )

        Text(
            "¡Cada pequeño logro cuenta!",
            color = Color.Gray
        )

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF2C9)
            )
        ) {
            Column(Modifier.padding(18.dp)) {
                Text(
                    "⭐ Mis estrellas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF092B70)
                )

                Text(
                    "$stars estrellas conseguidas",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3978D5)
                )

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { completed.size.toFloat() / routines.size },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF3978D5)
                )

                Text(
                    "${completed.size} de ${routines.size} actividades completadas",
                    color = Color.DarkGray
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(routines) { routine ->
                val isCompleted = routine.title in completed

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(routine.emoji, fontSize = 36.sp)

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                routine.title,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF092B70)
                            )

                            Text(
                                routine.description,
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }

                        Checkbox(
                            checked = isCompleted,
                            onCheckedChange = { checked ->
                                completed = if (checked) {
                                    completed + routine.title
                                } else {
                                    completed - routine.title
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
