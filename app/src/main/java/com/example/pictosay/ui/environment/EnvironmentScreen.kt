
package com.example.pictosay.ui.environment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ========================================
// MODELOS
// ========================================

data class EnvironmentItem(
    val name: String,
    val emoji: String,
    val description: String
)

data class EnvironmentPictogram(
    val name: String,
    val emoji: String,
    val phrase: String
)

// ========================================
// AMBIENTES
// ========================================

private val environments = listOf(
    EnvironmentItem("Mi cuarto", "🛏️", "Descansa y juega"),
    EnvironmentItem("El baño", "🚿", "Cuida tu higiene"),
    EnvironmentItem("La cocina", "🍳", "Alimentos y bebidas"),
    EnvironmentItem("Mi casa y patio", "🏡", "Mi hogar"),
    EnvironmentItem("Supermercado", "🛒", "Vamos de compras"),
    EnvironmentItem("Mi escuela", "🏫", "Aprendemos juntos")
)

// ========================================
// PICTOGRAMAS
// ========================================

private val pictograms = mapOf(
    "Mi cuarto" to listOf(
        EnvironmentPictogram("Cama", "🛏️", "Quiero dormir"),
        EnvironmentPictogram("Almohada", "🛌", "Quiero mi almohada"),
        EnvironmentPictogram("Juguete", "🧸", "Quiero jugar"),
        EnvironmentPictogram("Ropa", "👕", "Quiero mi ropa")
    ),

    "El baño" to listOf(
        EnvironmentPictogram("Baño", "🚽", "Quiero ir al baño"),
        EnvironmentPictogram("Ducha", "🚿", "Quiero bañarme"),
        EnvironmentPictogram("Jabón", "🧼", "Necesito jabón"),
        EnvironmentPictogram("Cepillo", "🪥", "Quiero cepillarme")
    ),

    "La cocina" to listOf(
        EnvironmentPictogram("Agua", "💧", "Quiero agua"),
        EnvironmentPictogram("Manzana", "🍎", "Quiero una manzana"),
        EnvironmentPictogram("Pan", "🍞", "Quiero pan"),
        EnvironmentPictogram("Leche", "🥛", "Quiero leche")
    ),

    "Mi casa y patio" to listOf(
        EnvironmentPictogram("Perro", "🐶", "Quiero ver al perro"),
        EnvironmentPictogram("Gato", "🐱", "Quiero ver al gato"),
        EnvironmentPictogram("Pelota", "⚽", "Quiero jugar"),
        EnvironmentPictogram("Auto", "🚗", "Quiero salir")
    ),

    "Supermercado" to listOf(
        EnvironmentPictogram("Plátano", "🍌", "Quiero un plátano"),
        EnvironmentPictogram("Galletas", "🍪", "Quiero galletas"),
        EnvironmentPictogram("Jugo", "🧃", "Quiero jugo"),
        EnvironmentPictogram("Carrito", "🛒", "Vamos a comprar")
    ),

    "Mi escuela" to listOf(
        EnvironmentPictogram("Mochila", "🎒", "Quiero mi mochila"),
        EnvironmentPictogram("Libro", "📚", "Quiero leer"),
        EnvironmentPictogram("Lápiz", "✏️", "Necesito un lápiz"),
        EnvironmentPictogram("Maestra", "👩‍🏫", "Quiero hablar con mi maestra")
    )
)

// ========================================
// COLORES
// ========================================

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val Background = Color(0xFFF7F9FC)
private val LightBlue = Color(0xFFE8F0FF)

// ========================================
// PANTALLA PRINCIPAL
// ========================================

