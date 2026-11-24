
package com.example.pictosay.ui.tutor.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val availableEmojis = listOf(
    "🏠", "🛏️", "🚿", "🍳", "🏡", "🛒", "🏫",
    "💧", "🍎", "🍞", "🥛", "🍌", "🍪", "🧃",
    "🐶", "🐱", "🚗", "⚽", "🎒", "📚", "✏️",
    "😊", "😢", "😡", "😨", "😴", "🥰", "🤩",
    "⭐", "❤️", "👍", "👏", "🧸", "🪥", "🚽",
    "🙋", "🙏", "🎨", "🎵", "📖", "🧩", "🎮"
)

@Composable
fun EmojiPickerDialog(
    onDismiss: () -> Unit,
    onEmojiSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Selecciona un emoji")
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 340.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableEmojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable {
                                onEmojiSelected(emoji)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 27.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        shape = RoundedCornerShape(22.dp)
    )
}
