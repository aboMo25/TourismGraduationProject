package com.ui.feature.checkout.payment_method_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ui.feature.checkout.checkout_ticket_screen.CheckoutTicketScreenViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

// Assuming R.drawable.ic_bca, R.drawable.ic_dana, R.drawable.ic_visa exist
// Placeholder for image resources if not available:
// import androidx.compose.ui.res.painterResource(id = R.drawable.ic_bca)
// import androidx.compose.ui.res.painterResource(id = R.drawable.ic_dana)
// import androidx.compose.ui.res.painterResource(id = R.drawable.ic_visa)

@RequiresApi(Build.VERSION_CODES.O) // For LocalDate in Ticket data model if used directly
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
	navController: NavController,
	paymentViewModel: PaymentMethodScreenViewModel = koinViewModel(),
	checkoutViewModel: CheckoutTicketScreenViewModel = koinViewModel() // Access checkout VM
) {
	var selectedPaymentMethodId by remember { mutableStateOf("BCA") } // Using "BCA" as default ID
	val paymentOptions = listOf(
		PaymentOptionData("BCA Virtual Account", "BCA", /* painterResource(id = R.drawable.ic_bca) */),
		PaymentOptionData("Dana - 08835745XXX", "Dana", /* painterResource(id = R.drawable.ic_dana) */),
		PaymentOptionData("Visa - 508 772 8XXX", "Visa", /* painterResource(id = R.drawable.ic_visa) */)
	)
	var voucherCode by remember { mutableStateOf("") }

	val finalPrice by checkoutViewModel.finalPrice.collectAsState()
	val cartItems by checkoutViewModel.cartItems.collectAsState()
	val fullName by checkoutViewModel.fullName.collectAsState()
	val email by checkoutViewModel.email.collectAsState()
	val adultPassengers by checkoutViewModel.adultPassengers.collectAsState()
	val youngPassengers by checkoutViewModel.youngPassengers.collectAsState()
	val nationality by checkoutViewModel.nationality.collectAsState()
	val phoneNumber by checkoutViewModel.phoneNumber.collectAsState()

	// Pass data to PaymentMethodScreenViewModel
	LaunchedEffect(cartItems, finalPrice, fullName, email, adultPassengers, youngPassengers, nationality, phoneNumber) {
		paymentViewModel.cartItems = cartItems
		paymentViewModel.finalAmount = finalPrice
		paymentViewModel.passengerDetails = com.domain.model.PassengerDetails(
			fullName = fullName,
			email = email,
			totalAdultPassengers = adultPassengers,
			totalYoungPassengers = youngPassengers,
			nationality = nationality,
			phoneNumber = phoneNumber,

		)
	}

	val paymentState by paymentViewModel.paymentState.collectAsState()
	val snackbarHostState = remember { SnackbarHostState() }
	val scope = rememberCoroutineScope()

	// Observe payment state for feedback and navigation
	LaunchedEffect(paymentState) {
		when (paymentState) {
			is PaymentState.Success -> {
				scope.launch {
					val successMessage = (paymentState as PaymentState.Success).message
					snackbarHostState.showSnackbar(successMessage)
					val ticket = (paymentState as PaymentState.Success).ticket
					// Navigate to TicketDetailsScreen, passing the ticket ID or object
					navController.navigate("ticketDetails/${ticket.bookingId}") { // Pass bookingId
						// Clear back stack to prevent going back to checkout screens
						popUpTo(navController.graph.startDestinationId) {
							inclusive = false
						}
						launchSingleTop = true
					}
					paymentViewModel.resetPaymentState()
				}
			}
			is PaymentState.Error -> {
				scope.launch {
					snackbarHostState.showSnackbar((paymentState as PaymentState.Error).message)
					paymentViewModel.resetPaymentState()
				}
			}
			else -> {}
		}
	}

	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Checkout Ticket", fontWeight = FontWeight.Bold) },
				navigationIcon = {
					IconButton(onClick = { navController.popBackStack() }) {
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
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp, vertical = 12.dp)
				) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text("Total Payable:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
						Text(
							"$${String.format("%.2f", finalPrice)}",
							style = MaterialTheme.typography.titleLarge,
							fontWeight = FontWeight.Bold,
							color = MaterialTheme.colorScheme.primary
						)
					}
					Spacer(modifier = Modifier.height(12.dp))
					Button(
						onClick = {
							if (paymentState !is PaymentState.Loading) {
								paymentViewModel.proceedToCheckout(selectedPaymentMethodId, voucherCode.ifEmpty { null })
							}
						},
						modifier = Modifier
							.fillMaxWidth()
							.height(50.dp),
						shape = RoundedCornerShape(8.dp),
						colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
						enabled = paymentState !is PaymentState.Loading // Disable while loading
					) {
						if (paymentState is PaymentState.Loading) {
							CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
						} else {
							Text("Proceed Checkout", color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
						}
					}
				}
			}
		},
		containerColor = Color(0xFFF5F5F5), // Light gray background
		snackbarHost = { SnackbarHost(snackbarHostState) }
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.padding(horizontal = 16.dp)
				.verticalScroll(rememberScrollState())
		) {
			Spacer(modifier = Modifier.height(16.dp))

			// Display loading/error for payment processing
			if (paymentState is PaymentState.Loading) {
				LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
				Spacer(modifier = Modifier.height(8.dp))
			}

			Text("Payment Method", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			PaymentMethodSelection(paymentOptions, selectedPaymentMethodId) { selectedPaymentMethodId = it }
			Spacer(modifier = Modifier.height(24.dp))

			Text("Discount Voucher", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(8.dp))
			DiscountVoucherInput(voucherCode = voucherCode, onValueChange = { voucherCode = it })
			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}
data class PaymentOptionData(val text: String, val id: String, val icon: Painter? = null)

@Composable
fun PaymentMethodSelection(
	options: List<PaymentOptionData>,
	selectedOptionId: String,
	onOptionSelected: (String) -> Unit
) {
	Card(
		shape = RoundedCornerShape(12.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		modifier = Modifier.fillMaxWidth()
	) {
		Column(modifier = Modifier.padding(vertical = 8.dp)) {
			options.forEachIndexed { index, option ->
				PaymentOptionRow(
					text = option.text,
					icon = option.icon,
					selected = selectedOptionId == option.id,
					onClick = { onOptionSelected(option.id) }
				)
				if (index < options.size - 1) {
					HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(horizontal = 16.dp))
				}
			}
		}
	}
}

@Composable
fun PaymentOptionRow(text: String, icon: Painter?, selected: Boolean, onClick: () -> Unit) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.selectable(
				selected = selected,
				onClick = onClick,
				role = Role.RadioButton
			)
			.padding(horizontal = 16.dp, vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		if (icon != null) {
			Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Unspecified) // Use actual icon color
			Spacer(modifier = Modifier.width(12.dp))
		} else {
			// Placeholder for icon if needed
			Spacer(modifier = Modifier.width(36.dp))
		}
		Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
		RadioButton(
			selected = selected,
			onClick = null, // onClick handled by Row's selectable
			colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountVoucherInput(voucherCode: String, onValueChange: (String) -> Unit) {
	Card(
		shape = RoundedCornerShape(12.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		modifier = Modifier.fillMaxWidth()
	) {
		OutlinedTextField(
			value = voucherCode,
			onValueChange = onValueChange,
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			placeholder = { Text("Voucher Code") },
			shape = RoundedCornerShape(8.dp),
			colors = TextFieldDefaults.outlinedTextFieldColors(
				containerColor = Color.Transparent, // Make background transparent inside the card
				focusedBorderColor = Color.Gray,
				unfocusedBorderColor = Color.LightGray
			),
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
			singleLine = true
		)
	}
}

//@Preview(showBackground = true, device = "spec:width=360dp,height=740dp")
//@Composable
//fun PaymentMethodScreenPreview() {
//	MaterialTheme {
//		PaymentMethodScreen(rememberNavController())
//	}
//}

// --- Reusable Components (Ensure these are accessible or defined) ---
// If TicketInfoCard is not in the same file/package, it needs to be defined here or imported.
// Example placeholder if not accessible:
/*
@Composable
fun TicketInfoCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().height(200.dp) // Placeholder size
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Ticket Info Placeholder")
        }
    }
}
*/