@Composable
fun EnvironmentScreen(
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onAddPhrase: (String) -> Unit
) {

    var selectedEnvironment by remember {
        mutableStateOf<EnvironmentItem?>(null)
    }

    var sceneMode by remember {
        mutableStateOf(false)
    }

    var selectedPhrase by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {

        // ====================================
        // ENCABEZADO
        // ====================================

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = {
                    if (selectedEnvironment != null) {
                        selectedEnvironment = null
                        selectedPhrase = ""
                        sceneMode = false
                    } else {
                        onBack()
                    }
                }
            ) {
                Text(
                    "← Regresar",
                    color = Blue
                )
            }

            Text(
                "Mis ambientes",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }

        Spacer(Modifier.height(12.dp))

        // ====================================
        // LISTA DE AMBIENTES
        // ====================================

        if (selectedEnvironment == null) {

            environments.forEach { environment ->

                Card(
                    onClick = {
                        selectedEnvironment = environment
                        selectedPhrase = ""
                        sceneMode = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            environment.emoji,
                            fontSize = 38.sp
                        )

                        Spacer(Modifier.width(15.dp))

                        Column {

                            Text(
                                environment.name,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue,
                                fontSize = 18.sp
                            )

                            Text(
                                environment.description,
                                color = Color.Gray
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Text(
                            "›",
                            color = Blue,
                            fontSize = 30.sp
                        )
                    }
                }
            }

        } else {

            // ====================================
            // AMBIENTE SELECCIONADO
            // ====================================

            Text(
                "${selectedEnvironment!!.emoji} ${selectedEnvironment!!.name}",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            Spacer(Modifier.height(12.dp))

            // ====================================
            // SELECTOR DE VISTA
            // ====================================

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {

                SegmentedButton(
                    selected = !sceneMode,
                    onClick = {
                        sceneMode = false
                    },
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        bottomStart = 12.dp
                    )
                ) {
                    Text("Pictogramas")
                }

                SegmentedButton(
                    selected = sceneMode,
                    onClick = {
                        sceneMode = true
                    },
                    shape = RoundedCornerShape(
                        topEnd = 12.dp,
                        bottomEnd = 12.dp
                    )
                ) {
                    Text("Escena")
                }
            }

            Spacer(Modifier.height(15.dp))

            val items = pictograms[
                selectedEnvironment!!.name
            ].orEmpty()

            // ====================================
            // MODO ESCENA
            // ====================================

            if (sceneMode) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .background(
                                LightBlue,
                                RoundedCornerShape(16.dp)
                            )
                    ) {

                        // Ilustración del ambiente

                        Text(
                            selectedEnvironment!!.emoji,
                            fontSize = 75.sp,
                            modifier = Modifier.align(
                                Alignment.TopCenter
                            )
                        )

                        // Objetos interactivos

                        items.forEachIndexed { index, item ->

                            val positions = listOf(
                                Alignment.CenterStart,
                                Alignment.CenterEnd,
                                Alignment.BottomStart,
                                Alignment.BottomEnd
                            )

                            Column(
                                modifier = Modifier
                                    .align(positions[index % 4])
                                    .clickable {

                                        // Guardar selección
                                        selectedPhrase = item.phrase

                                        // Enviar al constructor
                                        onAddPhrase(item.phrase)

                                        // REPRODUCIR VOZ
                                        onSpeak(item.phrase)
                                    }
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    item.emoji,
                                    fontSize = 42.sp
                                )

                                Text(
                                    item.name,
                                    color = DarkBlue,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

            } else {

                // ====================================
                // MODO PICTOGRAMAS
                // ====================================

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(items) { item ->

                        Card(
                            onClick = {

                                // Guardar selección
                                selectedPhrase = item.phrase

                                // Enviar al constructor
                                onAddPhrase(item.phrase)

                                // REPRODUCIR VOZ
                                onSpeak(item.phrase)
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    item.emoji,
                                    fontSize = 45.sp
                                )

                                Spacer(Modifier.height(8.dp))

                                Text(
                                    item.name,
                                    color = DarkBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // ====================================
            // FRASE SELECCIONADA
            // ====================================

            if (selectedPhrase.isNotBlank()) {

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = LightBlue
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            "Seleccionado: $selectedPhrase",
                            color = DarkBlue,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            TextButton(
                                onClick = {
                                    // Volver a escuchar
                                    onSpeak(selectedPhrase)
                                }
                            ) {
                                Text(
                                    "🔊 Escuchar",
                                    color = Blue
                                )
                            }

                            TextButton(
                                onClick = {
                                    selectedPhrase = ""
                                }
                            ) {
                                Text(
                                    "Limpiar",
                                    color = DarkBlue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
