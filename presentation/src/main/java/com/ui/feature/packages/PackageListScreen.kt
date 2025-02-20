package com.ui.feature.packages

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.data.model.response.Destination
import com.example.tourismgraduationproject.R
import com.navigation.DetailsScreen
import com.ui.feature.home.CategoryChips
import com.ui.feature.home.SearchBar
import com.ui.feature.splash.TourismData
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageListScreen(
	navController: NavController,
	packageListViewModel: PackageListViewModel = koinViewModel(),
) {
	val uiState by packageListViewModel.uiState.collectAsState()
	var selectedDestination by remember { mutableStateOf<Destination?>(null) }

	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			TopAppBar(
				title = { Text("Customize Your Journey") },
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xFFF5E1C0),
					scrolledContainerColor =Color(0xFFF5E1C0)
				),
				scrollBehavior = scrollBehavior
			)
		}
	) { padding ->
		when (uiState) {
			is PackageListUiState.Loading -> {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						CircularProgressIndicator()
						Text(
							text = "Loading destinations...",
							style = MaterialTheme.typography.bodyLarge,
							modifier = Modifier.padding(top = 16.dp)
						)
					}
				}
			}

			is PackageListUiState.Error -> {
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
							onClick = { packageListViewModel.loadDestinations() },
							modifier = Modifier.padding(top = 8.dp)
						) {
							Text("Retry")
						}
					}
				}
			}

			is PackageListUiState.Success -> {
				Column(
					modifier = Modifier
						.background(color = Color(0xFFF5E1C0))
						.fillMaxSize()
						.padding(padding)
				) {
					// Fixed header section
					Column(
						modifier = Modifier
							.fillMaxWidth()
//							.background(color = Color())
							.padding(16.dp)
					) {
						SearchBar(
							searchedValue = "SELECT YOUR Destination",
							onTextChanged = {}
						)

						Spacer(modifier = Modifier.height(16.dp))

						CategoryChips(
							categories = listOf(
								"Beach",
								"Mountain",
								"Cultural",
								"Nature",
								"Relaxation",
								"Adventure"
							),
							selectedCategory = "Beach",
							onCategorySelected = {}
						)
					}

					// Scrollable content
					LazyColumn(
						modifier = Modifier.fillMaxWidth(),
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						items(TourismData.governorates) { destination ->
							DestinationCard(
								destination = destination,
								isSelected = selectedDestination == destination,
								onClick = { navController.navigate(DetailsScreen) }
							)
						}

						item {
							Spacer(modifier = Modifier.height(16.dp))
						}
					}
				}
			}

			else -> {}
		}
	}
}

@Composable
fun DestinationCard(
	destination: Destination,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp)
			.height(290.dp)
	) {
		// Image Card
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(250.dp)
				.shadow(8.dp, RoundedCornerShape(16.dp))
				.clickable { onClick() },
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = destination.imageUrl,
					contentDescription = destination.name,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.Crop
				)
				// Favorite Icon
				Box(
					modifier = Modifier
                        .clip(CircleShape)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
					contentAlignment = Alignment.TopEnd
				){
					Image(
						painter = painterResource(id = R.drawable.favorite),
						contentDescription = null,
						modifier = Modifier
							.size(36.dp)
							.clip(CircleShape)
							.background(Color.White.copy(alpha = 0.8f))
							.clickable { }
							.align(Alignment.TopEnd)
							.padding(8.dp),
						contentScale = ContentScale.Inside
					)
				}

			}
		}
		// Destination Info Card (Overlay)
		Card(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.offset(y = 20.dp)
				.fillMaxWidth(0.9f)
				.shadow(6.dp, RoundedCornerShape(16.dp)),
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor =Color(0xFFF5E1C0))
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp)
			) {
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = destination.name,
						style = TextStyle(
							fontSize = 18.sp,
							fontWeight = FontWeight.Bold,
							shadow = Shadow(color = Color(0xFFF5E1C0).copy(alpha = 0.2f), blurRadius = 2f)
						),
						modifier = Modifier.weight(1f),
						color = Color(0xFFE65100)
					)
					Text(
						text = "$150/per day",
						style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
					)
				}
				Text(
					text = "Atwab",
					style = TextStyle(fontSize = 14.sp, color = Color(0xFFE65100))
				)
				Spacer(modifier = Modifier.height(8.dp))
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						painter = painterResource(id = R.drawable.star),
						contentDescription = "Rating",
						tint = Color(0xFFFFD700),
						modifier = Modifier.size(14.dp)
					)
					Text(text = "4.5 ($193 reviews)", fontSize = 14.sp, color = Color(0xFFE65100))
					Spacer(modifier = Modifier.width(8.dp))
					Icon(
						painter = painterResource(id = R.drawable.location),
						contentDescription = "Distance",
						tint = Color.Gray,
						modifier = Modifier.size(14.dp)
					)
					Text(text = "35.5 km", fontSize = 14.sp, color = Color(0xFFE65100))
				}
			}
		}
	}
	Spacer(modifier = Modifier.height(8.dp))

}


@Composable
@Preview
fun DestinationCardPreview() {
	DestinationCard(
		destination = TourismData.governorates[0],
		isSelected = false,
		onClick = {}
	)
}

