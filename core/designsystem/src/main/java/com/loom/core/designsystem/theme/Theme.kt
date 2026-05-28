package com.loom.core.designsystem.theme


import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Esquema por defecto (Light)
val LightDefaultColorScheme = lightColorScheme(
    primary = DarkBlue20,
    onPrimary = White,
    primaryContainer = DarkBlue90,
    onPrimaryContainer =  DarkBlue20,
    background =  DarkBlue20,
    surface = DarkBlue20,
    onSurface = Grey50,
    surfaceVariant = DarkBlue35,
)

// Esquema por defecto (Dark)
val DarkDefaultColorScheme = darkColorScheme(
    /*primary = DarkBlue20,
    onPrimary = White,
    primaryContainer = DarkBlue90,
    onPrimaryContainer =  DarkBlue20,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = Grey30,
    surfaceVariant = DarkBlue35,
    secondary = Grey,*/


    primary = DarkBlue20,
    onPrimary =  White,
    primaryContainer =  DarkBlue90,
    onPrimaryContainer =  DarkBlue20,
    inversePrimary =  Color.Red,
    secondary =  Grey,
    onSecondary = White,
    secondaryContainer =  Color.Transparent,
    onSecondaryContainer =  Color.Transparent,
    tertiary =   Color.Transparent,
    onTertiary =   Color.Transparent,
    tertiaryContainer =   Color.Transparent,
    onTertiaryContainer =  Color.Transparent,
    background =   Black,
    /*onBackground =  White,
    surface =   Color.Black,
    onSurface =   Color.Yellow,
    surfaceVariant =   Color.Transparent,
    onSurfaceVariant =  White,
    surfaceTint =  Color.Transparent,
    inverseSurface =   Color.Transparent,
    inverseOnSurface =  Color.Transparent,
    error =   Color.Transparent,
    onError =   Color.Transparent,
    errorContainer =   Color.Transparent,
    onErrorContainer =  Color.Transparent,
    outline =   Color.White,
    outlineVariant =  Color.Transparent,
    scrim =   Color.Transparent,
    surfaceBright =   Color.Transparent,
    surfaceContainer =   Color.Transparent,
    surfaceContainerHigh =   Color.Transparent,
    surfaceContainerHighest =   Color.Transparent,
    surfaceContainerLow =   Color.Transparent,
    surfaceContainerLowest =   Color.Transparent,
    surfaceDim =   Color.Transparent,
    primaryFixed =   Color.Transparent,
    primaryFixedDim =  Color.Transparent,
    onPrimaryFixed =   Color.Transparent,
    onPrimaryFixedVariant =  Color.Transparent,
    secondaryFixed =   Color.Transparent,
    secondaryFixedDim =  Color.Transparent,
    onSecondaryFixed =  Color.Transparent,
    onSecondaryFixedVariant =  Color.Transparent,
    tertiaryFixed =   Color.Transparent,
    tertiaryFixedDim =  Color.Transparent,
    onTertiaryFixed =  Color.Transparent,
    onTertiaryFixedVariant =   Color.Transparent,*/
)

@Composable
fun LoomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }

    val backgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )

    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides TintTheme(iconTint = colorScheme.primary)
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = LoomTypography,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S