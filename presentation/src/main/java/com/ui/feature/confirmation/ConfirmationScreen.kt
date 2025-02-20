package com.ui.feature.confirmation

import com.ui.feature.splash.TourismData.paymentMethods
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.navigation.HomeScreen
import com.ui.feature.splash.TourismData
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
	navController: NavController,
	confirmationViewModel: ConfirmationViewModel = koinViewModel()

//	onClassSelected: (TripClass) -> Unit,
) {
	var firstName by remember { mutableStateOf("") }
	var lastName by remember { mutableStateOf("") }
	var phoneNumber by remember { mutableStateOf("") }
	var idPassport by remember { mutableStateOf("") }
	var selectedPaymentMethod by remember { mutableStateOf("") }
	var selectedTripClass by remember { mutableStateOf<TripClass?>(null) }
	var selectedPeopleCount by remember { mutableStateOf<Int?>(null) }

	val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
			phoneNumber.isNotBlank() && idPassport.isNotBlank() &&
			selectedPaymentMethod.isNotBlank() && selectedTripClass != null &&
			selectedPeopleCount != null

	val scrollState = rememberScrollState()
	var showConfirmation by remember { mutableStateOf(false) }

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						"Confirmation",
						style = MaterialTheme.typography.headlineMedium.copy(
							fontWeight = FontWeight.Bold
						)
					)
				},
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(
							Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Back",
							tint = MaterialTheme.colorScheme.primary
						)
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xFFE65100),
					titleContentColor = Color(0xFFE65100)
				),
				modifier = Modifier.shadow(8.dp)
			)
		}
	) { padding ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(color = Color(0xFF867062))
		) {
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(padding)
					.verticalScroll(scrollState)
					.animateContentSize()
			) {
				Card(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
					colors = CardDefaults.cardColors(
						containerColor = Color(0xFFE78B5A)
					)
				) {
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(24.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						Text(
							"Finalize your trip",
							style = MaterialTheme.typography.headlineMedium.copy(
								fontWeight = FontWeight.Bold,
								color = Color(0xFFE65100)
							)
						)

						// Personal Information Section
						PersonalInfoSection(
							firstName = firstName,
							lastName = lastName,
							phoneNumber = phoneNumber,
							idPassport = idPassport,
							onFirstNameChange = { firstName = it },
							onLastNameChange = { lastName = it },
							onPhoneNumberChange = { phoneNumber = it },
							onIdPassportChange = { idPassport = it }
						)

						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))

						// Trip Details Section
						TripDetailsSection(
							selectedTripClass = selectedTripClass,
							selectedPeopleCount = selectedPeopleCount,
							onClassSelected = {
								selectedTripClass = it
//								onClassSelected(it)
							},
							onPeopleSelected = { selectedPeopleCount = it }
						)

						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))

						// Payment Section
						PaymentMethodSelector(
							onPaymentMethodSelected = { selectedPaymentMethod = it },
							modifier = Modifier.fillMaxWidth()
						)
					}
				}

				// Confirm Button
				AnimatedConfirmButton(
					isEnabled = isFormValid,
					onClick = {
						showConfirmation = true
						navController.navigate(HomeScreen)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp, vertical = 24.dp)
				)
			}

			// Confirmation Dialog
			if (showConfirmation) {
				ConfirmationDialog(
					onDismiss = { showConfirmation = false }
				)
			}
		}
	}
}

@Composable
fun ConfirmationDialog(
	onDismiss: () -> Unit,
) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(
			dismissOnBackPress = true,
			dismissOnClickOutside = true
		)
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(
				containerColor = Color(0xFFE65100)
			)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				Icon(
					imageVector = Icons.Default.CheckCircle,
					contentDescription = null,
					modifier = Modifier.size(64.dp),
					tint = Color(0xFFE65100)
				)

				Text(
					"Booking Confirmed!",
					style = MaterialTheme.typography.headlineSmall.copy(
						fontWeight = FontWeight.Bold
					),
					color = Color(0xFFE65100)
				)

				Text(
					"Your trip has been successfully booked. You will receive a confirmation email shortly.",
					style = MaterialTheme.typography.bodyMedium,
					textAlign = TextAlign.Center,
					color = Color(0xFFE65100)
				)

				Button(
					onClick = onDismiss,
					modifier = Modifier.fillMaxWidth(),
					colors = ButtonDefaults.buttonColors(
						containerColor = Color(0xFFE65100)
					)
				) {
					Text("Done")
				}
			}
		}
	}
}

