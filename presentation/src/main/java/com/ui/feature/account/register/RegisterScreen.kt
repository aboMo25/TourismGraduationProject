package com.ui.feature.account.register

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
	viewModel: RegisterViewModel = koinViewModel(),
) {
	val registerState = viewModel.registerState.collectAsState()
	val launcher = rememberLauncherForActivityResult(
		contract = AuthResultContract(viewModel.getGoogleSignInClient())
	) { task ->
		viewModel.handleGoogleSignInResult(task?.result)
	}
		Box(
		modifier = Modifier.fillMaxSize()
	) {
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
//                    .background(Color.White.copy(alpha = 0.4f))
//                    .background(Color.White.copy(alpha = 0.9f))
//                    .background(Color.Black.copy(alpha = 0.4f))
					.background(Color(0xFFF5E1C0).copy(alpha = 0.8f)) // Sand color background
					.padding(24.dp),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				when (val state = registerState.value) {
					is RegisterState.Success -> {
						LaunchedEffect(registerState.value) {
							navController.navigate(HomeScreen) {
								popUpTo(HomeScreen) { inclusive = true }
							}
						}
					}

					is RegisterState.Error -> {
						Text(
							text = state.message,
							color = Color.Red
						)
					}

					is RegisterState.Loading -> {
						CircularProgressIndicator(color = Color(0xFFE65100))
						Spacer(modifier = Modifier.height(8.dp))
						Text(text = stringResource(id = R.string.loading))
					}

					else -> {
						RegisterContent(
							onRegisterClicked = { email, password, name ->
								viewModel.register(
									email = email,
									password = password,
									name = name
								)
							},
							onSignInClick = {
								navController.popBackStack()
							},
							onGoogleSignInClicked = {
								launcher.launch(0)
							}
						)
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
			text = "Create your account",
			style = MaterialTheme.typography.bodyLarge,
			color = Color(0xFF5D4037),
			textAlign = TextAlign.Center
		)

		Spacer(modifier = Modifier.height(24.dp))

		OutlinedTextField(
			value = name.value,
			onValueChange = { name.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.clip(RoundedCornerShape(12.dp)),
			label = { Text(text = stringResource(id = R.string.name)) },
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color(0xFF8D6E63),
				focusedLabelColor = Color(0xFF8D6E63)
			),
			singleLine = true
		)

		OutlinedTextField(
			value = email.value,
			onValueChange = { email.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.clip(RoundedCornerShape(12.dp)),
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
				.clip(RoundedCornerShape(12.dp)),
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

		OutlinedTextField(
			value = confirmPassword.value,
			onValueChange = { confirmPassword.value = it },
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp)
				.clip(RoundedCornerShape(12.dp)),
			label = { Text(text = "Confirm Password") },
			colors = OutlinedTextFieldDefaults.colors(
				focusedBorderColor = Color(0xFF8D6E63),
				focusedLabelColor = Color(0xFF8D6E63)
			),
			visualTransformation = if (confirmPasswordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
			trailingIcon = {
				IconButton(onClick = {
					confirmPasswordVisibility.value = !confirmPasswordVisibility.value
				}) {
					Icon(
						painter = painterResource(R.drawable.eye),
						contentDescription = "Confirm Password Visibility",
						tint = if (confirmPasswordVisibility.value) Color(0xFFE65100) else Color.Gray
					)
				}
			},
			singleLine = true
		)

		Spacer(modifier = Modifier.height(24.dp))

		Button(
			onClick = {
				if (password.value == confirmPassword.value) {
					onRegisterClicked(email.value, password.value, name.value)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.height(50.dp)
				.clip(RoundedCornerShape(12.dp)),
			enabled = email.value.isNotEmpty() && password.value.isNotEmpty() &&
					name.value.isNotEmpty() && confirmPassword.value.isNotEmpty() &&
					password.value == confirmPassword.value,
			colors = ButtonDefaults.buttonColors(
				containerColor = Color(0xFFE65100)
			)
		) {
			Text(
				color = MaterialTheme.colorScheme.onPrimary,
				text = stringResource(id = R.string.register),
				fontSize = 16.sp
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
				onClick = { /* Handle Facebook registration */ }
			)
			SocialLoginButton(
				icon = R.drawable.ic_twitter,
				onClick = { /* Handle Twitter/X registration */ }
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

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