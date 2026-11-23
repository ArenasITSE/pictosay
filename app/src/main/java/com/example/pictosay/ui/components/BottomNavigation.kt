
package com.example.pictosay.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class BottomItem(
    val route: String,
    val label: String,
    val emoji: String
)

private val items = listOf(
    BottomItem("home", "Inicio", "🏡"),
    BottomItem("communication", "Comunicar", "💬"),
    BottomItem("tasks", "Rutinas", "📅"),
    BottomItem("favorites", "Favoritos", "⭐")
)

@Composable
fun BottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        contentColor = Color(0xFF3978D5)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    Text(item.emoji)
                },
                label = {
                    Text(item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF3978D5),
                    selectedTextColor = Color(0xFF3978D5),
                    indicatorColor = Color(0xFFE8F0FF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
