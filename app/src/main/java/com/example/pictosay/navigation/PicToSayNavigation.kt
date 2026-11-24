package com.example.pictosay.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pictosay.data.local.ChildProfileEntity
import com.example.pictosay.data.local.CategoryEntity
import com.example.pictosay.data.local.EmotionEntity
import com.example.pictosay.data.local.PictogramEntity
import com.example.pictosay.data.local.TaskEntity
import com.example.pictosay.data.repository.PicToSayRepository
import com.example.pictosay.ui.communication.CommunicationScreen
import com.example.pictosay.ui.components.BottomNavigation
import com.example.pictosay.ui.components.Mascot
import com.example.pictosay.ui.emotions.EmotionsScreen
import com.example.pictosay.ui.environment.EnvironmentScreen
import com.example.pictosay.ui.favorites.FavoritesScreen
import com.example.pictosay.ui.home.ChildHomeScreen
import com.example.pictosay.ui.splash.SplashScreen
import com.example.pictosay.ui.tasks.TasksScreen
import com.example.pictosay.ui.tutor.TutorDashboardScreen
import com.example.pictosay.ui.tutor.ManageEnvironmentsScreen
import com.example.pictosay.ui.tutor.ManagePictogramsScreen
import com.example.pictosay.ui.tutor.ManageEmotionsScreen
import com.example.pictosay.ui.tutor.ManageRoutinesScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.pictosay.ui.environment.EnvironmentScreen
import com.example.pictosay.ui.emotions.EmotionsScreen
object AppRoutes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val COMMUNICATION = "communication"
    const val ENVIRONMENT = "environment"
    const val TASKS = "tasks"
    const val EMOTIONS = "emotions"
    const val FAVORITES = "favorites"
    const val TUTOR_DASHBOARD = "tutor_dashboard"
    const val TUTOR_ENVIRONMENTS = "tutor_environments"
    const val TUTOR_PICTOGRAMS = "tutor_pictograms"
    const val TUTOR_PICTOGRAMS_WITH_ID = "tutor_pictograms/{categoryId}"
    const val TUTOR_EMOTIONS = "tutor_emotions"
    const val TUTOR_ROUTINES = "tutor_routines"
}

private val PrimaryBlue = Color(0xFF3978D5)
private val DarkBlue = Color(0xFF092B70)
private val BackgroundColor = Color(0xFFF7F9FC)

