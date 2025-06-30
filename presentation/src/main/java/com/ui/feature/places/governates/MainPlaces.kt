package com.ui.feature.places.governates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.domain.model.MainPlace2
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesOfInterestScreen(
	navController: NavController,
	viewModel: PlacesOfInterestViewModel = koinViewModel(),
) {
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
	val uiState by viewModel.uiState.collectAsState()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(
				title = {
					Text(
						"All Destinations",
						style = MaterialTheme.typography.headlineMedium.copy(
							fontWeight = FontWeight.Bold
						)
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
	) { innerPadding ->
		when (uiState) {
			is PlacesOfInterestUiState.Loading -> {
				// Show a progress indicator or placeholder
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding),
					contentAlignment = Alignment.Center
				) {
					Text("Loading...")
				}
			}

			is PlacesOfInterestUiState.Error -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding),
					contentAlignment = Alignment.Center
				) {
					Text(text = (uiState as PlacesOfInterestUiState.Error).message)
				}
			}

			is PlacesOfInterestUiState.Success -> {
				val mainPlace = (uiState as PlacesOfInterestUiState.Success).mainPlace
				LazyVerticalGrid(
					columns = GridCells.Fixed(2),
					contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.fillMaxSize()
						.padding(top = innerPadding.calculateTopPadding())
				) {
					items(mainPlace) { mainPlace ->
						MainPlace(
							mainPlace = mainPlace,
							onClick = {
								navController.navigate("places_screen/${mainPlace.id}")
								println("Clicked MainPlace ID: ${mainPlace.id}")

							}
						)
					}
				}
			}
		}
	}
}


@Composable
fun MainPlace(
	mainPlace: MainPlace2,
	onClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.clip(RoundedCornerShape(16.dp))
			.fillMaxWidth()
			.padding(4.dp)
	) {
		// Image Card
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(350.dp)
				.clickable { onClick() },
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = mainPlace.imageUrl,
					contentDescription = mainPlace.name,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.Crop
				)
				// Centered Place Name Overlay
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(Color.Black.copy(alpha = 0.3f)) // Semi-transparent overlay
						.padding(16.dp),
					contentAlignment = Alignment.Center
				) {
					Text(
						text = mainPlace.name,
						style = TextStyle(
							fontSize = 32.sp,
							fontWeight = FontWeight.Bold,
							color = Color.White,
							shadow = Shadow(
								color = Color.Black.copy(alpha = 0.7f),
								blurRadius = 8f,
								offset = Offset(2f, 2f)
							)
						),
						textAlign = TextAlign.Center,
						modifier = Modifier.fillMaxWidth()
					)
				}
			}
		}
	}
}

