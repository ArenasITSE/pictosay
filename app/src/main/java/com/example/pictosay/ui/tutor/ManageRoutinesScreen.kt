
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
import com.example.pictosay.data.local.TaskEntity

private val RoutineBlue = Color(0xFF3978D5)
private val RoutineDarkBlue = Color(0xFF092B70)
private val RoutineBackground = Color(0xFFF7F9FC)

@Composable
fun ManageRoutinesScreen(
    tasks: List<TaskEntity>,
    onBack: () -> Unit,
    onAddTask: (TaskEntity) -> Unit,
    onUpdateTask: (TaskEntity) -> Unit,
    onDeleteTask: (Int) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<TaskEntity?>(null) }
    var deletingTask by remember { mutableStateOf<TaskEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RoutineBackground)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Regresar")
            }

            Text(
                "Administrar rutinas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = RoutineDarkBlue
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "Crea actividades y recompensas para el niño.",
            color = RoutineDarkBlue
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                editingTask = null
                showForm = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = RoutineBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("＋ Crear rutina")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "${tasks.size} rutinas registradas",
            color = RoutineDarkBlue,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Todavía no hay rutinas creadas.")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    RoutineAdminCard(
                        task = task,
                        onEdit = {
                            editingTask = task
                            showForm = true
                        },
                        onDelete = {
                            deletingTask = task
                        }
                    )
                }
            }
        }
    }

    if (showForm) {
        RoutineEditorDialog(
            task = editingTask,
            onDismiss = {
                showForm = false
                editingTask = null
            },
            onSave = { title, description, emoji, stars ->
                val current = editingTask

                if (current == null) {
                    onAddTask(
                        TaskEntity(
                            title = title,
                            description = description,
                            emoji = emoji,
                            starsReward = stars
                        )
                    )
                } else {
                    onUpdateTask(
                        current.copy(
                            title = title,
                            description = description,
                            emoji = emoji,
                            starsReward = stars
                        )
                    )
                }

                showForm = false
                editingTask = null
            }
        )
    }

    deletingTask?.let { task ->
        AlertDialog(
            onDismissRequest = {
                deletingTask = null
            },
            title = { Text("Eliminar rutina") },
            text = {
                Text("¿Deseas eliminar la rutina ${task.title}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteTask(task.id)
                        deletingTask = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    deletingTask = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun RoutineAdminCard(
    task: TaskEntity,
    onEdit: () -> Unit,
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
                    task.emoji,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        task.title,
                        fontWeight = FontWeight.Bold,
                        color = RoutineDarkBlue
                    )

                    Text(
                        task.description.ifBlank { "Sin descripción" },
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        "⭐ ${task.starsReward} estrellas",
                        color = Color(0xFFB8860B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Text("✏️ Editar")
                }

                TextButton(onClick = onDelete) {
                    Text("🗑️", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
private fun RoutineEditorDialog(
    task: TaskEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Int) -> Unit
) {
    var title by remember(task?.id) {
        mutableStateOf(task?.title ?: "")
    }
    var description by remember(task?.id) {
        mutableStateOf(task?.description ?: "")
    }
    var emoji by remember(task?.id) {
        mutableStateOf(task?.emoji ?: "⭐")
    }
    var stars by remember(task?.id) {
        mutableStateOf((task?.starsReward ?: 1).toString())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (task == null) "Nueva rutina" else "Editar rutina")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nombre de la rutina") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )

                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = stars,
                    onValueChange = {
                        if (it.all(Char::isDigit)) stars = it
                    },
                    label = { Text("Estrellas de recompensa") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = title.isNotBlank() &&
                        emoji.isNotBlank() &&
                        (stars.toIntOrNull() ?: 0) > 0,
                onClick = {
                    onSave(
                        title.trim(),
                        description.trim(),
                        emoji.trim(),
                        stars.toInt()
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
