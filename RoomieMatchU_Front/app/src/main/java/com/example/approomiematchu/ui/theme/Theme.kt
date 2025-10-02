package com.example.approomiematchu.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColors = lightColorScheme(
    primary = DarkBlue,
    onPrimary = White,
    secondary = Orange,
    onSecondary = White,
    tertiary = Blue,
    background = White,
    onBackground = DarkBlue,
    surface = LightGray,
    onSurface = DarkBlue
)

private val DarkColors = darkColorScheme(
    primary = LightBlue,
    onPrimary = Black,
    secondary = Orange,
    onSecondary = Black,
    tertiary = Blue,
    background = DarkBlue,
    onBackground = White,
    surface = Blue,
    onSurface = White
)

@Composable
fun RoomieMatchUTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(16.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        typography = AppMaterialTypography,
        shapes = shapes,
        content = content
    )
}
