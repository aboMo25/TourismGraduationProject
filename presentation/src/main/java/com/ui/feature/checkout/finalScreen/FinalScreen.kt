package com.ui.feature.checkout.finalScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourismgraduationproject.R
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.navigation.HomeScreen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun FinalScreen(
	navController: NavController,
	viewModel: FinalScreenViewModel = koinViewModel()
) {
	val uiState by viewModel.uiState.collectAsState()

	// Load Lottie composition
	val composition by rememberLottieComposition(
		LottieCompositionSpec.RawRes(R.raw.raw)
	)

	// Control animation progress
	val progress by animateLottieCompositionAsState(
		composition = composition,
		iterations = 1, // Play only once
		isPlaying = true
	)

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.White)
			.padding(24.dp),
		verticalArrangement = Arrangement.SpaceBetween,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.weight(1f)
		) {
			Spacer(modifier = Modifier.height(40.dp))

			// Lottie animation
			LottieAnimation(
				composition = composition,
				progress = { progress },
				modifier = Modifier.size(200.dp)
			)

			Spacer(modifier = Modifier.height(24.dp))

			// Success text
			Text(
				text = "✓ Done",
				fontSize = 28.sp,
				fontWeight = FontWeight.Bold,
				color = Color(0xFF4CAF50)
			)

			Spacer(modifier = Modifier.height(8.dp))

			Text(
				text = "Your trip has been successfully booked!",
				fontSize = 18.sp,
				color = Color.Gray
			)
		}

		Button(
			onClick = { navController.navigate(HomeScreen) },
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			shape = RoundedCornerShape(12.dp)
		) {
			Text(text = "Go To Home Screen", fontSize = 16.sp)
		}
	}
}