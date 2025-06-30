package com.ui.feature.checkout.passenger_details_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.PaymentMethodScreen
import org.koin.androidx.compose.koinViewModel

// Assuming R.drawable.australia_flag exists

// --- Screen 2: Passenger Details ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerDetailsScreen(
	navController: NavController,
	viewModel: PassengerDetailsScreenViewModel = koinViewModel()

) {
	var fullName by remember { mutableStateOf("Brooklyn Joseph Simmons") }
	var familyName by remember { mutableStateOf("Joseph") }
	var dateOfBirth by remember { mutableStateOf("bsimmons@gmail.com") } // Note: Label says Date Of Birth, but field shows email?
	var nationality by remember { mutableStateOf("Australia") }
	var passportNumber by remember { mutableStateOf("PA0903478") }
	var expiryDate by remember { mutableStateOf("22/09/25") }

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Passenger Details", fontWeight = FontWeight.Bold) },
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
				actions = {
					IconButton(onClick = { /* TODO: Handle more options */ }) {
						Icon(Icons.Filled.MoreVert, contentDescription = "More options")
					}
				},
				colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
					containerColor = Color.White,
					titleContentColor = Color.Black,
					navigationIconContentColor = Color.Black,
					actionIconContentColor = Color.Black
				)
			)
		},
		bottomBar = {
			Surface(color = Color.White, shadowElevation = 8.dp) {
				Button(
					onClick = {
						navController.navigate(PaymentMethodScreen)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					shape = RoundedCornerShape(8.dp),
					colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
				) {
					Text("Save Changes", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
				}
			}
		},
		containerColor = Color(0xFFF5F5F5) // Light gray background
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(horizontal = 16.dp)
				.verticalScroll(rememberScrollState())
		) {
			Spacer(modifier = Modifier.height(16.dp))
			Text("Information Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(16.dp))

			DetailTextField(label = "Your Full Name*", value = fullName, onValueChange = { fullName = it })
			Spacer(modifier = Modifier.height(16.dp))
			DetailTextField(label = "Family Name", value = familyName, onValueChange = { familyName = it })
			Spacer(modifier = Modifier.height(16.dp))
			// Note: The label says "Date Of Birth*" but the placeholder/value looks like an email.
			// Using email keyboard type based on value, but label is kept as is.
			DetailTextField(label = "Date Of Birth*", value = dateOfBirth, onValueChange = { dateOfBirth = it }, keyboardType = KeyboardType.Email)

			Spacer(modifier = Modifier.height(24.dp))
			Text("Identity Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(16.dp))

			NationalityDropdown(selectedNationality = nationality, onValueChange = { nationality = it })
			Spacer(modifier = Modifier.height(16.dp))
			DetailTextField(label = "Passport Number*", value = passportNumber, onValueChange = { passportNumber = it })
			Spacer(modifier = Modifier.height(16.dp))
			DetailTextField(label = "Expiry Date*", value = expiryDate, onValueChange = { expiryDate = it }, keyboardType = KeyboardType.Number)

			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTextField(
	label: String,
	value: String,
	onValueChange: (String) -> Unit,
	keyboardType: KeyboardType = KeyboardType.Text
) {
	Column {
		Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
		Spacer(modifier = Modifier.height(4.dp))
		OutlinedTextField(
			value = value,
			onValueChange = onValueChange,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(8.dp),
			colors = OutlinedTextFieldDefaults.colors(
				focusedContainerColor = Color.White,
				unfocusedContainerColor = Color.White,
				disabledContainerColor = Color.White,
				focusedBorderColor = Color.Gray, // Adjust border color as needed
				unfocusedBorderColor = Color.LightGray,
			),
			keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
			singleLine = true
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NationalityDropdown(
	selectedNationality: String,
	onValueChange: (String) -> Unit
) {
	// This is a simplified representation. A real implementation would use ExposedDropdownMenuBox
	// or a custom dropdown implementation.
	Column {
		Text("Nationality*", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
		Spacer(modifier = Modifier.height(4.dp))
		OutlinedTextField(
			value = selectedNationality,
			onValueChange = { /* Read-only for this simplified example */ },
			modifier = Modifier.fillMaxWidth(),
			readOnly = true,
			shape = RoundedCornerShape(8.dp),
			colors = OutlinedTextFieldDefaults.colors(
			        focusedContainerColor = Color.White,
			        unfocusedContainerColor = Color.White,
			        disabledContainerColor = Color.White,
			        focusedBorderColor = Color.Gray,
			        unfocusedBorderColor = Color.LightGray,
			    ),
			leadingIcon = {
				// Replace with actual flag if available
				Text("") // Placeholder for flag
				// Image(painter = painterResource(id = R.drawable.australia_flag), contentDescription = "Flag", modifier = Modifier.size(24.dp))
			},
			trailingIcon = {
				Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown arrow")
			}
		)
	}
}

@Preview(showBackground = true, device = "spec:width=360dp,height=740dp")
@Composable
fun PassengerDetailsScreenPreview() {
	MaterialTheme {
		PassengerDetailsScreen(rememberNavController())
	}
}

