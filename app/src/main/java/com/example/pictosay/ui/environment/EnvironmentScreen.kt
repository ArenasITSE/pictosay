
package com.example.pictosay.ui.environment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pictosay.data.local.CategoryEntity
import com.example.pictosay.data.local.PictogramEntity
import com.example.pictosay.data.repository.PicToSayRepository

private val Blue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val Background = Color(0xFFF7F9FC)

private val EnvironmentColors = listOf(
    Color(0xFFE8F0FF),
    Color(0xFFDDF8FC),
    Color(0xFFFFF2C9),
    Color(0xFFE2F8DF),
    Color(0xFFF0E9FF),
    Color(0xFFFFE8EF)
)

@Composable
fun EnvironmentScreen(
    repository: PicToSayRepository,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onAddPhrase: (String) -> Unit
) {
    val environments by repository.categories.collectAsState(
        initial = emptyList()
    )

    var selectedEnvironment by remember {
        mutableStateOf<CategoryEntity?>(null)
    }

    var sceneMode by remember {
        mutableStateOf(false)
    }

    val currentItems by repository
        .getPictogramsByCategory(selectedEnvironment?.id ?: -1)
        .collectAsState(initial = emptyList())

    // Actualizar el ambiente seleccionado si cambia en el panel del tutor.
    LaunchedEffect(environments, selectedEnvironment?.id) {
        val selectedId = selectedEnvironment?.id

        if (selectedId != null) {
            val updatedEnvironment = environments.find {
                it.id == selectedId
            }

            if (updatedEnvironment == null) {
                selectedEnvironment = null
                sceneMode = false
            } else {
                selectedEnvironment = updatedEnvironment
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 16.dp)
    ) {

        // BOTÓN REGRESAR
        TextButton(
            onClick = {
                if (selectedEnvironment != null) {
                    selectedEnvironment = null
                    sceneMode = false
                } else {
                    onBack()
                }
            }
        ) {
            Text(
                text = "← Regresar",
                color = Blue,
                fontSize = 16.sp
            )
        }

        // =====================================
        // LISTA DE AMBIENTES
        // =====================================

        if (selectedEnvironment == null) {

            Text(
                text = "Mis ambientes",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )

            if (environments.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no hay ambientes disponibles. 💙",
                        color = DarkBlue,
                        fontSize = 17.sp
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    items(
                        count = environments.size,
                        key = { index -> environments[index].id }
                    ) { index ->

                        val environment = environments[index]

                        val backgroundColor =
                            EnvironmentColors[index % EnvironmentColors.size]

                        Card(
                            onClick = {
                                selectedEnvironment = environment
                                sceneMode = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = backgroundColor
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(17.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = environment.emoji,
                                    fontSize = 40.sp
                                )

                                Spacer(Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = environment.name,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkBlue
                                    )

                                    Text(
                                        text = environment.description,
                                        fontSize = 14.sp,
                                        color = Color.DarkGray
                                    )
                                }

                                Text(
                                    text = "›",
                                    fontSize = 32.sp,
                                    color = DarkBlue
                                )
                            }
                        }
                    }
                }
            }

        } else {

            // =====================================
            // AMBIENTE SELECCIONADO
            // =====================================

            val environment = selectedEnvironment!!

            Text(
                text = "${environment.emoji} ${environment.name}",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // SELECTOR DE VISTA
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {

                SegmentedButton(
                    selected = !sceneMode,
                    onClick = { sceneMode = false },
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        bottomStart = 14.dp
                    )
                ) {
                    Text("Pictogramas")
                }

                SegmentedButton(
                    selected = sceneMode,
                    onClick = { sceneMode = true },
                    shape = RoundedCornerShape(
                        topEnd = 14.dp,
                        bottomEnd = 14.dp
                    )
                ) {
                    Text("Escena")
                }
            }

            Spacer(Modifier.height(16.dp))

            // =====================================
            // CONTENIDO
            // =====================================

            if (currentItems.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Este ambiente todavía no tiene pictogramas. 💙",
                        color = DarkBlue,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }

            } else if (sceneMode) {

                SceneEnvironment(
                    items = currentItems,
                    onSpeak = onSpeak,
                    onAddPhrase = onAddPhrase,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

            } else {

                // VISTA DE PICTOGRAMAS
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    items(
                        items = currentItems,
                        key = { it.id }
                    ) { item ->

                        PictogramTile(
                            item = item,
                            onClick = {
                                onSpeak(item.speechText)
                                onAddPhrase(item.speechText)
                            }
                        )
                    }
                }
            }
        }
    }
}

// =========================================
// TARJETA INDIVIDUAL DE PICTOGRAMA
// =========================================

@Composable
private fun PictogramTile(
    item: PictogramEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = item.emoji,
                fontSize = 43.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = item.name,
                color = DarkBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

// =========================================
// ESCENA DEL AMBIENTE
// =========================================

@Composable
private fun SceneEnvironment(
    items: List<PictogramEntity>,
    onSpeak: (String) -> Unit,
    onAddPhrase: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFFFF2D6))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ENCABEZADO DE ESCENA
        Text(
            text = "✨",
            fontSize = 38.sp
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Toca los objetos para escuchar",
            fontSize = 16.sp,
            color = Color(0xFF30456B),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.height(18.dp))

        // CUADRÍCULA CON TARJETAS SEPARADAS
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(
                horizontal = 2.dp,
                vertical = 4.dp
            )
        ) {

            items(
                items = items,
                key = { it.id }
            ) { item ->

                Card(
                    onClick = {
                        onSpeak(item.speechText)
                        onAddPhrase(item.speechText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = item.emoji,
                            fontSize = 46.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = item.name,
                            fontSize = 17.sp,
                            color = DarkBlue,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
