package com.ui.feature.ticket

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tourismgraduationproject.R
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun MainTicket(
	navController: NavController,
	viewModel: TicketViewModel = koinViewModel()
) {
	val uiState by viewModel.uiState.collectAsState()
	LaunchedEffect(Unit) {
		viewModel.loadTickets()
	}

	when (val state = uiState) {
		is TicketUiState.Loading -> LoadingScreen()
		is TicketUiState.Error -> ErrorScreen(state.error.localizedMessage ?: "Unknown error")
		is TicketUiState.Success -> {
			if (state.tickets.isNotEmpty()) {
				val ticket = state.tickets.first()
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					TicketScreen(
						ticketNumber = ticket.id.toString(),
						ticketData = "Ticket-${ticket.id}",
						ticketInfo = ticket
					)
				}
			} else {
				EmptyTicketScreen()
			}
		}
	}
}

@Composable
fun LoadingScreen() {
	Box(
		modifier = Modifier.fillMaxSize().background(Color.White),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator()
	}
}

@Composable
fun ErrorScreen(message: String) {
	Box(
		modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			Icon(
				painter = painterResource(id = R.drawable.eye),
				contentDescription = "Error",
				modifier = Modifier.size(48.dp),
				tint = Color.Red
			)
			Spacer(modifier = Modifier.height(16.dp))
			Text(text = "Failed to load ticket: $message", color = Color.Red)
		}
	}
}

@Composable
fun EmptyTicketScreen() {
	Box(
		modifier = Modifier.fillMaxSize().background(Color.White),
		contentAlignment = Alignment.Center
	) {
		Text("No tickets available")
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketScreen(ticketNumber: String, ticketData: String, ticketInfo: Ticket) {
	val qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=$ticketData"

	Column(modifier = Modifier.padding(16.dp)) {
		Text("MY TICKET", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
		Text("The ticket that is already active!", fontSize = 14.sp, color = Color.Gray)
		Spacer(modifier = Modifier.height(16.dp))

		TicketCard(qrCodeUrl = qrCodeUrl, ticketNumber = ticketNumber, ticketInfo = ticketInfo)
	}
}

@Composable
fun TicketCard(qrCodeUrl: String, ticketNumber: String, ticketInfo: Ticket) {
	Box(
		modifier = Modifier.clip(RoundedCornerShape(12.dp)).border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)).background(Color.White)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text(ticketInfo.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
			Text(ticketInfo.description, fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 12.dp))

			DividerLine()

			InfoRow(iconRes = R.drawable.star, title = "Price", value = "$${ticketInfo.price}")
			InfoRow(iconRes = R.drawable.ic_cart, title = "Quantity", value = ticketInfo.quantity.toString())

			Spacer(modifier = Modifier.height(16.dp))

			QRCodeSection(qrCodeUrl = qrCodeUrl, ticketNumber = ticketNumber)
		}
	}
}

@Composable
fun QRCodeSection(qrCodeUrl: String, ticketNumber: String) {
	Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
		Text("Scan this QR code", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))

		AsyncImage(
			model = qrCodeUrl,
			contentDescription = "QR Code",
			contentScale = ContentScale.Fit,
			modifier = Modifier.size(150.dp)
		)

		Spacer(modifier = Modifier.height(8.dp))
		Text(ticketNumber, fontSize = 16.sp, fontWeight = FontWeight.Bold)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			Text("Printed : ${LocalDate.now()}", fontSize = 12.sp, color = Color.Gray)
		}
	}
}

@Composable
fun InfoRow(iconRes: Int, title: String, value: String) {
	Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
		Icon(
			painter = painterResource(id = iconRes),
			contentDescription = title,
			tint = Color.Gray,
			modifier = Modifier.size(20.dp)
		)
		Spacer(modifier = Modifier.width(12.dp))
		Column {
			Text(title, fontSize = 14.sp, color = Color.Gray)
			Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
		}
	}
}

@Composable
fun DividerLine() {
	Box(
		modifier = Modifier.fillMaxWidth().height(1.dp).background(
			Brush.horizontalGradient(listOf(Color.Transparent, Color.Gray, Color.Transparent))
		).padding(vertical = 12.dp)
	)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TicketScreenPreview() {
	MaterialTheme {
		TicketScreen(
			ticketNumber = "20242410003",
			ticketData = "Ticket-20242410003",
			ticketInfo = Ticket(
				id = 20242410003.toInt(),
				title = "Joy Fantasy Castle",
				description = "Joyworld Fantasy",
				price = 49.99,
				quantity = 1
			)
		)
	}
}