@Composable
fun PicToSayNavigation(
    repository: PicToSayRepository,
    onSpeak: (String) -> Unit = {}
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomRoutes = setOf(
        AppRoutes.HOME,
        AppRoutes.COMMUNICATION,
        AppRoutes.TASKS,
        AppRoutes.FAVORITES
    )

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            if (currentRoute in bottomRoutes) {
                BottomNavigation(
                    currentRoute = currentRoute ?: AppRoutes.HOME,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(
                                navController.graph.findStartDestination().id
                            ) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = AppRoutes.SPLASH,
            modifier = Modifier.padding(padding)
        ) {

            // SPLASH
            composable(AppRoutes.SPLASH) {
                SplashRoute(
                    repository = repository,
                    navController = navController
                )
            }

            // REGISTRO
            composable(AppRoutes.ONBOARDING) {
                OnboardingRoute(
                    repository = repository,
                    navController = navController
                )
            }

            // INICIO
            composable(AppRoutes.HOME) {
                val profile by repository.childProfile.collectAsState(
                    initial = null
                )

                ChildHomeScreen(
                    profile = profile,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            // COMUNICACIÓ
            composable(AppRoutes.COMMUNICATION) {
                CommunicationScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onOpenEnvironments = {
                        navController.navigate(AppRoutes.ENVIRONMENT)
                    },
                    onSpeak = onSpeak,
                    repository = repository
                )
            }


            composable(AppRoutes.ENVIRONMENT) {
                EnvironmentScreen(
                    repository = repository,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSpeak = onSpeak,
                    onAddPhrase = { phrase ->
                        // Se conserva el callback para futuras funciones.
                    }
                )
            }


            // RUTINAS
            composable(AppRoutes.TASKS) {
                TasksScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }



            composable(AppRoutes.EMOTIONS) {

                val emotions by repository.emotions.collectAsState(
                    initial = emptyList()
                )

                EmotionsScreen(
                    emotions = emotions,
                    onBack = {
                        navController.popBackStack()
                    },
                    onSpeak = onSpeak,
                    onAddPhrase = { phrase ->
                        // Se conserva el callback para futuras funciones.
                    }
                )
            }



            // FAVORITOS
            composable(AppRoutes.FAVORITES) {
                FavoritesScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSpeak = onSpeak,
                    onOpenTutor = {
                        navController.navigate(AppRoutes.TUTOR_DASHBOARD)
                    },
                    repository = repository
                )
            }

            // ========================================
            // PANEL DEL TUTOR
            // ========================================

            composable(AppRoutes.TUTOR_DASHBOARD) {
                TutorDashboardScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onManageEnvironments = {
                        navController.navigate(AppRoutes.TUTOR_ENVIRONMENTS)
                    },
                    onManagePictograms = {
                        navController.navigate(AppRoutes.TUTOR_ENVIRONMENTS)
                    },
                    onManageEmotions = {
                        navController.navigate(AppRoutes.TUTOR_EMOTIONS)
                    },
                    onManageRoutines = {
                        navController.navigate(AppRoutes.TUTOR_ROUTINES)
                    }
                )
            }

            // ADMINISTRAR AMBIENTES
            composable(AppRoutes.TUTOR_ENVIRONMENTS) {
                val categories by repository.categories.collectAsState(
                    initial = emptyList()
                )
                val scope = rememberCoroutineScope()

                ManageEnvironmentsScreen(
                    categories = categories,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddCategory = { category ->
                        scope.launch {
                            repository.addCategory(category)
                        }
                    },
                    onUpdateCategory = { category ->
                        scope.launch {
                            repository.updateCategory(category)
                        }
                    },
                    onDeleteCategory = { categoryId ->
                        scope.launch {
                            repository.deleteCategory(categoryId)
                        }
                    },
                    onManagePictograms = { category ->
                        navController.navigate("tutor_pictograms/${category.id}")
                    }
                )
            }

            // ADMINISTRAR PICTOGRAMAS DE UN AMBIENTE
            composable(AppRoutes.TUTOR_PICTOGRAMS_WITH_ID) { entry ->
                val categoryId = entry.arguments
                    ?.getString("categoryId")
                    ?.toIntOrNull()

                if (categoryId == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                } else {
                    val category by produceState<CategoryEntity?>(
                        initialValue = null,
                        key1 = categoryId
                    ) {
                        value = repository.getCategoryById(categoryId)
                    }

                    val pictograms by repository
                        .getAllPictogramsByCategory(categoryId)
                        .collectAsState(initial = emptyList())

                    val scope = rememberCoroutineScope()

                    if (category != null) {
                        ManagePictogramsScreen(
                            category = category!!,
                            pictograms = pictograms,
                            onBack = {
                                navController.popBackStack()
                            },
                            onAddPictogram = { pictogram ->
                                scope.launch {
                                    repository.addPictogram(pictogram)
                                }
                            },
                            onUpdatePictogram = { pictogram ->
                                scope.launch {
                                    repository.updatePictogram(pictogram)
                                }
                            },
                            onTogglePictogram = { pictogram ->
                                scope.launch {
                                    repository.togglePictogram(pictogram)
                                }
                            },
                            onDeletePictogram = { pictogramId ->
                                scope.launch {
                                    repository.deletePictogram(pictogramId)
                                }
                            }
                        )
                    }
                }
            }

            // ADMINISTRAR EMOCIONES
            composable(AppRoutes.TUTOR_EMOTIONS) {
                val emotions by repository.allEmotions.collectAsState(
                    initial = emptyList()
                )
                val scope = rememberCoroutineScope()

                ManageEmotionsScreen(
                    emotions = emotions,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddEmotion = { emotion ->
                        scope.launch {
                            repository.addEmotion(emotion)
                        }
                    },
                    onUpdateEmotion = { emotion ->
                        scope.launch {
                            repository.updateEmotion(emotion)
                        }
                    },
                    onToggleEmotion = { emotion ->
                        scope.launch {
                            if (emotion.isActive) {
                                repository.deactivateEmotion(emotion.id)
                            } else {
                                repository.updateEmotion(
                                    emotion.copy(isActive = true)
                                )
                            }
                        }
                    },
                    onDeleteEmotion = { emotionId ->
                        scope.launch {
                            repository.deleteEmotion(emotionId)
                        }
                    }
                )
            }

            // ADMINISTRAR RUTINAS
            composable(AppRoutes.TUTOR_ROUTINES) {
                val tasks by repository.tasks.collectAsState(
                    initial = emptyList()
                )
                val scope = rememberCoroutineScope()

                ManageRoutinesScreen(
                    tasks = tasks,
                    onBack = {
                        navController.popBackStack()
                    },
                    onAddTask = { task ->
                        scope.launch {
                            repository.addTask(task)
                        }
                    },
                    onUpdateTask = { task ->
                        scope.launch {
                            repository.updateTask(task)
                        }
                    },
                    onDeleteTask = { taskId ->
                        scope.launch {
                            repository.deleteTask(taskId)
                        }
                    }
                )
            }
        }
    }
}

