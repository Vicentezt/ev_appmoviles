package com.example.dsy1105_004v2026.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AzulOscuro,
    secondary = TurquesaOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    secondary = Turquesa,
    secondaryContainer = TurquesaClaro,
    onSecondaryContainer = Color(0xFF004D40),
    background = FondoClaro,
    surface = Color.White
)

@Composable
fun DSY1105004V2026Theme(
    darkTheme: Boolean = false,   // se usa siempre el tema claro para mantener el diseño
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
