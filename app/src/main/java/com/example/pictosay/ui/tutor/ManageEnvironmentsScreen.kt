
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
import androidx.compose.ui.unit.sp
import com.example.pictosay.data.local.CategoryEntity
import com.example.pictosay.ui.tutor.components.TutorFormDialog
import com.example.pictosay.ui.tutor.components.TutorItemCard

private val EnvironmentBlue = Color(0xFF3978D5)
private val EnvironmentDarkBlue = Color(0xFF092B70)
private val EnvironmentBackground = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEnvironmentsScreen(
    categories: List<CategoryEntity>,
    onBack: () -> Unit,
    onAddCategory: (CategoryEntity) -> Unit,
    onUpdateCategory: (CategoryEntity) -> Unit,
    onDeleteCategory: (Int) -> Unit,
    onManagePictograms: (CategoryEntity) -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var editingCategory by remember {
        mutableStateOf<CategoryEntity?>(null)
    }
    var deletingCategory by remember {
        mutableStateOf<CategoryEntity?>(null)
    }

    Scaffold(
        containerColor = EnvironmentBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Administrar ambientes",
                        color = EnvironmentDarkBlue,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(
                            "←",
                            fontSize = 25.sp,
                            color = EnvironmentDarkBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EnvironmentBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingCategory = null
                    showForm = true
                },
                containerColor = EnvironmentBlue,
                contentColor = Color.White
            ) {
                Text("+", fontSize = 28.sp)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp)
        ) {
            Text(
                text = "Mis ambientes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = EnvironmentDarkBlue
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Agrega y personaliza los espacios de comunicación.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(18.dp))

            if (categories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🏠", fontSize = 55.sp)
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Todavía no tienes ambientes",
                            color = EnvironmentDarkBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Pulsa + para agregar el primero.",
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(
                        items = categories,
                        key = { it.id }
                    ) { category ->

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            TutorItemCard(
                                emoji = category.emoji,
                                title = category.name,
                                description = category.description,
                                onEdit = {
                                    editingCategory = category
                                    showForm = true
                                },
                                onDelete = {
                                    deletingCategory = category
                                }
                            )

                            TextButton(
                                onClick = {
                                    onManagePictograms(category)
                                }
                            ) {
                                Text(
                                    "🧩 Administrar pictogramas de ${category.name}",
                                    color = EnvironmentBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showForm) {
        val current = editingCategory

        TutorFormDialog(
            title = if (current == null) {
                "Nuevo ambiente"
            } else {
                "Editar ambiente"
            },
            initialName = current?.name.orEmpty(),
            initialDescription = current?.description.orEmpty(),
            initialEmoji = current?.emoji ?: "🏠",
            nameLabel = "Nombre del ambiente",
            descriptionLabel = "Descripción",
            onDismiss = {
                showForm = false
                editingCategory = null
            },
            onSave = { name, description, emoji ->

                if (current == null) {
                    onAddCategory(
                        CategoryEntity(
                            name = name,
                            emoji = emoji,
                            description = description,
                            sortOrder = (categories.maxOfOrNull {
                                it.sortOrder
                            } ?: 0) + 1
                        )
                    )
                } else {
                    onUpdateCategory(
                        current.copy(
                            name = name,
                            emoji = emoji,
                            description = description
                        )
                    )
                }

                showForm = false
                editingCategory = null
            }
        )
    }

    deletingCategory?.let { category ->
        AlertDialog(
            onDismissRequest = {
                deletingCategory = null
            },
            title = {
                Text("Eliminar ambiente")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar ${category.name}? " +
                            "También debemos gestionar sus pictogramas asociados."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteCategory(category.id)
                        deletingCategory = null
                    }
                ) {
                    Text(
                        "Eliminar",
                        color = Color(0xFFD9534F)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deletingCategory = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
