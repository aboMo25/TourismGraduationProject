//package com.ui.feature.home.cart
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.domain.model.AllPlaces
//import com.domain.model.CartItem
//import com.domain.model.Hotels
//import com.navigation.CheckoutTicketScreen
//import com.navigation.hotelDetailsRoute
//import com.navigation.placeDetailsRoute
//import org.koin.androidx.compose.koinViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CartScreen(
//	navController: NavController,
//	viewModel: CartViewModel = koinViewModel(),
//) {
//
//	val uiState = viewModel.uiState.collectAsState()
//
//	Scaffold(
//		topBar = {
//			TopAppBar(
//				title = { Text(text = "Cart") },
//				navigationIcon = {
//					IconButton(onClick = { navController.navigateUp() }) {
//						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//					}
//				},
//				modifier = Modifier.shadow(4.dp)
//			)
//		}
//	) { padding ->
//		when (val state = uiState.value) {
//			is CartUiState.Loading -> {
//				Box(
//					modifier = Modifier
//						.fillMaxSize()
//						.padding(padding),
//					contentAlignment = Alignment.Center
//				) {
//					CircularProgressIndicator()
//				}
//			}
//
//			is CartUiState.Error -> {
//				Box(
//					modifier = Modifier
//						.fillMaxSize()
//						.padding(padding),
//					contentAlignment = Alignment.Center
//				) {
//					Text(
//						text = state.message,
//						style = MaterialTheme.typography.bodyLarge,
//						color = MaterialTheme.colorScheme.error
//					)
//				}
//			}
//
//			is CartUiState.Success -> {
//				val places = state.items.filterIsInstance<CartItem.PlaceItem>()
//				val hotels = state.items.filterIsInstance<CartItem.HotelItem>()
//				val total = state.items.sumOf {
//					when (it) {
//						is CartItem.PlaceItem -> it.place.price.toInt()
//						is CartItem.HotelItem -> it.hotel.price.toInt()
//					}
//				}
//
//				Box(
//					modifier = Modifier
//						.fillMaxSize()
//						.padding(padding)
//				) {
//					if (places.isEmpty() && hotels.isEmpty()) {
//						// ✅ Center the empty state message
//						Box(
//							modifier = Modifier.fillMaxSize(),
//							contentAlignment = Alignment.Center
//						) {
//							Text(
//								text = "Your cart is empty",
//								style = MaterialTheme.typography.titleLarge,
//								color = Color.Gray
//							)
//						}
//					} else {
//						LazyColumn(
//							modifier = Modifier
//								.fillMaxSize()
//								.padding(bottom = 100.dp),
//							contentPadding = PaddingValues(16.dp)
//						) {
//							if (places.isNotEmpty()) {
//								item {
//									Text("Places", style = MaterialTheme.typography.headlineMedium)
//								}
//								items(places) { placeItem ->
//									val place = placeItem.place
//									PlaceCartItem(
//										place = place,
//										onClick = {
//											navController.navigate(placeDetailsRoute(place.placeData.id.toString()))
//										}
//									)
//								}
//							}
//
//							if (places.isNotEmpty() && hotels.isNotEmpty()) {
//								item {
//									HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
//								}
//							}
//
//							if (hotels.isNotEmpty()) {
//								item {
//									Text("Hotels", style = MaterialTheme.typography.headlineMedium)
//								}
//								items(hotels) { hotelItem ->
//									HotelCartItem(
//										hotel = hotelItem.hotel,
//										onClick = {
//											navController.navigate(hotelDetailsRoute(hotelItem.hotel.id))
//										}
//									)
//								}
//							}
//						}
//
//						// ✅ Pinned cart summary
//						CartSummary(
//							totalPrice = total,
//							onBookNowClick = {
//								navController.navigate(CheckoutTicketScreen)
//							},
//							modifier = Modifier
//								.align(Alignment.BottomCenter)
//								.padding(16.dp)
//						)
//					}
//				}
//			}
//		}
//	}
//}
//
//
//
//@Composable
//fun PlaceCartItem(
//	place: AllPlaces,
//	onClick: () -> Unit,
//) {
//	CartItemCard(
//		imageUrl = place.imageUrl,
//		name = place.name,
//		price = place.price,
//		onClick = onClick
//	)
//}
//
//@Composable
//fun HotelCartItem(
//	hotel: Hotels,
//	onClick: () -> Unit,
//) {
//	CartItemCard(
//		imageUrl = hotel.imageUrl,
//		name = hotel.name,
//		price = hotel.price.toInt(),
//		onClick = onClick
//	)
//}
//
//@Composable
//fun CartItemCard(
//	imageUrl: String,
//	name: String,
//	price: Int,
//	onClick: () -> Unit,
//) {
//	Card(
//		modifier = Modifier
//			.fillMaxWidth()
//			.padding(vertical = 8.dp)
//			.clip(RoundedCornerShape(8.dp))
//			.clickable(onClick = onClick),
//		elevation = CardDefaults.cardElevation(4.dp),
//		colors = CardDefaults.cardColors(containerColor = Color.White)
//	) {
//		Row(
//			modifier = Modifier
//				.fillMaxWidth()
//				.padding(16.dp),
//			verticalAlignment = Alignment.CenterVertically
//		) {
//			AsyncImage(
//				model = imageUrl,
//				contentDescription = name,
//				modifier = Modifier
//					.size(80.dp)
//					.clip(RoundedCornerShape(8.dp)),
//				contentScale = ContentScale.Crop
//			)
//
//			Spacer(modifier = Modifier.width(16.dp))
//
//			Column(
//				modifier = Modifier.weight(1f)
//			) {
//				Text(
//					text = name,
//					style = MaterialTheme.typography.titleMedium,
//					maxLines = 1,
//					overflow = TextOverflow.Ellipsis
//				)
//				Text(
//					text = "$$price",
//					style = MaterialTheme.typography.bodyLarge,
//					color = MaterialTheme.colorScheme.primary
//				)
//			}
//
//			Text(
//				text = "Show Details",
//				style = MaterialTheme.typography.bodySmall
//			)
//		}
//	}
//}
//
//@Composable
//fun CartSummary(
//	totalPrice: Int,
//	onBookNowClick: () -> Unit,
//	modifier: Modifier = Modifier,
//) {
//	Card(
//		modifier = modifier
//			.fillMaxWidth(),
//		elevation = CardDefaults.cardElevation(4.dp),
//		colors = CardDefaults.cardColors(containerColor = Color.White),
//		shape = RoundedCornerShape(12.dp)
//	) {
//		Row(
//			modifier = Modifier
//				.fillMaxWidth()
//				.padding(16.dp),
//			verticalAlignment = Alignment.CenterVertically
//		) {
//			Button(
//				onClick = onBookNowClick,
//				shape = RoundedCornerShape(10.dp),
//				modifier = Modifier
//					.weight(1f)
//					.height(48.dp)
//			) {
//				Text("Book Now")
//			}
//			Spacer(modifier = Modifier.width(16.dp))
//			Text(
//				text = "$$totalPrice",
//				style = MaterialTheme.typography.titleMedium,
//				color = MaterialTheme.colorScheme.primary
//			)
//		}
//	}
//}
