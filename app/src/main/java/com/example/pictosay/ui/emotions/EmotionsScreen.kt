
package com.example.pictosay.ui.emotions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictosay.data.local.EmotionEntity

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val Background = Color(0xFFF7F9FC)

@Composable
fun EmotionsScreen(
    emotions: List<EmotionEntity>,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onAddPhrase: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(18.dp)
    ) {
        TextButton(onClick = onBack) {
            Text(
                text = "← Regresar",
                color = Blue
            )
        }

        Text(
            text = "¿Cómo me siento? 😊",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            text = "Todas las emociones son importantes.",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(20.dp))

        if (emotions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no hay emociones disponibles. Pide a tu tutor que agregue algunas. 💙",
                    color = DarkBlue,
                    fontSize = 17.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(
                    items = emotions,
                    key = { it.id }
                ) { emotion ->

                    Card(
                        onClick = {
                            onSpeak(emotion.speechText)
                            onAddPhrase(emotion.speechText)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
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
                                text = emotion.emoji,
                                fontSize = 52.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = emotion.name,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
