package com.example.pictosay.ui.tutor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pictosay.data.local.CategoryEntity
import com.example.pictosay.data.local.PictogramEntity

@Composable
fun ManagePictogramsScreen(
    category: CategoryEntity,
    pictograms: List<PictogramEntity>,
    onBack: () -> Unit,
    onAddPictogram: (PictogramEntity) -> Unit,
    onUpdatePictogram: (PictogramEntity) -> Unit,
    onTogglePictogram: (PictogramEntity) -> Unit,
    onDeletePictogram: (Int) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var selectedPictogram by remember {
        mutableStateOf<PictogramEntity?>(null)
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var pictogramToDelete by remember {
        mutableStateOf<PictogramEntity?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        // ENCABEZADO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Regresar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "Administrar pictogramas",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "${category.emoji} ${category.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // BOTÓN AGREGAR
        Button(
            onClick = {
                selectedPictogram = null
                showForm = true
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("＋ Agregar pictograma")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pictogramas registrados (${pictograms.size})",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (pictograms.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Todavía no hay pictogramas en este ambiente.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = pictograms,
                    key = { it.id }
                ) { pictogram ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pictogram.emoji,
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = pictogram.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = pictogram.speechText,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Text(
                                        text = if (pictogram.isActive) {
                                            "Activo"
                                        } else {
                                            "Desactivado"
                                        },
                                        color = if (pictogram.isActive) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        },
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        selectedPictogram = pictogram
                                        showForm = true
                                    }
                                ) {
                                    Text("Editar")
                                }

                                TextButton(
                                    onClick = {
                                        onTogglePictogram(pictogram)
                                    }
                                ) {
                                    Text(
                                        if (pictogram.isActive) {
                                            "Desactivar"
                                        } else {
                                            "Activar"
                                        }
                                    )
                                }

                                TextButton(
                                    onClick = {
                                        pictogramToDelete = pictogram
                                        showDeleteDialog = true
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // FORMULARIO AGREGAR / EDITAR
    if (showForm) {
        PictogramEditorDialog(
            pictogram = selectedPictogram,
            categoryId = category.id,
            onDismiss = {
                showForm = false
            },
            onSave = { pictogram ->
                if (selectedPictogram == null) {
                    onAddPictogram(pictogram)
                } else {
                    onUpdatePictogram(pictogram)
                }

                showForm = false
            }
        )
    }

    // CONFIRMACIÓN DE ELIMINACIÓN
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Eliminar pictograma")
            },
            text = {
                Text(
                    "¿Estás seguro de eliminar " +
                            "${pictogramToDelete?.name}? " +
                            "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pictogramToDelete?.let {
                            onDeletePictogram(it.id)
                        }

                        showDeleteDialog = false
                        pictogramToDelete = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun PictogramEditorDialog(
    pictogram: PictogramEntity?,
    categoryId: Int,
    onDismiss: () -> Unit,
    onSave: (PictogramEntity) -> Unit
) {
    var name by remember(pictogram) {
        mutableStateOf(pictogram?.name ?: "")
    }

    var emoji by remember(pictogram) {
        mutableStateOf(pictogram?.emoji ?: "⭐")
    }

    var speechText by remember(pictogram) {
        mutableStateOf(pictogram?.speechText ?: "")
    }

    var description by remember(pictogram) {
        mutableStateOf(pictogram?.description ?: "")
    }

    val emojis = listOf(
        "⭐", "💧", "🍎", "🍞", "🥛",
        "🚽", "🛏️", "🪥", "👕", "👟",
        "⚽", "📚", "✏️", "🎒", "🧸",
        "🚗", "🏠", "🍽️", "😊", "🙋"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (pictogram == null) {
                    "Nuevo pictograma"
                } else {
                    "Editar pictograma"
                }
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Selecciona un emoji",
                    style = MaterialTheme.typography.labelLarge
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    emojis.take(5).forEach { option ->
                        TextButton(
                            onClick = { emoji = option }
                        ) {
                            Text(
                                text = if (emoji == option) {
                                    "✓$option"
                                } else {
                                    option
                                }
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    emojis.drop(5).take(5).forEach { option ->
                        TextButton(
                            onClick = { emoji = option }
                        ) {
                            Text(
                                text = if (emoji == option) {
                                    "✓$option"
                                } else {
                                    option
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del pictograma") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = speechText,
                    onValueChange = { speechText = it },
                    label = { Text("Texto que se pronunciará") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank() && speechText.isNotBlank(),
                onClick = {
                    onSave(
                        PictogramEntity(
                            id = pictogram?.id ?: 0,
                            name = name.trim(),
                            emoji = emoji,
                            categoryId = categoryId,
                            speechText = speechText.trim(),
                            description = description.trim(),
                            isActive = pictogram?.isActive ?: true
                        )
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
