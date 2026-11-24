
package com.example.pictosay.ui.tutor.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TutorFormDialog(
    title: String,
    initialName: String = "",
    initialDescription: String = "",
    initialEmoji: String = "⭐",
    nameLabel: String = "Nombre",
    descriptionLabel: String = "Descripción",
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember(initialName) {
        mutableStateOf(initialName)
    }

    var description by remember(initialDescription) {
        mutableStateOf(initialDescription)
    }

    var emoji by remember(initialEmoji) {
        mutableStateOf(initialEmoji)
    }

    var showEmojiPicker by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showEmojiPicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$emoji  Seleccionar emoji"
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(nameLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(descriptionLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            name.trim(),
                            description.trim(),
                            emoji
                        )
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3978D5)
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(22.dp)
    )

    if (showEmojiPicker) {
        EmojiPickerDialog(
            onDismiss = { showEmojiPicker = false },
            onEmojiSelected = {
                emoji = it
                showEmojiPicker = false
            }
        )
    }
}
