// com.ui.feature.cart/CartScreen.kt
package com.ui.feature.cart

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.domain.model.AllPlaces2
import com.domain.model.CartItem2
import com.domain.model.Hotels2
import com.navigation.CheckoutTicketScreen
import com.navigation.Screens
import org.koin.androidx.compose.koinViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen2(
	navController: NavController,
	viewModel: CartViewModel2 = koinViewModel() // Inject the ViewModel
) {
	val cartItems by viewModel.cartItems.collectAsState()
	val loading by viewModel.loading.collectAsState()
	val error by viewModel.error.collectAsState()
	val context = LocalContext.current // For Toast messages

	// Observe navigation events
	LaunchedEffect(Unit) {
		viewModel.navigateToTicketDetails.collect { bookingId ->
			navController.navigate(Screens.TicketDetails.route + "/$bookingId")
		}
	}

	// Observe user messages (e.g., for Toast/Snackbar)
	LaunchedEffect(Unit) {
		viewModel.userMessage.collect { message ->
			Toast.makeText(context, message, Toast.LENGTH_LONG).show()
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						"My Cart",
						style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
					)
				}
			)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		) {
			when {
				loading -> {
					Column(
						modifier = Modifier.fillMaxSize(),
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						CircularProgressIndicator()
						Text("Loading cart...", modifier = Modifier.padding(top = 16.dp))
					}
				}
				error != null -> {
					Column(
						modifier = Modifier.fillMaxSize(),
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Error: ${error}",
							color = MaterialTheme.colorScheme.error,
							modifier = Modifier.padding(16.dp)
						)
						Button(onClick = { viewModel.loadCartItems() }) {
							Text("Retry")
						}
					}
				}
				cartItems.isEmpty() -> {
					Box(
						modifier = Modifier.fillMaxSize(),
						contentAlignment = Alignment.Center
					) {
						Text("Your cart is empty!", style = MaterialTheme.typography.bodyLarge)
					}
				}
				else -> {
					Column(modifier = Modifier.fillMaxSize()) {
						LazyColumn(
							modifier = Modifier
								.weight(1f) // Occupy available space
								.fillMaxWidth()
								.padding(8.dp),
							verticalArrangement = Arrangement.spacedBy(8.dp)
						) {
							items(cartItems, key = { it.id.toString() }) { item ->
								CartItemCard(item = item, viewModel = viewModel)
							}
						}
						// Cart summary and checkout button
						CartSummary(cartItems = cartItems, viewModel = viewModel,navController=navController)
					}
				}
			}
		}
	}
}

// ... (CartItemCard and InfoCard remain the same)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CartSummary(cartItems: List<CartItem2>, viewModel: CartViewModel2,navController: NavController) {
	val totalPrice = cartItems.sumOf { (it.price ?: 0.0) }
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text("Total Items:", style = MaterialTheme.typography.bodyLarge)
			Text(cartItems.size.toString(), style = MaterialTheme.typography.bodyLarge) // Display item count
		}
		Spacer(modifier = Modifier.height(8.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text("Total Price:", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
			Text("$${String.format("%.2f", totalPrice)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
		}
		Spacer(modifier = Modifier.height(16.dp))
		Button(
			onClick = {
				viewModel.proceedToCheckout()
				navController.navigate(CheckoutTicketScreen) // Navigate to ticket details
			},
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(8.dp)
		) {
			Text("Proceed to Checkout")
		}
		Spacer(modifier = Modifier.height(8.dp))
		Button(
			onClick = { viewModel.clearCart() },
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(8.dp),
			colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
		) {
			Text("Clear Cart")
		}
	}
}

@Composable
fun CartItemCard(item: CartItem2, viewModel: CartViewModel2) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(8.dp))
			.clickable { /* Navigate to item details */ }
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = rememberAsyncImagePainter(item.imageUrl),
				contentDescription = item.name,
				modifier = Modifier
					.size(80.dp)
					.clip(RoundedCornerShape(4.dp)),
				contentScale = ContentScale.Crop
			)
			Spacer(modifier = Modifier.width(16.dp))
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = item.name,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)
				Spacer(modifier = Modifier.height(4.dp))
				// Display type-specific details if needed
				when (item) {
					is AllPlaces2 -> {
						Text(text = item.description ?: "No description", fontSize = 12.sp)
						Text(
							text = "Location: ${item.placeData?.location ?: "N/A"}",
							fontSize = 12.sp
						)
					}

					is Hotels2 -> {
						Text(text = item.location ?: "No location", fontSize = 12.sp)
						Text(text = "Rating: ${item.ratings ?: "N/A"}", fontSize = 12.sp)
					}
					// Add Transports if applicable
					else -> {
						// Default details for other CartItem2 types
					}
				}
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = "Price: $${String.format("%.2f", item.price)}",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.primary
				)
				Spacer(modifier = Modifier.height(8.dp))
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					Spacer(modifier = Modifier.weight(1f)) // Push delete to end
					IconButton(
						onClick = { viewModel.removeItemFromCart(item.id.toString()) },
						modifier = Modifier.size(24.dp)
					) {
						Icon(
							Icons.Default.Delete,
							contentDescription = "Remove from Cart",
							tint = MaterialTheme.colorScheme.error
						)
					}
				}
			}
		}
	}
}
