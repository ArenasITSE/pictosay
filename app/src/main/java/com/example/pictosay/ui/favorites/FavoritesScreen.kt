
package com.example.pictosay.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictosay.data.repository.PicToSayRepository
import kotlinx.coroutines.launch

// ========================================
// EMOJIS DISPONIBLES
// ========================================

private val availableEmojis = listOf(
    "😀", "😄", "😊", "😢",
    "😡", "😨", "😴", "❤️",
    "💧", "🥛", "🍎", "🍌",
    "🍞", "🍪", "🧃", "🍕",
    "⚽", "🏀", "🧸", "🚗",
    "🏠", "🏫", "🚽", "🚿",
    "🪥", "🧼", "🎒", "📚",
    "✏️", "🐶", "🐱", "🙋",
    "🙏", "👍", "⭐", "🎉"
)


// ========================================
// COLORES
// ========================================

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val Background = Color(0xFFF7F9FC)
private val LightBlue = Color(0xFFE8F0FF)
private val LightPurple = Color(0xFFF0E9FF)


// ========================================
// PANTALLA FAVORITOS
// ========================================

@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    repository: PicToSayRepository
) {

    val scope = rememberCoroutineScope()

    // ------------------------------------
    // FAVORITOS DESDE ROOM
    // ------------------------------------

    val favorites by repository.favoriteMessages.collectAsState(
        initial = emptyList()
    )

    // Inicializar favoritos predeterminados
    LaunchedEffect(Unit) {
        repository.initializeDefaultFavorites()
    }


    // ------------------------------------
    // ESTADOS DEL TUTOR
    // ------------------------------------

    var showPinDialog by remember {
        mutableStateOf(false)
    }

    var tutorUnlocked by remember {
        mutableStateOf(false)
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var selectedFavoriteId by remember {
        mutableIntStateOf(-1)
    }

    var pin by remember {
        mutableStateOf("")
    }

    var pinError by remember {
        mutableStateOf(false)
    }


    // ------------------------------------
    // ESTADOS PARA NUEVO FAVORITO
    // ------------------------------------

    var newText by remember {
        mutableStateOf("")
    }

    var selectedEmoji by remember {
        mutableStateOf("😀")
    }

    var saving by remember {
        mutableStateOf(false)
    }

    var saveError by remember {
        mutableStateOf<String?>(null)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(18.dp)
    ) {

        // ====================================
        // ENCABEZADO
        // ====================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBack
            ) {
                Text(
                    "← Regresar",
                    color = Blue
                )
            }

            Spacer(Modifier.weight(1f))

            if (!tutorUnlocked) {

                TextButton(
                    onClick = {
                        pin = ""
                        pinError = false
                        showPinDialog = true
                    }
                ) {
                    Text(
                        "🔒 Tutor",
                        color = DarkBlue,
                        fontSize = 13.sp
                    )
                }

            } else {

                TextButton(
                    onClick = {
                        tutorUnlocked = false
                    }
                ) {
                    Text(
                        "Salir tutor",
                        color = Blue
                    )
                }
            }
        }


        Text(
            "Mis favoritos ⭐",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            "Tus mensajes más importantes.",
            color = Color.Gray,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(18.dp))


        // ====================================
        // ÁREA DEL TUTOR
        // ====================================

        if (tutorUnlocked) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LightPurple
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "⚙️ Administración de favoritos",
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "Agrega mensajes personalizados para facilitar la comunicación.",
                        color = DarkBlue,
                        fontSize = 13.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            newText = ""
                            selectedEmoji = "😀"
                            saveError = null
                            showAddDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue
                        )
                    ) {
                        Text("＋ Agregar nuevo pictograma")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }


        // ====================================
        // LISTA DE FAVORITOS
        // ====================================

        if (favorites.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Todavía no tienes favoritos ⭐",
                    color = Color.Gray,
                    fontSize = 17.sp
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {

                items(
                    items = favorites,
                    key = { it.favoriteId }
                ) { favorite ->

                    // ====================================
                    // TARJETA INTERACTIVA
                    // ====================================

                    Card(
                        onClick = {
                            onSpeak(favorite.content)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Emoji
                            Text(
                                favorite.emoji,
                                fontSize = 37.sp
                            )

                            Spacer(Modifier.width(12.dp))

                            // Mensaje
                            Text(
                                favorite.content,
                                modifier = Modifier.weight(1f),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkBlue
                            )

                            // Eliminar desde el área del tutor
                            if (tutorUnlocked) {

                                IconButton(
                                    onClick = {
                                        selectedFavoriteId = favorite.messageId
                                        showDeleteDialog = true
                                    }
                                ) {
                                    Text(
                                        "🗑️",
                                        fontSize = 19.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // ========================================
    // DIÁLOGO DE CONTRASEÑA
    // ========================================

    if (showPinDialog) {

        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
            },

            title = {
                Text(
                    "🔒 Área del tutor",
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text("Introduce tu clave de acceso.")

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = pin,
                        onValueChange = {
                            pin = it.filter { char ->
                                char.isDigit()
                            }.take(4)

                            pinError = false
                        },
                        label = {
                            Text("Clave de acceso")
                        },
                        singleLine = true,
                        visualTransformation =
                            PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (pinError) {

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Clave incorrecta. Intenta nuevamente.",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        if (pin == "1220") {
                            tutorUnlocked = true
                            showPinDialog = false
                            pin = ""
                        } else {
                            pinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Text("Ingresar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showPinDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


    // ========================================
    // DIÁLOGO AGREGAR PICTOGRAMA
    // ========================================

    if (showAddDialog) {

        AlertDialog(
            onDismissRequest = {
                if (!saving) {
                    showAddDialog = false
                }
            },

            title = {
                Text(
                    "Nuevo pictograma",
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text(
                        "Selecciona un emoji:",
                        fontWeight = FontWeight.Medium,
                        color = DarkBlue
                    )

                    Spacer(Modifier.height(10.dp))

                    // Selector de emojis
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        modifier = Modifier.height(170.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        gridItems(availableEmojis) { emoji ->

                            Card(
                                onClick = {
                                    selectedEmoji = emoji
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (
                                        selectedEmoji == emoji
                                    ) LightBlue else Color.White
                                )
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        emoji,
                                        fontSize = 23.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "¿Qué dirá PicToSay?",
                        fontWeight = FontWeight.Medium,
                        color = DarkBlue
                    )

                    Spacer(Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newText,
                        onValueChange = {
                            newText = it
                            saveError = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Ej. Quiero ir al parque")
                        },
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Vista previa: $selectedEmoji ${
                            newText.ifBlank { "Tu mensaje" }
                        }",
                        color = Blue,
                        fontSize = 14.sp
                    )

                    if (saveError != null) {

                        Spacer(Modifier.height(8.dp))

                        Text(
                            saveError ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            },

            confirmButton = {

                Button(
                    enabled = newText.isNotBlank() && !saving,
                    onClick = {

                        scope.launch {

                            saving = true
                            saveError = null

                            try {

                                repository.saveFavoriteMessage(
                                    emoji = selectedEmoji,
                                    message = newText.trim()
                                )

                                showAddDialog = false
                                newText = ""
                                selectedEmoji = "😀"

                            } catch (exception: Exception) {

                                saveError = exception.message
                                    ?: "No se pudo guardar el favorito."

                            } finally {
                                saving = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Text(
                        if (saving) "Guardando..." else "Guardar"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    enabled = !saving,
                    onClick = {
                        showAddDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


    // ========================================
    // DIÁLOGO ELIMINAR FAVORITO
    // ========================================

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },

            title = {
                Text(
                    "Eliminar favorito",
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text("¿Seguro que deseas eliminar este mensaje?")
            },

            confirmButton = {

                Button(
                    onClick = {

                        scope.launch {

                            try {
                                repository.removeFavoriteMessage(
                                    selectedFavoriteId
                                )
                            } finally {
                                showDeleteDialog = false
                                selectedFavoriteId = -1
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue
                    )
                ) {
                    Text("Eliminar")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        selectedFavoriteId = -1
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