// ------------------------------------
// SPLASH
// ------------------------------------

@Composable
private fun SplashRoute(
    repository: PicToSayRepository,
    navController: NavHostController
) {
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            repository.initializeDefaultData()
            delay(2500)

            val profile = repository.getChildProfile()

            val destination = if (profile == null) {
                AppRoutes.ONBOARDING
            } else {
                AppRoutes.HOME
            }

            navController.navigate(destination) {
                popUpTo(AppRoutes.SPLASH) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } catch (exception: Exception) {
            error = true
        }
    }

    if (error) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "No se pudo iniciar PicToSay",
                color = DarkBlue,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = {
                error = false
                navController.navigate(AppRoutes.SPLASH) {
                    popUpTo(AppRoutes.SPLASH) {
                        inclusive = true
                    }
                }
            }) {
                Text("Intentar nuevamente")
            }
        }
    } else {
        SplashScreen()
    }
}

// ------------------------------------
// ONBOARDING
// ------------------------------------

@Composable
private fun OnboardingRoute(
    repository: PicToSayRepository,
    navController: NavHostController
) {
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("BOY") }
    var saving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¡Hola! 👋",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBlue
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Primero, vamos a conocernos.",
            fontSize = 18.sp,
            color = DarkBlue
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                error = null
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text("¿Cómo te llamas?") },
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "Elige tu compañero",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MascotOption(
                modifier = Modifier.weight(1f),
                gender = "BOY",
                selected = gender == "BOY",
                onClick = { gender = "BOY" }
            )

            MascotOption(
                modifier = Modifier.weight(1f),
                gender = "GIRL",
                selected = gender == "GIRL",
                onClick = { gender = "GIRL" }
            )
        }

        if (error != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                error!!,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isBlank()) {
                    error = "Escribe tu nombre para continuar."
                } else if (!saving) {
                    saving = true
                }
            },
            enabled = name.isNotBlank() && !saving,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                if (saving) "Guardando..." else "¡Vamos a comenzar!"
            )
        }
    }

    LaunchedEffect(saving) {
        if (saving) {
            try {
                repository.saveChildProfile(
                    ChildProfileEntity(
                        name = name.trim(),
                        gender = gender,
                        mascot = if (gender == "GIRL") "PINK" else "BLUE",
                        onboardingCompleted = true
                    )
                )

                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.ONBOARDING) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            } catch (exception: Exception) {
                error = "No se pudo guardar el perfil. Intenta nuevamente."
                saving = false
            }
        }
    }
}

@Composable
private fun MascotOption(
    modifier: Modifier = Modifier,
    gender: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        border = if (selected) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                PrimaryBlue
            )
        } else null,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                Color(0xFFE8F0FF)
            } else {
                Color.White
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Mascot(
                gender = gender,
                size = 95.dp
            )

            Text(
                if (gender == "BOY") "Niño" else "Niña",
                color = DarkBlue,
                fontWeight = FontWeight.Bold
            )

            if (selected) {
                Text(
                    "Seleccionado ✓",
                    color = PrimaryBlue,
                    fontSize = 12.sp
                )
            }
        }
    }
}
