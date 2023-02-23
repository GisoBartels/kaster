package app.kaster.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun KasterTheme(content: @Composable () -> Unit) = MaterialTheme(colors = themeColors(), content = content)

@Composable
private fun themeColors() =
    if (isSystemInDarkTheme())
        darkColors(primary = Color(0xFF00CC00), secondary = Color(0xFF00CC00), background = Color.Black)
    else
        lightColors(primary = Color(0xFF005000), secondary = Color(0xFF005000))
