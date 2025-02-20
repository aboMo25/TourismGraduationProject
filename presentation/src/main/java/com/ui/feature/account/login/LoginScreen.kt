package com.ui.feature.account.login

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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.navigation.HomeScreen
import com.navigation.RegisterScreen
import com.util.AuthResultContract
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin

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
	val launcher = rememberLauncherForActivityResult(
		contract = AuthResultContract(viewModel.getGoogleSignInClient())
	) { task ->
		viewModel.handleGoogleSignInResult(task?.result)
	}

	Box(modifier = Modifier.fillMaxSize()) {
		// Background Image
		Image(
			painter = painterResource(id = R.drawable.login),
			contentDescription = "Egypt Background",
			modifier = Modifier.fillMaxSize(),
			contentScale = ContentScale.FillBounds
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
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth(0.9f)
					.clip(RoundedCornerShape(24.dp))
					.background(Color(0xFFF5E1C0).copy(alpha = 0.8f))
					.padding(24.dp),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				when (val state = loginState.value) {
					is LoginState.Success -> {
						LaunchedEffect(loginState.value) {
							navController.navigate(HomeScreen) {
								popUpTo(HomeScreen) { inclusive = true }
							}
						}
					}
					is LoginState.Error -> {
						Text(
							text = state.message,
							color = Color.Red,
							modifier = Modifier.testTag("errorMsg")
						)
					}
					is LoginState.Loading -> {
						CircularProgressIndicator(color = Color(0xFFE65100))
						Spacer(modifier = Modifier.height(8.dp))
						Text(text = stringResource(id = R.string.loading))
					}
					else -> {
						LoginContent(
							onSignInClicked = { email, password ->
								viewModel.login(email, password)
							},
							onGoogleSignInClicked = {
								launcher.launch(0)
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
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "WELCOME",
			style = MaterialTheme.typography.headlineLarge,
			fontWeight = FontWeight.Bold,
			color = Color(0xFFE65100),
			fontSize = 32.sp
		)

		Spacer(modifier = Modifier.height(8.dp))

		Text(
			text = "Sign in with your email",
			style = MaterialTheme.typography.bodyLarge,
			color = Color.Gray,
			textAlign = TextAlign.Center
		)

		Spacer(modifier = Modifier.height(24.dp))

		OutlinedTextField(
			value = email.value,
			onValueChange = { email.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.clip(RoundedCornerShape(12.dp))
				.testTag("emailField"),
			label = { Text(text = stringResource(id = R.string.email)) },
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color(0xFF8D6E63),
				focusedLabelColor = Color(0xFF8D6E63)
			),
			singleLine = true
		)

		OutlinedTextField(
			value = password.value,
			onValueChange = { password.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.clip(RoundedCornerShape(12.dp))
				.testTag("passwordField"),
			label = { Text(text = stringResource(id = R.string.password)) },
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color(0xFF8D6E63),
				focusedLabelColor = Color(0xFF8D6E63)
			),
			visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
			trailingIcon = {
				IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
					Icon(
						painter = painterResource(R.drawable.eye),
						contentDescription = "Password Visibility",
						tint = if (passwordVisibility.value) Color(0xFFE65100) else Color.Gray
					)
				}
			},
			singleLine = true
		)

		Spacer(modifier = Modifier.height(24.dp))

		Button(
			onClick = { onSignInClicked(email.value, password.value) },
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp)
				.clip(RoundedCornerShape(12.dp))
				.testTag("loginButton"),
			enabled = email.value.isNotEmpty() && password.value.isNotEmpty(),
			colors = ButtonDefaults.buttonColors(
				containerColor = Color(0xFFE65100)
			)
		) {
			Text(
				color = MaterialTheme.colorScheme.onPrimary,
				text = stringResource(id = R.string.login),
				fontSize = 16.sp,
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			SocialLoginButton(
				icon = R.drawable.ic_google,
				onClick = onGoogleSignInClicked
			)
			SocialLoginButton(
				icon = R.drawable.ic_facebook,
				onClick = { /* Handle Facebook login */ }
			)
			SocialLoginButton(
				icon = R.drawable.ic_twitter,
				onClick = { /* Handle Twitter/X login */ }
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		Text(
			text = stringResource(id = R.string.dont_have_account),
			color = Color(0xFFE65100),
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
			.background(Color(0xFFE65100).copy(alpha = 0.8f))
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
