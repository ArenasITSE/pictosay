
package com.example.pictosay.ui.tutor.components

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

private val TutorDarkBlue = Color(0xFF092B70)

@Composable
fun TutorItemCard(
    emoji: String,
    title: String,
    description: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .background(
                        Color(0xFFE8F0FF),
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 30.sp
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TutorDarkBlue
                )

                if (description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }

            TextButton(onClick = onEdit) {
                Text(
                    text = "✏️",
                    fontSize = 20.sp
                )
            }

            TextButton(onClick = onDelete) {
                Text(
                    text = "🗑️",
                    fontSize = 20.sp
                )
            }
        }
    }
}
