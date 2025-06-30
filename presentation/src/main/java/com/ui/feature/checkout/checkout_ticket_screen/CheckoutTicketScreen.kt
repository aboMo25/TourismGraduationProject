package com.ui.feature.checkout.checkout_ticket_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.navigation.PassengerDetailsScreen
import com.ui.feature.checkout.passenger_details_screen.DetailTextField
import org.koin.androidx.compose.koinViewModel
import org.koin.core.Koin

// Assuming R.drawable.qatar_airways_logo exists
// Assuming R.drawable.airplane_icon exists
// Assuming R.drawable.australia_flag exists

// --- Screen 1: Checkout Ticket (Initial) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutTicketScreen(
	navController: NavController,
	viewModel: CheckoutTicketScreenViewModel = koinViewModel(),
) {
	Scaffold(
		topBar = {
			CenterAlignedTopAppBar(
				title = { Text("Checkout Ticket", fontWeight = FontWeight.Bold) },
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
				actions = {
					IconButton(
						onClick = { /* TODO: Handle more options */ }) {
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
			CheckoutBottomBar(
				price = "$110",
				passengerInfo = "(1 Adult Passenger)",
				buttonText = "Checkout Ticket"
			) {
				// Navigate to Payment Screen
				navController.navigate(PassengerDetailsScreen)
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
//			Text(
//				"Your Ticket",
//				style = MaterialTheme.typography.titleMedium,
//				fontWeight = FontWeight.Bold
//			)
//			Spacer(modifier = Modifier.height(8.dp))
//			TicketInfoCard()
			Spacer(modifier = Modifier.height(24.dp))
			Text(
				"Your Personal Information",
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Bold
			)
			Spacer(modifier = Modifier.height(8.dp))
			PersonalInfoCard(navController)
			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}

@Composable
fun TicketInfoCard() {
	Card(
		shape = RoundedCornerShape(12.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		modifier = Modifier.fillMaxWidth()
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				InfoChip("Free luggage 10Kg" /* painterResource(id = R.drawable.ic_luggage) */)
				InfoChip("Christmas Disc" /* painterResource(id = R.drawable.ic_discount) */)
			}
			Spacer(modifier = Modifier.height(8.dp))
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				InfoChip("Free Cabin 8Kg" /* painterResource(id = R.drawable.ic_cabin) */)
				InfoChip("Free Reschedule" /* painterResource(id = R.drawable.ic_reschedule) */)
			}
			Spacer(modifier = Modifier.height(16.dp))
			HorizontalDivider(color = Color.LightGray)
			Spacer(modifier = Modifier.height(16.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				FlightEndpoint(city = "Jakarta", code = "CGK", time = "07.15 PM")
				FlightDuration(duration = "2H 45M", flightNo = "QA-1389")
				FlightEndpoint(
					city = "Bali",
					code = "DPS",
					time = "09.15 PM",
					alignment = Alignment.End
				)
			}
			Spacer(modifier = Modifier.height(16.dp))
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				// Replace with actual logo if available
				Text("QATAR AIRWAYS", fontWeight = FontWeight.Bold, color = Color(0xFF5C0F3A))
				// Image(painter = painterResource(id = R.drawable.qatar_airways_logo), contentDescription = "Qatar Airways Logo", modifier = Modifier.height(24.dp))
				Text(
					"$110",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}

@Composable
fun InfoChip(text: String, icon: Painter? = null) {
	SuggestionChip(
		onClick = { /* No action needed */ },
		label = { Text(text, fontSize = 12.sp) },
		// icon = { if (icon != null) Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp)) },
		shape = RoundedCornerShape(8.dp),
		colors = SuggestionChipDefaults.suggestionChipColors(
			containerColor = Color(0xFFE0F7FA) // Light blue background
		),
		border = BorderStroke(8.dp, Color.Red)
	)
}

@Composable
fun FlightEndpoint(
	city: String,
	code: String,
	time: String,
	alignment: Alignment.Horizontal = Alignment.Start,
) {
	Column(horizontalAlignment = alignment) {
		Text(city, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
		Text(code, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
		Text(time, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
	}
}

@Composable
fun FlightDuration(duration: String, flightNo: String) {
	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Text(flightNo, fontSize = 10.sp, color = Color.Gray)
		Row(verticalAlignment = Alignment.CenterVertically) {
			HorizontalDivider(modifier = Modifier.width(20.dp), color = Color.LightGray)
			Spacer(modifier = Modifier.width(8.dp))
			// Replace with actual icon if available
			Box(modifier = Modifier
				.size(24.dp)
				.clip(CircleShape)
				.background(Color.Black)) {
				Icon(
					painter = painterResource(id = android.R.drawable.ic_menu_send),
					contentDescription = "Flight Icon",
					tint = Color.White,
					modifier = Modifier
						.align(Alignment.Center)
						.size(16.dp)
				)
				// Image(painter = painterResource(id = R.drawable.airplane_icon), contentDescription = "Flight Icon", modifier = Modifier.size(16.dp).align(Alignment.Center))
			}
			Spacer(modifier = Modifier.width(8.dp))
			HorizontalDivider(modifier = Modifier.width(20.dp), color = Color.LightGray)
		}
		Text(duration, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
	}
}

@Composable
fun PersonalInfoCard(navController: NavController) {
	var fullName by remember { mutableStateOf("Brooklyn Joseph Simmons") }
	var email by remember { mutableStateOf("Joseph@gmail.com") }
	var totalAdultPassengers by remember { mutableStateOf("1")}
	var totalYoungPassengers by remember { mutableStateOf("1")}

	Card(
		shape = RoundedCornerShape(12.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
		modifier = Modifier.fillMaxWidth()
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			DetailTextField(label = "Your Full Name", value = fullName, onValueChange = {fullName =it})
			HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(vertical = 12.dp))
			DetailTextField(label = "Email", value = email, onValueChange = {email =it}, keyboardType = KeyboardType.Email)
			HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(vertical = 12.dp))
			Text(text = "Passengers")
			DetailTextField(label = "Total Adult passengers", value = totalAdultPassengers, onValueChange = {totalAdultPassengers =it}, keyboardType = KeyboardType.Number)
			Spacer(modifier = Modifier.height(8.dp))
			DetailTextField(label = "Total young passengers", value = totalYoungPassengers, onValueChange = {totalYoungPassengers =it}, keyboardType = KeyboardType.Number )

		}
		Spacer(modifier = Modifier.height(16.dp))
	}
}
@Composable
fun CheckoutBottomBar(
	price: String,
	passengerInfo: String,
	buttonText: String,
	onButtonClick: () -> Unit,
) {
	Surface(
		color = Color.White,
		shadowElevation = 8.dp // Add shadow to the bottom bar
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column {
				Text("Ticket Price", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
				Row {
					Text(
						price,
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.Bold
					)
					Spacer(modifier = Modifier.width(4.dp))
					Text(
						passengerInfo,
						style = MaterialTheme.typography.bodySmall,
						color = Color.Gray,
						modifier = Modifier.align(Alignment.Bottom)
					)
				}
			}
			Button(
				onClick = onButtonClick,
				shape = RoundedCornerShape(8.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
			) {
				Text(buttonText, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
			}
		}
	}
}

@Preview(showBackground = true, device = "spec:width=360dp,height=740dp")
@Composable
fun CheckoutTicketScreenPreview() {
	// Basic theme wrapper for preview
	MaterialTheme {
		CheckoutTicketScreen(rememberNavController())
	}
}

