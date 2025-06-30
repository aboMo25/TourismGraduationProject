package com.ui.feature.hotels

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.domain.model.Hotels2
import com.example.tourismgraduationproject.R
import com.ui.feature.packages.PackageListUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(
	navController: NavController,
	hotelsViewModel: HotelsViewModel = koinViewModel(),
) {

	val uiState by hotelsViewModel.uiState.collectAsState()
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(
				title = {
					Text(
						"Explore All Hotels ! ",
						style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
					)
				},
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
				scrollBehavior = scrollBehavior
			)
		}
	) { padding ->
		when (uiState) {
			is HotelsUiState.Loading -> {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						CircularProgressIndicator()
						Text(
							text = "Loading trips...",
							style = MaterialTheme.typography.bodyLarge,
							modifier = Modifier.padding(top = 16.dp)
						)
					}
				}
			}

			is HotelsUiState.Error -> {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						Icon(
							imageVector = Icons.Default.Check,
							contentDescription = "Error",
							tint = MaterialTheme.colorScheme.error,
							modifier = Modifier.size(48.dp)
						)
						Text(
							text = (uiState as PackageListUiState.Error).message,
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.error,
							modifier = Modifier.padding(top = 16.dp)
						)
						Button(
							onClick = { hotelsViewModel.loadHotels()},
							modifier = Modifier.padding(top = 8.dp)
						) {
							Text("Retry")
						}
					}
				}
			}

			is HotelsUiState.Success -> {
				val hotels = (uiState as HotelsUiState.Success).hotels
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(top=padding.calculateTopPadding(), bottom = 0.dp)
				) {
//					// Fixed header section
//					Column(
//						modifier = Modifier
//							.fillMaxWidth()
//							.padding(16.dp)
//					) {
//						Spacer(modifier = Modifier.height(16.dp))
//						CategoryChips(
//							categories = listOf("Beach", "Adventure", "Mountain", "Relaxation", "Nature"),
//							icons = listOf(R.drawable.beach, R.drawable.adventure, R.drawable.mountain, R.drawable.relaxation, R.drawable.island_on_water)
//						)
//					}
					// Scrollable content
					LazyColumn(
						modifier = Modifier.fillMaxWidth(),
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						items(hotels) { hotel ->
							HotelsCard(
								hotels = hotel,
								onClick = { navController.navigate("hotels_details_screen/${hotel.id}") },
								modifier = Modifier
									.fillMaxWidth()
									.padding(horizontal = 16.dp)
									.height(320.dp)
									.shadow(12.dp, RoundedCornerShape(16.dp))
									.background(Color.White )
							)
						}
						item {
							Spacer(modifier = Modifier.height(16.dp))
						}
					}
				}
			}
		}
	}
}
@Composable
fun HotelsCard(
	hotels: Hotels2,
	onClick: () -> Unit,
	modifier: Modifier
) {
	Box(
		modifier = modifier,
	) {
		// Image Card
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(250.dp)
				.shadow(3.dp)
				.clickable { onClick() },
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = hotels.imageUrl,
					contentDescription = hotels.name,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.Crop
				)
			}
		}

		// Details Section Below Image
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 260.dp, start = 8.dp, end = 8.dp) // Positioned below the image
		) {
			// First row: Name and Price
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = hotels.name,
					style = TextStyle(
						fontSize = 18.sp,
						fontWeight = FontWeight.Bold,
						shadow = Shadow(
							color = Color(0xFF000000).copy(alpha = 0.2f),
							blurRadius = 2f
						)
					),
					color = Color(0xFF000000),
					modifier = Modifier.weight(1f)
				)
				Text(
					text = hotels.price.toString(),
					style = TextStyle(
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold,
						color = Color(0xFF4CAF50)
					)
				)
			}

			Spacer(modifier = Modifier.height(4.dp))

			// Second row: Location and Rating
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						painter = painterResource(id = R.drawable.star),
						contentDescription = "Rating",
						tint = Color(0xFFFFD700),
						modifier = Modifier.size(14.dp)
					)
					Text(
						text = "${hotels.ratings} (${hotels.numberOfReviews} reviews )",
						fontSize = 14.sp,
						color = Color(0xFF000000),
						modifier = Modifier.weight(1f)

					)
					Icon(
						painter = painterResource(id = R.drawable.location),
						contentDescription = "Distance",
						tint = Color.Gray,
						modifier = Modifier.size(14.dp)
					)
					Text(text = "${hotels.location} km", fontSize = 14.sp, color = Color(0xFF000000))
				}
			}
		}
	}

}