@Composable
fun TripDetailsSection(
	selectedTripClass: TripClass?,
	selectedPeopleCount: Int?,
	onClassSelected: (TripClass) -> Unit,
	onPeopleSelected: (Int) -> Unit,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		Text(
			"Trip Details",
			style = MaterialTheme.typography.titleMedium,
			color = Color(0xFFE65100)
		)

		// Trip Class Selection
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.surfaceVariant
			)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Text(
					"Select Class",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				LazyRow(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					contentPadding = PaddingValues(4.dp)
				) {
					items(TourismData.tripClasses) { tripClass ->
						ClassCard(
							tripClass = tripClass,
							isSelected = selectedTripClass == tripClass,
							onClick = { onClassSelected(tripClass) }
						)
					}
				}
			}
		}

		// Number of People Selection
		Card(
			modifier = Modifier.fillMaxWidth(),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.surfaceVariant
			)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Text(
					"Number of People",
					style = MaterialTheme.typography.labelLarge,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)

				LazyRow(
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					contentPadding = PaddingValues(4.dp)
				) {
					items(6) { number ->
						val count = number + 1
						PeopleCountButton(
							count = count,
							isSelected = selectedPeopleCount == count,
							onClick = { onPeopleSelected(count) }
						)
					}
					item {
						CustomPeopleCountButton(
							onPeopleSelected = onPeopleSelected
						)
					}
				}
			}
		}
	}
}

