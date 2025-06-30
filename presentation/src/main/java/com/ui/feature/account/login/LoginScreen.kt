package com.ui.feature.account.login

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourismgraduationproject.R
import com.navigation.HomeScreen
import com.navigation.RegisterScreen
import com.util.AuthResultContract
import org.koin.androidx.compose.koinViewModel // Ensure this import is correct
import com.google.android.gms.common.api.ApiException // Import ApiException for error handling

@Composable
fun LoginScreen(
	navController: NavController,
	viewModel: LoginViewModel = koinViewModel(),
) {
	val isLoggedIn by viewModel.isLoggedIn.collectAsState()
	LaunchedEffect(isLoggedIn) {
		if (isLoggedIn) {
			navController.navigate(HomeScreen) {
				popUpTo(HomeScreen) { inclusive = true }
			}
		}
	}

	if (!isLoggedIn) {
		LoginScreenContent(
			navController = navController,
			viewModel = viewModel
		)
	}
}

@Composable
private fun LoginScreenContent(
	navController: NavController,
	viewModel: LoginViewModel,
) {
	val loginState = viewModel.loginState.collectAsState()
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
			// For example, if the user cancels or an internal Google error occurs
			viewModel.loginState.value = LoginState.Error("Google Sign-In failed: ${e.statusCode}")
		} catch (e: Exception) {
			// Catch any other unexpected exceptions
			viewModel.loginState.value = LoginState.Error("An unexpected error occurred during Google Sign-In.")
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		// Background
		Image(
			painter = painterResource(id = R.drawable.login),
			contentDescription = null,
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.Crop
		)

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

		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			contentAlignment = Alignment.Center
		) {
			Surface(
				shape = RoundedCornerShape(24.dp),
				color = Color.White.copy(alpha = 0.85f),
				tonalElevation = 4.dp,
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
					when (val state = loginState.value) {
						is LoginState.Loading -> {
							CircularProgressIndicator()
							Text(text = stringResource(id = R.string.loading))
						}
						is LoginState.Error -> {
							Text(
								text = state.message,
								color = Color.Red,
								textAlign = TextAlign.Center,
								modifier = Modifier.testTag("errorMsg")
							)
							// --- ADDED: Reset state after showing error ---
							LaunchedEffect(state.message) {
								Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
								viewModel.resetLoginState()
							}
							// ---------------------------------------------
						}
						is LoginState.Success -> {
							LaunchedEffect(state) {
								navController.navigate(HomeScreen) {
									popUpTo(HomeScreen) { inclusive = true }
								}
							}
						}
						else -> { // LoginState.Idle
							LoginContent(
								onSignInClicked = { email, password ->
									viewModel.login(email, password)
								},
								onGoogleSignInClicked = {
									launcher.launch(0) // Trigger the Google Sign-In flow
								},
								onRegisterClick = {
									navController.navigate(RegisterScreen)
								}
							)
						}
					}
				}
			}
		}
	}
}


@Composable
fun LoginContent(
	onSignInClicked: (String, String) -> Unit,
	onGoogleSignInClicked: () -> Unit,
	onRegisterClick: () -> Unit,
) {
	val email = remember { mutableStateOf("") }
	val password = remember { mutableStateOf("") }
	val passwordVisibility = remember { mutableStateOf(false) }

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
			text = "Sign in with your email",
			style = MaterialTheme.typography.bodyLarge,
			color = Color.Gray,
			textAlign = TextAlign.Center
		)

		OutlinedTextField(
			value = email.value,
			onValueChange = { email.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.testTag("emailField"),
			label = { Text(text = stringResource(id = R.string.email)) },
			singleLine = true,
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color(0xFF8D6E63),
				focusedLabelColor = Color(0xFF8D6E63)
			)
		)

		OutlinedTextField(
			value = password.value,
			onValueChange = { password.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.testTag("passwordField"),
			label = { Text(text = stringResource(id = R.string.password)) },
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
			},
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color(0xFF8D6E63),
				focusedLabelColor = Color(0xFF8D6E63)
			)
		)

		Button(
			onClick = { onSignInClicked(email.value, password.value) },
			enabled = email.value.isNotEmpty() && password.value.isNotEmpty(),
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp)
				.testTag("loginButton"),
			colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
		) {
			Text(
				text = stringResource(id = R.string.login),
				color = Color.White,
				fontSize = 16.sp
			)
		}

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			SocialLoginButton(R.drawable.ic_google, onGoogleSignInClicked) // Google button
			SocialLoginButton(R.drawable.ic_facebook) { /*TODO*/ }
			SocialLoginButton(R.drawable.ic_twitter) { /*TODO*/ }
		}

		Text(
			text = stringResource(id = R.string.dont_have_account),
			color = Color.Black,
			modifier = Modifier
				.clickable { onRegisterClick() }
				.padding(top = 8.dp)
		)
	}
}


@Composable
fun SocialLoginButton(
	icon: Int,
	onClick: () -> Unit
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

@Composable
@Preview(showBackground = true)
fun PreviewLoginScreen() {
	LoginContent(
		onSignInClicked = { _, _ -> },
		onGoogleSignInClicked = {},
		onRegisterClick = {}
	)
}