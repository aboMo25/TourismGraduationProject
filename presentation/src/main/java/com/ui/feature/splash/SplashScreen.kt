package com.ui.feature.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.domain.model.SplashScreenData
import com.navigation.LoginScreen
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
	navController: NavController,
	viewModel: SplashViewModel = koinViewModel()
) {
	val uiState by viewModel.uiState.collectAsState()

	LaunchedEffect(uiState) {
		if (uiState is SplashUiState.Success && (uiState as SplashUiState.Success).shouldNavigate) {
			navController.navigate(LoginScreen) {
				popUpTo(LoginScreen) { inclusive = true }
			}
			viewModel.resetNavigation()
		}
	}

	when (val state = uiState) {
		is SplashUiState.Loading -> LoadingView()
		is SplashUiState.Error -> ErrorView(message = state.message)
		is SplashUiState.Success -> {
			SplashContentWithNavigation(
				currentScreen = state.currentScreen,
				currentIndex = state.currentIndex,
				totalScreens = state.totalScreens,
				onNextClick = { viewModel.onNextClicked() },
				onSkipClick = {
					navController.navigate(LoginScreen) {
						popUpTo(LoginScreen) { inclusive = true }
					}
				},
				navController = navController
			)
		}
	}
}

@Composable
private fun SplashContentWithNavigation(
	currentScreen: SplashScreenData,
	currentIndex: Int,
	totalScreens: Int,
	onNextClick: () -> Unit,
	onSkipClick: () -> Unit,
	navController: NavController
) {
	Box(modifier = Modifier.fillMaxSize()) {

		// Background Image
		AsyncImage(
			model = ImageRequest.Builder(LocalContext.current)
				.data(currentScreen.image)
				.crossfade(true)
				.memoryCachePolicy(CachePolicy.ENABLED)
				.diskCachePolicy(CachePolicy.ENABLED)
				.build(),
			contentDescription = null,
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.Crop
		)

		// Gradient Overlay
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(
					Brush.verticalGradient(
						listOf(
							Color.Black.copy(alpha = 0.3f),
							Color.Black.copy(alpha = 0.7f)
						)
					)
				)
		)

		// Skip Button
		if (currentIndex < totalScreens - 1) {
			SkipButton(
				onSkipClick = onSkipClick,
				modifier = Modifier
					.align(Alignment.TopEnd)
					.padding(16.dp)
			)
		}

		// Main Content
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 24.dp),
			verticalArrangement = Arrangement.SpaceBetween,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Spacer(modifier = Modifier.height(48.dp)) // Optional Top Spacer

			SplashContent(splashScreen = currentScreen)

			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				// Indicators
				Row(
					modifier = Modifier.padding(bottom = 16.dp),
					horizontalArrangement = Arrangement.Center
				) {
					repeat(totalScreens) { index ->
						Box(
							modifier = Modifier
								.padding(horizontal = 4.dp)
								.size(12.dp)
								.clip(RoundedCornerShape(32.dp))
								.background(
									if (index == currentIndex)
										Color(0xFFE65100)
									else
										Color.White.copy(alpha = 0.5f)
								)
						)
					}
				}

				NextButton(
					onNext = onNextClick,
					isLastScreen = currentIndex == totalScreens - 1,
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
}


@Composable
private fun SplashContent(splashScreen: SplashScreenData) {
	var isVisible by remember { mutableStateOf(false) }

	LaunchedEffect(Unit) {
		delay(500)
		isVisible = true
	}

	AnimatedVisibility(
		visible = isVisible,
		enter = fadeIn() + slideInVertically()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(12.dp),
			modifier = Modifier
				.clip(RoundedCornerShape(16.dp))
				.background(Color(0xFFF5E1C0).copy(alpha = 0.85f))
				.padding(24.dp)
				.fillMaxWidth()
		) {
			Text(
				text = "Automated Tourism",
				style = MaterialTheme.typography.headlineMedium,
				color = Color(0xFFE65100),
				textAlign = TextAlign.Center
			)
			Text(
				text = splashScreen.title,
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.Bold,
				color = Color(0xFF6D4C41),
				textAlign = TextAlign.Center
			)
			Text(
				text = splashScreen.description,
				style = MaterialTheme.typography.bodyLarge,
				color = Color(0xFF6D4C41),
				textAlign = TextAlign.Center
			)
		}
	}
}


@Composable
private fun LoadingView() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator(color = Color(0xFFE65100))
	}
}

@Composable
private fun ErrorView(message: String) {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.padding(24.dp)
		) {
			Text(
				text = "Error",
				style = MaterialTheme.typography.headlineMedium,
				color = Color(0xFFE65100)
			)
			Spacer(modifier = Modifier.height(16.dp))
			Text(
				text = message,
				style = MaterialTheme.typography.bodyLarge,
				color = Color.White,
				textAlign = TextAlign.Center
			)
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
