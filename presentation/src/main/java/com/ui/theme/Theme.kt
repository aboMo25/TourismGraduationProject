package com.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.automatedtourismproject.ui.theme.Typography

private val DarkColorScheme = darkColorScheme(
	primary = NightSky,
	secondary = SunsetOrange,
	tertiary = OasisGreen,
	background = Color.Black,
	surface = DesertBeige,
	onPrimary = Color.White,
	onSecondary = Color.Black,
	onBackground = Color.White,
	onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
	primary = BlueNile,
	secondary = GoldenPyramid,
	tertiary = OasisGreen,
	background = DesertBeige,
	surface = Color.White,
	onPrimary = Color.White,
	onSecondary = Color.Black,
	onBackground = Color.Black,
	onSurface = Color.Black
)

@Composable
fun AutomatedTourismProjectTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}
		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}