@Composable
private fun ClassCard(
	tripClass: TripClass,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier
			.width(160.dp)
			.clickable(onClick = onClick),
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected)
				MaterialTheme.colorScheme.primaryContainer
			else
				MaterialTheme.colorScheme.surface
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = if (isSelected) 8.dp else 2.dp
		)
	) {
		Column(
			modifier = Modifier
				.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Text(
				tripClass.name,
				style = MaterialTheme.typography.titleMedium,
				color = if (isSelected)
					MaterialTheme.colorScheme.onPrimaryContainer
				else
					MaterialTheme.colorScheme.onSurface
			)
			Text(
				tripClass.description,
				style = MaterialTheme.typography.bodySmall,
				color = if (isSelected)
					MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
				else
					MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun PeopleCountButton(
	count: Int,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	OutlinedButton(
		onClick = onClick,
		modifier = Modifier.size(56.dp),
		colors = ButtonDefaults.outlinedButtonColors(
			containerColor = if (isSelected)
				MaterialTheme.colorScheme.primary
			else
				MaterialTheme.colorScheme.surface
		),
		border = BorderStroke(
			1.dp,
			if (isSelected)
				MaterialTheme.colorScheme.primary
			else
				MaterialTheme.colorScheme.outline
		)
	) {
		Text(
			count.toString(),
			style = MaterialTheme.typography.titleMedium,
			color = if (isSelected)
				MaterialTheme.colorScheme.onPrimary
			else
				MaterialTheme.colorScheme.onSurface
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomPeopleCountButton(
	onPeopleSelected: (Int) -> Unit,
) {
	var showDialog by remember { mutableStateOf(false) }
	var customCount by remember { mutableStateOf("") }

	OutlinedButton(
		onClick = { showDialog = true },
		colors = ButtonDefaults.outlinedButtonColors(
			containerColor = MaterialTheme.colorScheme.secondaryContainer
		)
	) {
		Text(
			"More",
			color = MaterialTheme.colorScheme.onSecondaryContainer
		)
	}

	if (showDialog) {
		AlertDialog(
			onDismissRequest = { showDialog = false },
			title = { Text("Enter number of people") },
			text = {
				OutlinedTextField(
					value = customCount,
					onValueChange = {
						if (it.isEmpty() || it.matches(Regex("^\\d*\$"))) {
							customCount = it
						}
					},
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					singleLine = true
				)
			},
			confirmButton = {
				TextButton(
					onClick = {
						customCount.toIntOrNull()?.let { count ->
							if (count > 0) {
								onPeopleSelected(count)
							}
						}
						showDialog = false
					}
				) {
					Text("Confirm")
				}
			},
			dismissButton = {
				TextButton(onClick = { showDialog = false }) {
					Text("Cancel")
				}
			}
		)
	}
}

@Composable
private fun PersonalInfoSection(
	firstName: String,
	lastName: String,
	phoneNumber: String,
	idPassport: String,
	onFirstNameChange: (String) -> Unit,
	onLastNameChange: (String) -> Unit,
	onPhoneNumberChange: (String) -> Unit,
	onIdPassportChange: (String) -> Unit,
) {
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp)
	) {
		Text(
			"Personal Information",
			style = MaterialTheme.typography.titleMedium,
			color =Color(0xFFE65100)
		)

		CustomTextField(
			value = firstName,
			onValueChange = onFirstNameChange,
			label = "First name",
		)
		CustomTextField(
			value = lastName,
			onValueChange = onLastNameChange,
			label = "Last name",
		)
		PhoneNumberInput(
			value = phoneNumber,
			onValueChange = onPhoneNumberChange,
			label = "Phone number"
		)
		CustomTextField(
			value = idPassport,
			onValueChange = onIdPassportChange,
			label = "ID/Passport number"
		)
	}
}

@Composable
private fun AnimatedConfirmButton(
	isEnabled: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Button(
		onClick = onClick,
		enabled = isEnabled,
		modifier = modifier
			.height(56.dp)
			.scale(animateFloatAsState(if (isEnabled) 1f else 0.95f).value),
		colors = ButtonDefaults.buttonColors(
			containerColor = Color(0xFFE65100),
			contentColor = Color(0xFFE65100)
		)
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				"Confirm Booking",
				style = MaterialTheme.typography.titleMedium
			)
			Icon(
				Icons.Default.ArrowForward,
				contentDescription = null
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodSelector(
	onPaymentMethodSelected: (String) -> Unit,
	modifier: Modifier = Modifier,
) {
	var expanded by remember { mutableStateOf(false) }
	var savedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }

	Column(modifier = modifier) {
		Text(
			text = "Select Payment Method",
			style = MaterialTheme.typography.bodyMedium,
			modifier = Modifier.padding(bottom = 8.dp)
		)
		ExposedDropdownMenuBox(
			expanded = expanded,
			onExpandedChange = { expanded = it }
		) {
			OutlinedTextField(
				value = savedPaymentMethod?.name ?: "Select payment method",
				onValueChange = {},
				readOnly = true,
				trailingIcon = {
					Icon(
						imageVector = Icons.Filled.ArrowDropDown,
						contentDescription = "Select payment method"
					)
				},
				modifier = Modifier
					.fillMaxWidth()
					.menuAnchor()
			)
			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {

				paymentMethods.forEach { paymentMethod ->
					DropdownMenuItem(
						text = {
							Column {
								Text(paymentMethod.name)
								Text(
									text = paymentMethod.description,
									style = MaterialTheme.typography.bodySmall,
									color = MaterialTheme.colorScheme.onSurfaceVariant
								)
							}
						},
						onClick = {
							savedPaymentMethod = paymentMethod
							onPaymentMethodSelected(paymentMethod.name)
							expanded = false
						}
					)
					if (paymentMethod != paymentMethods.last()) {
						HorizontalDivider()
					}
				}
			}
		}
	}
}

data class TripClass(
	val id: String,
	val name: String,
	val description: String,
	val priceMultiplier: Double,
)

data class PaymentMethod(
	val name: String,
	val description: String,
	val id: String,
)
