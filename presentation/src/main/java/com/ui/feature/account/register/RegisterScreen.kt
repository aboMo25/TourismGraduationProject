package com.ui.feature.account.register

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourismgraduationproject.R
import com.navigation.HomeScreen
import com.util.AuthResultContract
import org.koin.androidx.compose.koinViewModel
import com.google.android.gms.common.api.ApiException // Import ApiException for error handling

@Composable
fun RegisterScreen(
	navController: NavController,
	viewModel: RegisterViewModel = koinViewModel(),
) {
	RegisterScreenContent(
		navController = navController,
		viewModel = viewModel
	)
}

@Composable
fun RegisterScreenContent(
	navController: NavController,
	viewModel: RegisterViewModel, // ViewModel is already koinViewModel() in RegisterScreen
) {
	val registerState = viewModel.registerState.collectAsState()
	val context = LocalContext.current // Get context for Toast

	val launcher = rememberLauncherForActivityResult(
		contract = AuthResultContract(viewModel.getGoogleSignInClient())
	) { task ->
		// Handle the result from the AuthResultContract
		try {
			val account = task?.getResult(ApiException::class.java)
//			viewModel.handleGoogleSignInResult(account)
		} catch (e: ApiException) {
			// This catches errors specific to Google Sign-In before backend authentication
			viewModel.registerState.value = RegisterState.Error("Google Sign-In failed: ${e.statusCode}")
		} catch (e: Exception) {
			// Catch any other unexpected exceptions
			viewModel.registerState.value = RegisterState.Error("An unexpected error occurred during Google Sign-In.")
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		// Background Image
		Image(
			painter = painterResource(id = R.drawable.login),
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
						colors = listOf(
							Color.Black.copy(alpha = 0.3f),
							Color.Black.copy(alpha = 0.7f)
						)
					)
				)
		)

		// Main Content
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center
		) {
			Surface(
				shape = RoundedCornerShape(24.dp),
				color = Color.White.copy(alpha = 0.85f),
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight()
			) {
				Column(
					modifier = Modifier
						.padding(24.dp)
						.fillMaxWidth(),
					verticalArrangement = Arrangement.spacedBy(16.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					when (val state = registerState.value) {
						is RegisterState.Loading -> {
							CircularProgressIndicator(color = Color(0xFFE65100))
							Text(text = stringResource(id = R.string.loading))
						}
						is RegisterState.Error -> {
							Text(
								text = state.message,
								color = Color.Red,
								textAlign = TextAlign.Center
							)
							// --- ADDED: Reset state after showing error ---
							LaunchedEffect(state.message) {
								Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
								viewModel.resetRegisterState()
							}
							// ---------------------------------------------
						}
						is RegisterState.Success -> {
							LaunchedEffect(state) {
								navController.navigate(HomeScreen) {
									popUpTo(HomeScreen) { inclusive = true }
								}
							}
						}
						else -> { // RegisterState.Idle
							RegisterContent(
								onRegisterClicked = { email, password, name ->
									viewModel.register(email, password, name)
								},
								onSignInClick = { navController.popBackStack() },
								onGoogleSignInClicked = { launcher.launch(0) } // Trigger the Google Sign-In flow
							)
						}
					}
				}
			}
		}
	}
}


@Composable
fun RegisterContent(
	onRegisterClicked: (String, String, String) -> Unit,
	onSignInClick: () -> Unit,
	onGoogleSignInClicked: () -> Unit,
) {
	val name = remember { mutableStateOf("") }
	val email = remember { mutableStateOf("") }
	val password = remember { mutableStateOf("") }
	val confirmPassword = remember { mutableStateOf("") }
	val passwordVisibility = remember { mutableStateOf(false) }
	val confirmPasswordVisibility = remember { mutableStateOf(false) }

	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "WELCOME",
			style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
			fontWeight = FontWeight.Bold,
			color = Color.Black
		)

		Text(
			text = "Create your account",
			style = MaterialTheme.typography.bodyLarge,
			color = Color.Gray,
			textAlign = TextAlign.Center
		)

		OutlinedTextField(
			value = name.value,
			onValueChange = { name.value = it },
			modifier = Modifier.fillMaxWidth(),
			label = { Text(stringResource(id = R.string.name)) },
			singleLine = true
		)

		OutlinedTextField(
			value = email.value,
			onValueChange = { email.value = it },
			modifier = Modifier.fillMaxWidth(),
			label = { Text(stringResource(id = R.string.email)) },
			singleLine = true
		)

		OutlinedTextField(
			value = password.value,
			onValueChange = { password.value = it },
			modifier = Modifier.fillMaxWidth(),
			label = { Text(stringResource(id = R.string.password)) },
			singleLine = true,
			visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
			trailingIcon = {
				IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
					Icon(
						painter = painterResource(R.drawable.eye),
						contentDescription = null,
						tint = if (passwordVisibility.value) Color.Black else Color.Gray
					)
				}
			}
		)

		OutlinedTextField(
			value = confirmPassword.value,
			onValueChange = { confirmPassword.value = it },
			modifier = Modifier.fillMaxWidth(),
			label = { Text("Confirm Password") },
			singleLine = true,
			visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
			trailingIcon = {
				IconButton(onClick = { confirmPasswordVisibility.value = !confirmPasswordVisibility.value }) {
					Icon(
						painter = painterResource(R.drawable.eye),
						contentDescription = null,
						tint = if (confirmPasswordVisibility.value) Color.Black else Color.Gray
					)
				}
			}
		)

		Button(
			onClick = {
				if (password.value == confirmPassword.value) {
					onRegisterClicked(email.value, password.value, name.value)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp),
			enabled = name.value.isNotEmpty() &&
					email.value.isNotEmpty() &&
					password.value.isNotEmpty() &&
					confirmPassword.value == password.value,
			colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
		) {
			Text(
				text = stringResource(id = R.string.register),
				color = Color.White,
				fontSize = 16.sp
			)
		}

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			SocialLoginButton(R.drawable.ic_google, onGoogleSignInClicked) // Google button
			SocialLoginButton(R.drawable.ic_facebook) { /* TODO */ }
			SocialLoginButton(R.drawable.ic_twitter) { /* TODO */ }
		}

		Text(
			text = stringResource(id = R.string.alread_have_an_account),
			color = Color(0xFFE65100),
			modifier = Modifier
				.clickable { onSignInClick() }
				.padding(top = 8.dp)
		)
	}
}


@Composable
fun SocialLoginButton(
	icon: Int,
	onClick: () -> Unit,
) {
	IconButton(
		onClick = onClick,
		modifier = Modifier
			.size(48.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(Color(0xFFFFFFFF).copy(alpha = 0.8f))
	) {
		Icon(
			painter = painterResource(id = icon),
			contentDescription = "Social Login",
			tint = Color.Unspecified,
			modifier = Modifier.size(24.dp)
		)
	}
}