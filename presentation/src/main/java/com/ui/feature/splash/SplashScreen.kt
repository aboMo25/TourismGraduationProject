package com.ui.feature.splash

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.navigation.LoginScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
	navController: NavController,
	viewModel: SplashViewModel = koinViewModel()
) {
	val currentIndex by viewModel.currentIndex.collectAsState()
	val currentSplashData by viewModel.currentSplashData.collectAsState()
	val shouldNavigateToLogin by viewModel.shouldNavigateToLogin.collectAsState()


	LaunchedEffect(shouldNavigateToLogin) {
		if (shouldNavigateToLogin) {
			navController.navigate(LoginScreen) {
				popUpTo(LoginScreen) { inclusive = true }
			}
			viewModel.resetNavigation()
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		// Background Image
		AsyncImage(
			model = ImageRequest.Builder(LocalContext.current)
				.data(currentSplashData.image)
				.crossfade(true)
				.memoryCachePolicy(CachePolicy.ENABLED)
				.diskCachePolicy(CachePolicy.ENABLED)
				.build(),
			contentDescription = null,
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.Crop
		)

		// Gradient overlay
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(
					Brush.verticalGradient(
						colors = listOf(
							Color.Black.copy(alpha = 0.3f),
							Color.Black.copy(alpha = 0.7f)
						)
					)
				)
		)

		// Content
		SplashContent(
			splashScreenData = currentSplashData,
			modifier = Modifier.fillMaxSize()
		)

		// Skip button
		if (currentIndex < 2) {
			SkipButton(
				onSkipClick = {
					navController.navigate(LoginScreen) {
						popUpTo(LoginScreen) { inclusive = true }
					}
				},
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(24.dp)
			)
		}

		Column(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.padding(bottom = 32.dp)
		) {
			// Screen Indicators
			Row(
				modifier = Modifier
					.padding(bottom = 24.dp)
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				repeat(3) { index ->
					Box(
						modifier = Modifier
							.padding(horizontal = 4.dp)
							.size(if (currentIndex == index) 10.dp else 8.dp)
							.clip(CircleShape)
							.background(
								if (currentIndex == index)
									Color(0xFFE65100)
								else
									Color.White.copy(alpha = 0.5f)
							)
					)
				}
			}

			// Next button
			NextButton(
				onNext = { viewModel.onNextClicked() },
				isLastScreen = currentIndex == 2,
				modifier = Modifier.padding(horizontal = 32.dp)
			)
		}
	}
}



@Composable
fun SplashContent(
	splashScreenData: SplashScreenData,
	modifier: Modifier = Modifier
) {
	var isVisible by remember { mutableStateOf(false) }

	LaunchedEffect(Unit) {
		delay(500)
		isVisible = true
	}
	Box(
		modifier = modifier
			.fillMaxSize()
			.padding(24.dp),
		contentAlignment = Alignment.Center // Centers content inside Box
	) {
		Text(
			text = "Automated Tourism",
			style = MaterialTheme.typography.headlineLarge,
			color = Color(0xFFE65100),
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(horizontal = 16.dp)
		)
	}

	Box(
		modifier = modifier
			.fillMaxSize()
			.padding(24.dp),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Bottom,
			modifier = Modifier
				.fillMaxSize()
				.padding(bottom = 160.dp)
		) {
			AnimatedVisibility(
				visible = isVisible,
				enter = fadeIn() + slideInVertically()
			) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.clip(RoundedCornerShape(16.dp))
						.background(Color(0xFFF5E1C0).copy(alpha = 0.8f)) // Sand color background
						.padding(24.dp)
				) {
					Text(
						text = splashScreenData.title,
						style = MaterialTheme.typography.headlineMedium,
						fontWeight = FontWeight.Bold,
						color = Color(0xFF6D4C41),
						textAlign = TextAlign.Center,
						modifier = Modifier.padding(horizontal = 16.dp)
					)
					Spacer(modifier = Modifier.height(12.dp))
					Text(
						text = splashScreenData.description,
						style = MaterialTheme.typography.bodyLarge,
						color = Color(0xFF6D4C41),
						textAlign = TextAlign.Center,
						modifier = Modifier.padding(horizontal = 16.dp)
					)
				}
			}
		}
	}
}

@Composable
fun NextButton(
	onNext: () -> Unit,
	isLastScreen: Boolean,
	modifier: Modifier = Modifier
) {
	val scale = remember { Animatable(1f) }

	LaunchedEffect(isLastScreen) {
		if (isLastScreen) {
			while (true) {
				scale.animateTo(
					targetValue = 1.05f,
					animationSpec = tween(500, easing = LinearEasing)
				)
				scale.animateTo(
					targetValue = 1f,
					animationSpec = tween(500, easing = LinearEasing)
				)
				delay(1000)
			}
		}
	}

	Button(
		onClick = onNext,
		modifier = modifier
			.fillMaxWidth()
			.scale(scale.value),
		colors = ButtonDefaults.buttonColors(
			containerColor = Color(0xFFE65100),
			contentColor = Color.White // Ensuring text is always white for better contrast
		),
		shape = RoundedCornerShape(16.dp),
		elevation = ButtonDefaults.buttonElevation(
			defaultElevation = 6.dp,
			pressedElevation = 8.dp
		)
	) {
		Text(
			text = if (isLastScreen) "Get Started" else "Next",
			style = MaterialTheme.typography.titleMedium.copy(
				color = Color(0xFFF5E1C0), // Explicitly setting text color
				shadow = Shadow(
					color = Color.Black.copy(alpha = 0.25f),
					blurRadius = 4f
				)
			),
			fontWeight = FontWeight.Bold,
			modifier = Modifier.padding(vertical = 8.dp)
		)
	}
}

@Composable
fun SkipButton(
	onSkipClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Surface(
		modifier = modifier,
		color = Color(0xFFE65100).copy(alpha = 0.8f),
		shape = RoundedCornerShape(20.dp)
	) {
		TextButton(
			onClick = onSkipClick,
			modifier = Modifier.padding(horizontal = 8.dp)
		) {
			Text(
				text = "Skip",
				style = MaterialTheme.typography.titleMedium,
				color = Color(0xFFF5E1C0),
				fontWeight = FontWeight.Medium
			)
		}
	}
}

@Composable
@Preview
fun SplashScreenPreview() {
	val navController = rememberNavController()
    val viewModel = SplashViewModel()

    SplashScreen(navController, viewModel)
}