
package com.example.pictosay.ui.tutor

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
import com.example.pictosay.data.local.EmotionEntity

private val EmotionBlue = Color(0xFF3978D5)
private val EmotionDarkBlue = Color(0xFF092B70)
private val EmotionBackground = Color(0xFFF7F9FC)

@Composable
fun ManageEmotionsScreen(
    emotions: List<EmotionEntity>,
    onBack: () -> Unit,
    onAddEmotion: (EmotionEntity) -> Unit,
    onUpdateEmotion: (EmotionEntity) -> Unit,
    onToggleEmotion: (EmotionEntity) -> Unit,
    onDeleteEmotion: (Int) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var editingEmotion by remember { mutableStateOf<EmotionEntity?>(null) }
    var deletingEmotion by remember { mutableStateOf<EmotionEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EmotionBackground)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onBack) {
                Text("← Regresar")
            }

            Text(
                text = "Administrar emociones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmotionDarkBlue
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Personaliza las emociones que el niño puede comunicar.",
            color = EmotionDarkBlue
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                editingEmotion = null
                showForm = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmotionBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("＋ Agregar emoción")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "${emotions.size} emociones registradas",
            color = EmotionDarkBlue,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        if (emotions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Todavía no hay emociones registradas.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(emotions, key = { it.id }) { emotion ->
                    EmotionAdminCard(
                        emotion = emotion,
                        onEdit = {
                            editingEmotion = emotion
                            showForm = true
                        },
                        onToggle = {
                            onToggleEmotion(emotion)
                        },
                        onDelete = {
                            deletingEmotion = emotion
                        }
                    )
                }
            }
        }
    }

    if (showForm) {
        EmotionEditorDialog(
            emotion = editingEmotion,
            onDismiss = {
                showForm = false
                editingEmotion = null
            },
            onSave = { name, emoji, phrase, description ->
                val current = editingEmotion

                if (current == null) {
                    onAddEmotion(
                        EmotionEntity(
                            name = name,
                            emoji = emoji,
                            speechText = phrase,
                            description = description,
                            sortOrder = emotions.size + 1
                        )
                    )
                } else {
                    onUpdateEmotion(
                        current.copy(
                            name = name,
                            emoji = emoji,
                            speechText = phrase,
                            description = description
                        )
                    )
                }

                showForm = false
                editingEmotion = null
            }
        )
    }

    deletingEmotion?.let { emotion ->
        AlertDialog(
            onDismissRequest = {
                deletingEmotion = null
            },
            title = { Text("Eliminar emoción") },
            text = {
                Text("¿Deseas eliminar ${emotion.name}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteEmotion(emotion.id)
                        deletingEmotion = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    deletingEmotion = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun EmotionAdminCard(
    emotion: EmotionEntity,
    onEdit: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emotion.emoji,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        emotion.name,
                        fontWeight = FontWeight.Bold,
                        color = EmotionDarkBlue
                    )
                    Text(
                        emotion.speechText,
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        if (emotion.isActive) "Activa" else "Desactivada",
                        color = if (emotion.isActive) {
                            Color(0xFF23834A)
                        } else {
                            Color.Gray
                        },
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            if (emotion.description.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    emotion.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onEdit) {
                    Text("✏️ Editar")
                }

                TextButton(onClick = onToggle) {
                    Text(if (emotion.isActive) "Desactivar" else "Activar")
                }

                TextButton(onClick = onDelete) {
                    Text("🗑️", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun EmotionEditorDialog(
    emotion: EmotionEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember(emotion?.id) {
        mutableStateOf(emotion?.name ?: "")
    }
    var emoji by remember(emotion?.id) {
        mutableStateOf(emotion?.emoji ?: "😊")
    }
    var phrase by remember(emotion?.id) {
        mutableStateOf(emotion?.speechText ?: "")
    }
    var description by remember(emotion?.id) {
        mutableStateOf(emotion?.description ?: "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (emotion == null) "Nueva emoción" else "Editar emoción")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = phrase,
                    onValueChange = { phrase = it },
                    label = { Text("Frase que se pronunciará") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank() &&
                        emoji.isNotBlank() &&
                        phrase.isNotBlank(),
                onClick = {
                    onSave(
                        name.trim(),
                        emoji.trim(),
                        phrase.trim(),
                        description.trim()
                    )
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
