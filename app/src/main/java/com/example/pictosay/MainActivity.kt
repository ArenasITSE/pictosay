package com.example.pictosay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.speech.tts.TextToSpeech
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import kotlinx.coroutines.delay
import androidx.compose.foundation.horizontalScroll
// ==========================================
// PALETA DE COLORES
// ==========================================

private val PrimaryBlue = Color(0xFF3978D5)
private val LightBlue = Color(0xFFE8F0FF)
private val BackgroundColor = Color(0xFFF7F9FC)
private val TutorPurple = Color(0xFF7754B8)
private val DarkBlue = Color(0xFF092B70)
private val LightPurple = Color(0xFFF0E9FF)
private val LightYellow = Color(0xFFFFF2C9)
private val LightOrange = Color(0xFFFFF0DF)

// ==========================================
// ACTIVITY PRINCIPAL
// ==========================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PicToSayTheme {
                PicToSayApp()
            }
        }
    }
}

// ==========================================
// TEMA
// ==========================================

@Composable
fun PicToSayTheme(content: @Composable () -> Unit) {

    val colors = lightColorScheme(
        primary = PrimaryBlue,
        secondary = TutorPurple,
        background = BackgroundColor,
        surface = Color.White
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

// ==========================================
// NAVEGACION PRINCIPAL
// ==========================================

enum class AppScreen {
    SPLASH,
    PROFILE,
    CHILD,
    TUTOR
}

data class RoutineTask(val id: Int, val emoji: String, val title: String)

@Composable
fun PicToSayApp() {

    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    var childName by remember { mutableStateOf("Lucas") }
    var childAge by remember { mutableStateOf("6") }
    var childGender by remember { mutableStateOf("Niño") }
    var childNotes by remember { mutableStateOf("") }
    var tutorPin by remember { mutableStateOf("1220") }
    val routineTasks = remember {
        mutableStateListOf(
            RoutineTask(1, "🛏️", "Despertar"),
            RoutineTask(2, "🪥", "Cepillarse los dientes"),
            RoutineTask(3, "🥣", "Desayunar"),
            RoutineTask(4, "🎒", "Ir a la escuela"),
            RoutineTask(5, "📚", "Hacer tarea"),
            RoutineTask(6, "🌙", "Dormir")
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BackgroundColor
    ) {

        when (currentScreen) {

            AppScreen.SPLASH -> {
                SplashScreen {
                    currentScreen = AppScreen.PROFILE
                }
            }

            AppScreen.PROFILE -> {
                ProfileSelectionScreen(
                    onChildSelected = {
                        currentScreen = AppScreen.CHILD
                    },
                    onTutorSelected = {
                        currentScreen = AppScreen.TUTOR
                    }
                )
            }

            AppScreen.CHILD -> {
                ChildHomeScreen(childName, childGender, routineTasks)
            }

            AppScreen.TUTOR -> {
                TutorAccessScreen(
                    accessPin = tutorPin,
                    onBack = { currentScreen = AppScreen.PROFILE },
                    onPinChanged = { tutorPin = it },
                    childName = childName,
                    onChildNameChange = { childName = it },
                    childAge = childAge,
                    onChildAgeChange = { childAge = it },
                    childGender = childGender,
                    onChildGenderChange = { childGender = it },
                    childNotes = childNotes,
                    onChildNotesChange = { childNotes = it },
                    routineTasks = routineTasks
                )
            }
        }
    }
}

// ==========================================
// 01. SPLASH SCREEN
// ==========================================

@Composable
fun SplashScreen(
    onContinue: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.pictosay_logo
            ),
            contentDescription = "Logotipo PicToSay",
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Tu mundo. Tu voz.",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu manera de comunicar.",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        // La pantalla de bienvenida avanza automáticamente después de 2.5 segundos.
        LaunchedEffect(Unit) {
            delay(2500)
            onContinue()
        }
    }
}

// ==========================================
// 02. SELECCION DE PERFIL
// ==========================================

@Composable
fun ProfileSelectionScreen(
    onChildSelected: () -> Unit,
    onTutorSelected: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Pic to Say",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿Quién usará la aplicación?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(35.dp))

        ProfileCard(
            emoji = "👦🏻",
            title = "Soy el niño",
            description = "Quiero comunicarme",
            color = LightBlue,
            onClick = onChildSelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProfileCard(
            emoji = "👩🏻",
            title = "Soy el tutor",
            description = "Quiero configurar la aplicación",
            color = LightPurple,
            onClick = onTutorSelected
        )
    }
}

// ==========================================
// TARJETA DE PERFIL
// ==========================================

@Composable
fun ProfileCard(
    emoji: String,
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = emoji,
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {

                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// ==========================================
// ÁREA DEL TUTOR: ACCESO Y PANEL DE CONTROL
// ==========================================

private enum class TutorSection {
    RESUMEN, PERFIL, RUTINAS, APOYO
}

@Composable
fun TutorAccessScreen(
    accessPin: String,
    onBack: () -> Unit,
    onPinChanged: (String) -> Unit,
    childName: String,
    onChildNameChange: (String) -> Unit,
    childAge: String,
    onChildAgeChange: (String) -> Unit,
    childGender: String,
    onChildGenderChange: (String) -> Unit,
    childNotes: String,
    onChildNotesChange: (String) -> Unit,
    routineTasks: MutableList<RoutineTask>
) {
    var enteredCode by remember { mutableStateOf("") }
    var unlocked by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    if (unlocked) {
        TutorDashboard(
            onBack, childName, onChildNameChange, childAge, onChildAgeChange,
            childGender, onChildGenderChange, childNotes, onChildNotesChange,
            routineTasks, accessPin, onPinChanged
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🔐", fontSize = 66.sp)
            Spacer(Modifier.height(14.dp))
            Text("Acceso para tutores", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
            Spacer(Modifier.height(8.dp))
            Text("Ingresa tu código de acceso para administrar PicToSay.", textAlign = TextAlign.Center, color = Color.DarkGray)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = enteredCode,
                onValueChange = { value ->
                    enteredCode = value.filter { it.isDigit() }.take(4)
                    error = false
                },
                label = { Text("Código de 4 dígitos") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (error) {
                Text("El código no es correcto. Inténtalo nuevamente.", color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = {
                    if (enteredCode == accessPin) unlocked = true else error = true
                },
                enabled = enteredCode.length == 4,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TutorPurple),
                shape = RoundedCornerShape(16.dp)
            ) { Text("Ingresar", fontWeight = FontWeight.Bold) }
            TextButton(onClick = onBack) { Text("Regresar", color = DarkBlue) }
        }
    }
}

@Composable
private fun TutorDashboard(
    onBack: () -> Unit,
    childName: String,
    onChildNameChange: (String) -> Unit,
    childAge: String,
    onChildAgeChange: (String) -> Unit,
    childGender: String,
    onChildGenderChange: (String) -> Unit,
    childNotes: String,
    onChildNotesChange: (String) -> Unit,
    routineTasks: MutableList<RoutineTask>,
    accessPin: String,
    onPinChanged: (String) -> Unit
) {
    var section by remember { mutableStateOf(TutorSection.RESUMEN) }
    var useVoice by remember { mutableStateOf(true) }
    var showPictures by remember { mutableStateOf(true) }
    var newTask by remember { mutableStateOf("") }
    var newTaskEmoji by remember { mutableStateOf("⭐") }
    var newPin by remember { mutableStateOf(accessPin) }

    Column(Modifier.fillMaxSize().background(BackgroundColor)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(TutorPurple).padding(horizontal = 18.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("Panel del tutor", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                Text("Administración de PicToSay", color = Color.White.copy(alpha = .88f), fontSize = 13.sp)
            }
            TextButton(onClick = onBack) { Text("Salir", color = Color.White) }
        }

        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TutorTab("Resumen", section == TutorSection.RESUMEN) { section = TutorSection.RESUMEN }
            TutorTab("Perfil", section == TutorSection.PERFIL) { section = TutorSection.PERFIL }
            TutorTab("Rutinas", section == TutorSection.RUTINAS) { section = TutorSection.RUTINAS }
            TutorTab("Ajustes", section == TutorSection.APOYO) { section = TutorSection.APOYO }
        }

        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(18.dp)) {
            when (section) {
                TutorSection.RESUMEN -> {
                    Text("¡Bienvenido! 👋", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Text("Desde aquí puedes adaptar la aplicación a las necesidades del niño.", color = Color.DarkGray)
                    Spacer(Modifier.height(18.dp))
                    TutorInfoCard(if (childGender == "Niña") "👧🏻" else "👦🏻", "Perfil del niño", "$childName · $childAge años", "PERFIL") { section = TutorSection.PERFIL }
                    TutorInfoCard("🗓️", "Rutinas", "${routineTasks.size} actividades configuradas", "RUTINAS") { section = TutorSection.RUTINAS }
                    TutorInfoCard("🔊", "Comunicación", if (useVoice) "Lectura por voz activada" else "Lectura por voz desactivada", "AJUSTES") { section = TutorSection.APOYO }
                    Spacer(Modifier.height(12.dp))
                    Text("Recomendación", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 17.sp)
                    Text("Personaliza el nombre, las rutinas y las opciones de apoyo para que PicToSay acompañe mejor las actividades diarias.", color = Color.DarkGray, fontSize = 14.sp)
                }
                TutorSection.PERFIL -> {
                    Text("Perfil del niño", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Text("Personaliza la información que aparece en el área infantil.", color = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                    Text("¿Cómo es el perfil?", color = Color.Gray, fontSize = 14.sp)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        listOf("Niño", "Niña").forEach { option ->
                            FilterChip(
                                selected = childGender == option,
                                onClick = { onChildGenderChange(option) },
                                label = { Text(if (option == "Niño") "👦 Niño" else "👧 Niña") }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = childName, onValueChange = onChildNameChange,
                        label = { Text("Nombre del niño o niña") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TutorPurple, focusedLabelColor = TutorPurple)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = childAge, onValueChange = { onChildAgeChange(it.filter(Char::isDigit).take(2)) },
                        label = { Text("Edad") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TutorPurple, focusedLabelColor = TutorPurple)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = childNotes, onValueChange = onChildNotesChange,
                        label = { Text("Observaciones o recomendaciones") },
                        modifier = Modifier.fillMaxWidth(), minLines = 3,
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TutorPurple, focusedLabelColor = TutorPurple)
                    )
                    Text("El nombre y el avatar se actualizan también en la pantalla principal del niño.", fontSize = 12.sp, color = Color.Gray)
                }
                TutorSection.RUTINAS -> {
                    Text("Administrar rutinas", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Text("Agrega actividades y selecciona un emoji para identificarlas.", color = Color.Gray)
                    Spacer(Modifier.height(14.dp))
                    OutlinedTextField(
                        value = newTask, onValueChange = { newTask = it },
                        label = { Text("Nombre de la actividad") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TutorPurple, focusedLabelColor = TutorPurple)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("Selecciona un emoji", color = DarkBlue, fontWeight = FontWeight.SemiBold)
                    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("⭐","🛏️","🪥","🥣","🎒","📚","🍎","⚽","🛁","🌙","🧸","🚌","🎨","💊","🧹").forEach { emoji ->
                            FilterChip(selected = newTaskEmoji == emoji, onClick = { newTaskEmoji = emoji }, label = { Text(emoji, fontSize = 20.sp) })
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Button(
                        onClick = {
                            if (newTask.isNotBlank()) {
                                routineTasks.add(RoutineTask((routineTasks.maxOfOrNull { it.id } ?: 0) + 1, newTaskEmoji, newTask.trim()))
                                newTask = ""
                                newTaskEmoji = "⭐"
                            }
                        },
                        enabled = newTask.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TutorPurple)
                    ) { Text("＋ Agregar actividad") }
                    Spacer(Modifier.height(16.dp))
                    Text("Actividades (${routineTasks.size})", color = DarkBlue, fontWeight = FontWeight.Bold)
                    routineTasks.forEachIndexed { index, task ->
                        Card(
                            Modifier.fillMaxWidth().padding(vertical = 5.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(task.emoji, fontSize = 27.sp)
                                Spacer(Modifier.width(12.dp))
                                Text("${index + 1}. ${task.title}", Modifier.weight(1f), color = DarkBlue, fontWeight = FontWeight.Medium)
                                TextButton(onClick = { routineTasks.remove(task) }) { Text("Eliminar", color = Color(0xFFB44343)) }
                            }
                        }
                    }
                }
                TutorSection.APOYO -> {
                    Text("Ajustes de comunicación", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Text("Activa o desactiva apoyos de la experiencia infantil.", color = Color.Gray)
                    Spacer(Modifier.height(18.dp))
                    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) { Text("Lectura por voz", fontWeight = FontWeight.Bold, color = DarkBlue); Text("Pronuncia los mensajes seleccionados", fontSize = 12.sp, color = Color.Gray) }
                                Switch(checked = useVoice, onCheckedChange = { useVoice = it })
                            }
                            Divider()
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) { Text("Mostrar pictogramas", fontWeight = FontWeight.Bold, color = DarkBlue); Text("Mantiene visibles los apoyos visuales", fontSize = 12.sp, color = Color.Gray) }
                                Switch(checked = showPictures, onCheckedChange = { showPictures = it })
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    Text("Seguridad del tutor", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { newPin = it.filter(Char::isDigit).take(4) },
                        label = { Text("Nuevo PIN de acceso") },
                        supportingText = { Text("Debe contener 4 números. PIN actual: $accessPin") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TutorPurple, focusedLabelColor = TutorPurple)
                    )
                    Button(
                        onClick = { onPinChanged(newPin) },
                        enabled = newPin.length == 4 && newPin != accessPin,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = TutorPurple),
                        shape = RoundedCornerShape(16.dp)
                    ) { Text("Guardar nuevo PIN") }
                    Text("Los cambios de perfil y rutinas se reflejan en el área infantil mientras la aplicación permanezca abierta.", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun TutorTab(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = LightPurple, selectedLabelColor = TutorPurple))
}

@Composable
private fun TutorInfoCard(emoji: String, title: String, subtitle: String, tag: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(bottom = 10.dp).clickable(onClick = onClick), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 34.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold, color = DarkBlue); Text(subtitle, fontSize = 13.sp, color = Color.Gray) }
            Text("›", fontSize = 28.sp, color = TutorPurple)
        }
    }
}

// ==========================================
// SECCIONES DEL NIÑO
// ==========================================

enum class ChildSection {
    INICIO,
    COMUNICAR,
    RUTINAS,
    EMOCIONES,
    FAVORITOS
}

// ==========================================
// 03. INICIO DEL NIÑO
// ==========================================

@Composable
fun ChildHomeScreen(childName: String, childGender: String, routineTasks: List<RoutineTask>) {

    var selectedSection by remember {
        mutableStateOf(ChildSection.INICIO)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {

        // CONTENIDO PRINCIPAL

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(28.dp))

            // ENCABEZADO

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "¡Hola, ${childName.ifBlank { "amigo" }}! 👋",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkBlue
                    )

                    Text(
                        text = when (selectedSection) {

                            ChildSection.INICIO ->
                                "¿Qué quieres decir hoy?"

                            ChildSection.COMUNICAR ->
                                "Vamos a comunicarnos"

                            ChildSection.RUTINAS ->
                                "Estas son tus rutinas"

                            ChildSection.EMOCIONES ->
                                "¿Cómo te sientes?"

                            ChildSection.FAVORITOS ->
                                "Tus pictogramas favoritos"
                        },
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                }

                Text(
                    text = if (childGender == "Niña") "👧🏻" else "👦🏻",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // CONTENIDO SEGUN SECCION

            when (selectedSection) {

                // INICIO

                ChildSection.INICIO -> {

                    Text(
                        text = "¿Qué quieres hacer?",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        ChildCategoryCard(
                            emoji = "💬",
                            title = "Comunicar",
                            color = LightBlue,
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedSection = ChildSection.COMUNICAR
                        }

                        ChildCategoryCard(
                            emoji = "📅",
                            title = "Rutinas",
                            color = LightOrange,
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedSection = ChildSection.RUTINAS
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        ChildCategoryCard(
                            emoji = "😊",
                            title = "Emociones",
                            color = LightYellow,
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedSection = ChildSection.EMOCIONES
                        }

                        ChildCategoryCard(
                            emoji = "⭐",
                            title = "Favoritos",
                            color = LightPurple,
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedSection = ChildSection.FAVORITOS
                        }
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {

                            Text(
                                text = "💡 ¿Necesitas ayuda?",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkBlue
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Puedes utilizar imágenes para expresar lo que necesitas.",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                // COMUNICAR
                ChildSection.COMUNICAR -> {
                    CommunicationScreen()
                }

                // RUTINAS
                ChildSection.RUTINAS -> {
                    RoutineScreen(routineTasks)
                }

                // EMOCIONES
                ChildSection.EMOCIONES -> {
                    EmotionsScreen()
                }

                // FAVORITOS
                ChildSection.FAVORITOS -> {
                    FavoritesScreen()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ==================================
        // BARRA DE NAVEGACION INFERIOR
        // ==================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .navigationBarsPadding()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ChildNavigationItem(
                emoji = "🏠",
                label = "Inicio",
                selected = selectedSection == ChildSection.INICIO
            ) {
                selectedSection = ChildSection.INICIO
            }

            ChildNavigationItem(
                emoji = "💬",
                label = "Comunicar",
                selected = selectedSection == ChildSection.COMUNICAR
            ) {
                selectedSection = ChildSection.COMUNICAR
            }

            ChildNavigationItem(
                emoji = "📅",
                label = "Rutinas",
                selected = selectedSection == ChildSection.RUTINAS
            ) {
                selectedSection = ChildSection.RUTINAS
            }

            ChildNavigationItem(
                emoji = "⭐",
                label = "Favoritos",
                selected = selectedSection == ChildSection.FAVORITOS
            ) {
                selectedSection = ChildSection.FAVORITOS
            }
        }
    }
}

// ==========================================
// TARJETA DE CATEGORIA
// ==========================================

@Composable
fun ChildCategoryCard(
    emoji: String,
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(145.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = emoji,
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue
            )
        }
    }
}

// ==========================================
// TARJETA DE OPCION
// ==========================================

@Composable
fun ChildOptionCard(
    emoji: String,
    title: String,
    description: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = emoji,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ==========================================
// NAVEGACION INFERIOR
// ==========================================

@Composable
fun ChildNavigationItem(
    emoji: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 5.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = emoji,
            fontSize = 23.sp
        )

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                PrimaryBlue
            } else {
                Color.Gray
            }
        )
    }
}


// ==========================================
// MODELO DE PICTOGRAMA
// ==========================================

data class Pictogram(
    val id: Int,
    val emoji: String,
    val name: String,
    val category: String
)

// ==========================================
// DATOS INICIALES DE PICTOGRAMAS
// ==========================================

val pictogramList = listOf(

    // FRASES

    Pictogram(1, "💬", "Quiero", "Frases"),
    Pictogram(2, "🙋", "Necesito", "Frases"),
    Pictogram(3, "❌", "No quiero", "Frases"),
    Pictogram(4, "✅", "Sí", "Frases"),

    // NECESIDADES

    Pictogram(5, "🥤", "Agua", "Necesidades"),
    Pictogram(6, "🚽", "Baño", "Necesidades"),
    Pictogram(7, "🤝", "Ayuda", "Necesidades"),
    Pictogram(8, "😴", "Dormir", "Necesidades"),

    // COMIDA

    Pictogram(9, "🍎", "Manzana", "Comida"),
    Pictogram(10, "🍞", "Pan", "Comida"),
    Pictogram(11, "🥛", "Leche", "Comida"),
    Pictogram(12, "🍌", "Plátano", "Comida"),
    Pictogram(13, "🍔", "Hamburguesa", "Comida"),
    Pictogram(14, "🍕", "Pizza", "Comida"),

    // EMOCIONES

    Pictogram(15, "😊", "Feliz", "Emociones"),
    Pictogram(16, "😢", "Triste", "Emociones"),
    Pictogram(17, "😠", "Enojado", "Emociones"),
    Pictogram(18, "😴", "Cansado", "Emociones"),
    Pictogram(19, "😨", "Asustado", "Emociones"),
    Pictogram(20, "😌", "Tranquilo", "Emociones"),

    // ACTIVIDADES

    Pictogram(21, "⚽", "Jugar", "Actividades"),
    Pictogram(22, "📺", "Ver televisión", "Actividades"),
    Pictogram(23, "🎨", "Dibujar", "Actividades"),
    Pictogram(24, "📖", "Leer", "Actividades"),

    // PERSONAS

    Pictogram(25, "👩", "Mamá", "Personas"),
    Pictogram(26, "👨", "Papá", "Personas"),
    Pictogram(27, "👦", "Amigo", "Personas")
)

// ==========================================
// MODULO DE COMUNICACION
// ==========================================

@Composable
fun CommunicationScreen() {

    val context = LocalContext.current

    // ESTADO DE LOS PICTOGRAMAS

    var selectedPictograms by remember {
        mutableStateOf(listOf<Pictogram>())
    }

    var selectedCategory by remember {
        mutableStateOf("Todos")
    }

    // MOTOR DE VOZ COMPARTIDO
    val speak = rememberPicToSaySpeaker()

    // CATEGORIAS

    val categories = listOf(
        "Todos",
        "Frases",
        "Necesidades",
        "Comida",
        "Emociones",
        "Actividades",
        "Personas"
    )

    // FILTRADO

    val filteredPictograms = pictogramList.filter {

        selectedCategory == "Todos" ||
                it.category == selectedCategory
    }

    // TEXTO CONSTRUIDO

    val message = selectedPictograms.joinToString(" ") {
        it.name
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // ==================================
        // TITULO
        // ==================================

        Text(
            text = "Mis pictogramas",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Text(
            text = "Selecciona imágenes para comunicarte",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ==================================
        // CONSTRUCTOR DE MENSAJE
        // ==================================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightBlue
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "🗣️ Mi mensaje",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(
                        onClick = {
                            selectedPictograms = emptyList()
                        },
                        enabled = selectedPictograms.isNotEmpty()
                    ) {
                        Text("Borrar todo")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // PICTOGRAMAS SELECCIONADOS

                if (selectedPictograms.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Selecciona un pictograma",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }

                } else {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        selectedPictograms.forEachIndexed { index, pictogram ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = pictogram.emoji,
                                        fontSize = 30.sp
                                    )

                                    Spacer(
                                        modifier = Modifier.width(12.dp)
                                    )

                                    Text(
                                        text = pictogram.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkBlue,
                                        modifier = Modifier.weight(1f)
                                    )

                                    TextButton(
                                        onClick = {
                                            selectedPictograms =
                                                selectedPictograms.filterIndexed {
                                                        i, _ -> i != index
                                                }
                                        }
                                    ) {
                                        Text(
                                            text = "✕",
                                            color = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // TEXTO FINAL

                Text(
                    text = if (message.isBlank()) {
                        "Tu mensaje aparecerá aquí"
                    } else {
                        message
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue
                )

                Spacer(modifier = Modifier.height(15.dp))

                // REPRODUCIR

                Button(
                    onClick = {

                        speak(message)
                    },
                    enabled = message.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue
                    )
                ) {

                    Text(
                        text = "🔊 Reproducir mensaje",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // AGREGAR PICTOGRAMA

                OutlinedButton(
                    onClick = {
                        if (selectedPictograms.isNotEmpty()) {
                            selectedPictograms =
                                selectedPictograms.dropLast(1)
                        }
                    },
                    enabled = selectedPictograms.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("↶ Deshacer último pictograma")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ==================================
        // CATEGORIAS
        // ==================================

        Text(
            text = "Categorías",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            categories.forEach { category ->

                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        selectedCategory = category
                    },
                    label = {
                        Text(category)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ==================================
        // LISTA DE PICTOGRAMAS
        // ==================================

        Text(
            text = selectedCategory,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        filteredPictograms.chunked(2).forEach { rowItems ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                rowItems.forEach { pictogram ->

                    PictogramCard(
                        pictogram = pictogram,
                        onClick = {

                            selectedPictograms =
                                selectedPictograms + pictogram
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

// ==========================================
// TARJETA INDIVIDUAL DE PICTOGRAMA
// ==========================================

@Composable
fun PictogramCard(
    pictogram: Pictogram,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .height(125.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
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
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = pictogram.emoji,
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = pictogram.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                textAlign = TextAlign.Center
            )
        }
    }
}


// ==========================================
// MODELO Y PANTALLA DE RUTINAS
// ==========================================

@Composable
fun RoutineScreen(activities: List<RoutineTask>) {
    val completed = remember { mutableStateListOf<Int>() }
    val progress = if (activities.isEmpty()) 0f else completed.count { id -> activities.any { it.id == id } }.toFloat() / activities.size
    LaunchedEffect(activities.map { it.id }) { completed.retainAll(activities.map { it.id }.toSet()) }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Mi rutina de hoy 📅", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
        Text("¡Vamos a completar nuestras actividades!", fontSize = 14.sp, color = Color.Gray)
        Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = LightBlue)) {
            Column(Modifier.padding(16.dp)) {
                Text("Mi progreso", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                Spacer(Modifier.height(8.dp))
                Text("${completed.size} de ${activities.size} actividades completadas", fontSize = 14.sp, color = DarkBlue)
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(10.dp), color = PrimaryBlue, trackColor = Color.White)
                Spacer(Modifier.height(6.dp))
                Text("${(progress * 100).toInt()}% completado", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }
        }
        Text("Actividades", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
        if (activities.isEmpty()) Text("Tu tutor todavía no ha agregado actividades.", color = Color.Gray)
        activities.forEach { activity ->
            val isCompleted = activity.id in completed
            Card(
                Modifier.fillMaxWidth().clickable { if (isCompleted) completed.remove(activity.id) else completed.add(activity.id) },
                shape = RoundedCornerShape(17.dp),
                colors = CardDefaults.cardColors(containerColor = if (isCompleted) Color(0xFFDDF4E5) else Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(activity.emoji, fontSize = 34.sp)
                    Spacer(Modifier.width(13.dp))
                    Text(activity.title, Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                    Checkbox(checked = isCompleted, onCheckedChange = { checked -> if (checked) completed.add(activity.id) else completed.remove(activity.id) }, colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue))
                }
            }
        }
        if (activities.isNotEmpty() && completed.size == activities.size) {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF4E5)), shape = RoundedCornerShape(18.dp)) {
                Text("🎉 ¡Excelente! ¡Terminaste todas tus actividades!", Modifier.padding(18.dp), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF237A46))
            }
        }
        OutlinedButton(onClick = { completed.clear() }, enabled = completed.isNotEmpty(), modifier = Modifier.fillMaxWidth()) { Text("Reiniciar rutina") }
    }
}

// ==========================================
// MODULO DE EMOCIONES
// ==========================================

private data class EmotionItem(
    val emoji: String,
    val name: String,
    val description: String,
    val color: Color
)

@Composable
fun EmotionsScreen() {
    val emotions = remember {
        listOf(
            EmotionItem("😊", "Feliz", "Me siento muy bien", Color(0xFFFFF2C9)),
            EmotionItem("😢", "Triste", "Me siento triste", Color(0xFFE8F0FF)),
            EmotionItem("😠", "Enojado", "Estoy molesto", Color(0xFFFFE5E5)),
            EmotionItem("😴", "Cansado", "Necesito descansar", Color(0xFFF0E9FF)),
            EmotionItem("😨", "Asustado", "Tengo miedo", Color(0xFFECE9FF)),
            EmotionItem("😌", "Tranquilo", "Me siento tranquilo", Color(0xFFDDF4E5))
        )
    }
    var selectedEmotion by remember { mutableStateOf<EmotionItem?>(null) }
    val speak = rememberPicToSaySpeaker()

    Column(Modifier.fillMaxWidth()) {
        Text("¿Cómo te sientes? 😊", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
        Text(
            "Selecciona la emoción que representa cómo te sientes.",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(18.dp))

        emotions.chunked(2).forEach { rowEmotions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowEmotions.forEach { emotion ->
                    val selected = selectedEmotion == emotion
                    Card(
                        modifier = Modifier.weight(1f).height(140.dp).clickable {
                            selectedEmotion = emotion
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = emotion.color),
                        border = if (selected) {
                            androidx.compose.foundation.BorderStroke(2.dp, PrimaryBlue)
                        } else null
                    ) {
                        Column(
                            Modifier.fillMaxSize().padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(emotion.emoji, fontSize = 43.sp)
                            Spacer(Modifier.height(7.dp))
                            Text(emotion.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                            if (selected) {
                                Text("Seleccionado ✓", fontSize = 11.sp, color = PrimaryBlue)
                            }
                        }
                    }
                }
                if (rowEmotions.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }

        selectedEmotion?.let { emotion ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlue)
            ) {
                Column(
                    Modifier.fillMaxWidth().padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(emotion.emoji, fontSize = 55.sp)
                    Text(
                        "Me siento ${emotion.name.lowercase()}",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )
                    Text(emotion.description, fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { speak("Me siento ${emotion.name.lowercase()}") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("🔊 Comunicar emoción")
                    }
                }
            }
        }
    }
}

// ==========================================
// MODULO DE FAVORITOS
// ==========================================

private data class FavoriteItem(
    val id: Int,
    val emoji: String,
    val message: String,
    val description: String
)

@Composable
fun FavoritesScreen() {
    val favorites = remember {
        mutableStateListOf(
            FavoriteItem(1, "🥤", "Quiero agua", "Tengo sed"),
            FavoriteItem(2, "🍎", "Quiero comer", "Tengo hambre"),
            FavoriteItem(3, "🚽", "Quiero ir al baño", "Necesito ir al baño"),
            FavoriteItem(4, "🤝", "Necesito ayuda", "Pedir ayuda"),
            FavoriteItem(5, "⚽", "Quiero jugar", "Tiempo de diversión"),
            FavoriteItem(6, "😴", "Quiero descansar", "Necesito descansar")
        )
    }
    val speak = rememberPicToSaySpeaker()

    Column(Modifier.fillMaxWidth()) {
        Text("⭐ Mis favoritos", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
        Text("Toca un mensaje para escucharlo.", fontSize = 14.sp, color = Color.Gray)
        Spacer(Modifier.height(18.dp))

        if (favorites.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    "Todavía no tienes favoritos.",
                    modifier = Modifier.padding(20.dp),
                    color = Color.Gray
                )
            }
        }

        favorites.forEach { favorite ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(58.dp).background(LightBlue, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(favorite.emoji, fontSize = 34.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(favorite.message, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                        Text(favorite.description, fontSize = 12.sp, color = Color.Gray)
                    }
                    IconButton(onClick = { speak(favorite.message) }) {
                        Text("🔊", fontSize = 23.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// MOTOR DE VOZ REUTILIZABLE
// ==========================================

@Composable
fun rememberPicToSaySpeaker(): (String) -> Unit {
    val context = LocalContext.current
    var engine by remember { mutableStateOf<TextToSpeech?>(null) }
    var ready by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        val engineHolder = arrayOfNulls<TextToSpeech>(1)
        val createdEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = engineHolder[0]?.setLanguage(Locale("es", "MX"))
                ready = result != null &&
                        result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
        engineHolder[0] = createdEngine
        engine = createdEngine

        onDispose {
            createdEngine.stop()
            createdEngine.shutdown()
            engine = null
            ready = false
        }
    }

    return { message ->
        if (ready && message.isNotBlank()) {
            engine?.speak(
                message,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "pictosay_child_message"
            )
        }
    }
}
