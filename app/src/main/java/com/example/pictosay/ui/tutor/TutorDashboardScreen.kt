
package com.example.pictosay.ui.tutor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

private val TutorPrimaryBlue = Color(0xFF3978D5)
private val TutorBackground = Color(0xFFF7F9FC)
private val TutorDarkBlue = Color(0xFF092B70)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorDashboardScreen(
    onBack: () -> Unit,
    onManageEnvironments: () -> Unit,
    onManagePictograms: () -> Unit,
    onManageEmotions: () -> Unit,
    onManageRoutines: () -> Unit
) {
    Scaffold(
        containerColor = TutorBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Panel del tutor",
                        color = TutorDarkBlue,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(
                            text = "‹",
                            fontSize = 30.sp,
                            color = TutorDarkBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TutorBackground
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¡Hola, tutor!",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = TutorDarkBlue
            )

            Text(
                text = "Personaliza los contenidos de comunicación de PicToSay.",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            TutorDashboardCard(
                emoji = "🏠",
                title = "Ambientes",
                description = "Administra los espacios y lugares.",
                background = Color(0xFFE8F0FF),
                onClick = onManageEnvironments
            )

            TutorDashboardCard(
                emoji = "🧩",
                title = "Pictogramas",
                description = "Agrega y organiza los pictogramas.",
                background = Color(0xFFFFF2C9),
                onClick = onManagePictograms
            )

            TutorDashboardCard(
                emoji = "😊",
                title = "Emociones",
                description = "Personaliza emociones y frases.",
                background = Color(0xFFF0E9FF),
                onClick = onManageEmotions
            )

            TutorDashboardCard(
                emoji = "⭐",
                title = "Rutinas",
                description = "Crea actividades y recompensas.",
                background = Color(0xFFFFF0DF),
                onClick = onManageRoutines
            )
        }
    }
}

@Composable
private fun TutorDashboardCard(
    emoji: String,
    title: String,
    description: String,
    background: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = background,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 36.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TutorDarkBlue
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }

        Text(
            text = "›",
            fontSize = 30.sp,
            color = TutorPrimaryBlue
        )
    }
}
