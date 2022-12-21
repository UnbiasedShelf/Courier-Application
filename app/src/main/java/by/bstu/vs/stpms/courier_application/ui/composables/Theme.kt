package by.bstu.vs.stpms.courier_application.ui.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val logoBlue = Color(0xFF1565C0)
private val accentRed = Color(0xFFF50057)
private val accentRedVariant = Color(0xFFCA0048)
private val black = Color.Black
private val white = Color.White

private val LightColors = lightColors(
    primary = logoBlue,
    primaryVariant = logoBlue,
    secondary = accentRed,
    secondaryVariant = accentRedVariant,
    onPrimary = white,
    onSecondary = black,
    background = white,
    surface = white,
    onBackground = black,
    onSurface = black,
    error = accentRedVariant,
    onError = black
)

private val logoBlueDark = Color(0xFF0B386B)
private val accentRedDark = Color(0xFF940035)
private val accentRedVariantDark = Color(0xFF7A002C)

private val DarkColors = darkColors(
    primary = logoBlueDark,
    primaryVariant = logoBlueDark,
    secondary = accentRedDark,
    secondaryVariant = accentRedVariantDark,
    onPrimary = white,
    onSecondary = white,
    error = accentRedVariantDark,
    onError = white
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    ) {
        val systemUiController = rememberSystemUiController()
        val statusBarColor = MaterialTheme.colors.primarySurface
        val useDarkIcons = statusBarColor.luminance() > 0.5f

        SideEffect {
            systemUiController.setStatusBarColor(
                color = statusBarColor,
                darkIcons = useDarkIcons
            )
        }
        content()
    }
}