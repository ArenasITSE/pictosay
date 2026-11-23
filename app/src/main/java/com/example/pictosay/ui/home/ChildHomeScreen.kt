
package com.example.pictosay.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictosay.data.local.ChildProfileEntity
import com.example.pictosay.ui.components.Mascot

private val DarkBlue = Color(0xFF092B70)
private val PrimaryBlue = Color(0xFF3978D5)

@Composable
fun ChildHomeScreen(
    profile: ChildProfileEntity?,
    onNavigate: (String) -> Unit
) {
    val name = profile?.name ?: "amigo"
    val gender = profile?.gender ?: "BOY"

    Scaffold(
        containerColor = Color(0xFFF7F9FC)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "¡Hola, $name! 👋",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Text(
                        text = "¿Qué quieres decir hoy?",
                        fontSize = 17.sp,
                        color = Color.DarkGray
                    )
                }

                Mascot(
                    gender = gender,
                    size = 82.dp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "¿Qué quieres hacer?",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    emoji = "💬",
                    title = "Comunicar",
                    background = Color(0xFFE8F0FF),
                    onClick = { onNavigate("communication") }
                )

                MenuCard(
                    modifier = Modifier.weight(1f),
                    emoji = "📅",
                    title = "Rutinas",
                    background = Color(0xFFFFF0DF),
                    onClick = { onNavigate("tasks") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MenuCard(
                    modifier = Modifier.weight(1f),
                    emoji = "😊",
                    title = "Emociones",
                    background = Color(0xFFFFF2C9),
                    onClick = { onNavigate("emotions") }
                )

                MenuCard(
                    modifier = Modifier.weight(1f),
                    emoji = "⭐",
                    title = "Favoritos",
                    background = Color(0xFFF0E9FF),
                    onClick = { onNavigate("favorites") }
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "💡 ¿Necesitas ayuda?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Puedes utilizar imágenes para expresar lo que necesitas.",
                        fontSize = 16.sp,
                        lineHeight = 25.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Tu mundo. Tu voz. 💙",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = PrimaryBlue,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MenuCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    background: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(178.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
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
                text = emoji,
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = DarkBlue
            )
        }
    }
}
