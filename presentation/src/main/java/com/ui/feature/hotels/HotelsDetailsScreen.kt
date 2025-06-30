package com.ui.feature.hotels

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.tourismgraduationproject.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
	navController: NavController,
	hotelId: String,
	hotelDetailsViewModel: HotelsViewModel = koinViewModel(),
) {

	val hotelDetails by hotelDetailsViewModel.currentHotel2.collectAsState()
	val uiState by hotelDetailsViewModel.uiState.collectAsState()

	// Load hotel details if not already loaded
	LaunchedEffect(hotelId) {
		val hotels = (uiState as? HotelsUiState.Success)?.hotels
		val hotel = hotels?.find { it.id.toString() == hotelId }
		if (hotel != null) {
			hotelDetailsViewModel.loadHotelDetails(hotel)
		} else {
			hotelDetailsViewModel.loadHotels()
		}
	}

	when (uiState) {
		is HotelsUiState.Loading -> {
			Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				CircularProgressIndicator()
			}
		}

		is HotelsUiState.Error -> {
			Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				Text("Error loading hotel details")
			}
		}

		is HotelsUiState.Success -> {
			val context = LocalContext.current
			hotelDetails?.let { hotel ->

				var expandedText by remember { mutableStateOf(false) }

				Scaffold(
//					topBar = {
//						TopAppBar(
//							title = {
//								Text(
//									text = hotel.name,
//									style = MaterialTheme.typography.headlineMedium.copy(
//										fontWeight = FontWeight.Bold
//									)
//								)
//							},
//							navigationIcon = {
//								IconButton(onClick = { navController.navigateUp() }) {
//									Icon(
//										Icons.AutoMirrored.Filled.ArrowBack,
//										contentDescription = "Back"
//									)
//								}
//							},
//							modifier = Modifier.shadow(elevation = 4.dp)
//						)
//					}
//					bottomBar = {
//						Box(
//							Modifier
//								.fillMaxWidth()
//								.padding(16.dp)
//						) {
//							Button(
//								onClick = {
//									Toast.makeText(context, "Successfully added to cart", Toast.LENGTH_SHORT).show()
//									hotelDetailsViewModel.addCurrentHotelToCart()
//									navController.navigate(CheckoutTicketScreen)
//								},
//								modifier = Modifier
//									.fillMaxWidth()
//									.height(50.dp),
//								shape = RoundedCornerShape(8.dp)
//							) {
//								Text(text = "Book Now", fontSize = 16.sp)
//							}
//						}
//					}
				) { padding ->
					Column(
						modifier = Modifier
							.fillMaxSize()
							.padding(padding)
							.background(Color.White)
							.verticalScroll(rememberScrollState())
					) {
						Box(modifier = Modifier.fillMaxWidth()) {
							AsyncImage(
								modifier = Modifier
									.padding(16.dp)
									.fillMaxWidth()
									.height(400.dp)
									.clip(RoundedCornerShape(8.dp)),
								model = hotel.imageUrl,
								contentDescription = "Hotel Image",
								contentScale = ContentScale.Crop,
							)
						}

						Column(modifier = Modifier.padding(16.dp)) {
							Row(
								modifier = Modifier.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.Center
							) {
								Row(
									modifier = Modifier
										.padding(8.dp)
										.clip(RoundedCornerShape(8.dp))
										.background(Color.LightGray.copy(alpha = 0.3f))
										.padding(8.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Image(
										modifier = Modifier
											.size(24.dp)
											.padding(end = 4.dp),
										painter = painterResource(id = R.drawable.wifi),
										contentDescription = "Wifi Icon",
									)
									Text(
										text = "Free Wifi",
										fontSize = 14.sp,
									)
								}

								Row(
									modifier = Modifier
										.padding(8.dp)
										.clip(RoundedCornerShape(8.dp))
										.background(Color.LightGray.copy(alpha = 0.3f))
										.padding(8.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Image(
										modifier = Modifier
											.size(24.dp)
											.padding(end = 4.dp),
										painter = painterResource(id = R.drawable.breakfast),
										contentDescription = "Breakfast Icon",
									)
									Text(
										text = "Free Breakfast",
										fontSize = 14.sp,
									)
								}

								Row(
									modifier = Modifier
										.clip(RoundedCornerShape(8.dp))
										.background(Color.LightGray.copy(alpha = 0.3f))
										.padding(8.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Icon(
										modifier = Modifier
											.size(24.dp)
											.padding(end = 4.dp),
										painter = painterResource(id = R.drawable.star),
										tint = Color(0xFFFFD700),
										contentDescription = "Rating Icon",
									)
									Text(
										text = hotel.ratings,
										fontSize = 14.sp,
									)
								}
							}

							Spacer(modifier = Modifier.height(8.dp))

							Row(verticalAlignment = Alignment.CenterVertically) {
								Text(
									text = hotel.name,
									fontSize = 20.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.weight(1f)
								)
								Text(
									text = "${hotel.price}",
									fontSize = 18.sp,
									fontWeight = FontWeight.Bold,
									color = Color.Blue
								)
								Text(
									text = "/night",
									fontSize = 18.sp,
									color = Color.Gray.copy(alpha = 0.8f)
								)
							}

							Spacer(modifier = Modifier.height(8.dp))

							Text(
								text = hotel.location,
								fontSize = 14.sp,
								color = Color.Gray
							)

							Spacer(modifier = Modifier.height(16.dp))

							Text(
								text = "Description",
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold
							)

							Spacer(modifier = Modifier.height(8.dp))

							Column {
								Text(
									text = hotel.description,
									maxLines = if (expandedText) Int.MAX_VALUE else 3,
									overflow = TextOverflow.Ellipsis,
								)
								Text(
									text = if (expandedText) "Show Less" else "Read More...",
									color = Color.Blue,
									modifier = Modifier.clickable { expandedText = !expandedText }
								)
							}

							Spacer(modifier = Modifier.height(16.dp))

							Text(
								text = "Preview",
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold
							)

							Spacer(modifier = Modifier.height(8.dp))

							LazyRow(
								modifier = Modifier.fillMaxWidth()
							) {
								items(hotel.imagesOfHotelImages) { hotelImage ->
									Card(
										modifier = Modifier
											.width(200.dp)
											.height(150.dp)
											.padding(end = 16.dp)
											.clickable { },
										shape = RoundedCornerShape(16.dp),
										colors = CardDefaults.cardColors(containerColor = Color.White)
									) {
										Box(modifier = Modifier.fillMaxSize()) {
											AsyncImage(
												model = hotelImage.imageUrl,
												contentDescription = "Image",
												modifier = Modifier.fillMaxSize(),
												contentScale = ContentScale.Crop
											)
										}
									}
								}
							}

							Spacer(modifier = Modifier.height(16.dp))

						}
					}
				}
			}
		}
	}
}
