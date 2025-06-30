package com.ui.feature.packages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourismgraduationproject.R
import com.navigation.DetailsScreen
import com.ui.feature.home.CategoryChips
import com.ui.feature.home.DestinationCard
import com.ui.feature.home.SearchBar
import com.domain.model.Trip
import com.ui.feature.packages.PackageListUiState.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageListScreen(
	navController: NavController,
	packageListViewModel: PackageListViewModel = koinViewModel(),
) {
	val uiState by packageListViewModel.uiState.collectAsState()
	var selectedTrip by remember { mutableStateOf<Trip?>(null) }

	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(
				title = {
					Text(
						"Explore All Trips ! ",
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
			is Loading -> {
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

			is Error -> {
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
							text = (uiState as Error).message,
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

			is Success -> {
				val trips = (uiState as Success).trips
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(top=padding.calculateTopPadding(), bottom = 0.dp)
				) {
					// Fixed header section
					Column(
						modifier = Modifier
							.fillMaxWidth()
//							.background(color = Color())
							.padding(16.dp)
					) {
						SearchBar(
							searchedValue = "Search for Trips",
							onTextChanged = {}
						)

						Spacer(modifier = Modifier.height(16.dp))
						CategoryChips(
							categories = listOf(
								"Beach",
								"Adventure",
								"Mountain",
								"Relaxation",
								"Nature"
							),
							icons = listOf(
								R.drawable.beach,
								R.drawable.adventure,
								R.drawable.mountain,
								R.drawable.relaxation,
								R.drawable.island_on_water
							)
						)
					}

					// Scrollable content
					LazyColumn(
						modifier = Modifier.fillMaxWidth(),
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						items(trips) { destination ->
							DestinationCard(
								trip = destination,
								onClick = { navController.navigate(DetailsScreen) },
								modifier = Modifier
									.fillMaxWidth()
									.padding(horizontal = 16.dp)
									.height(320.dp) // Increased height to accommodate extra details
									.shadow(12.dp, RoundedCornerShape(16.dp)) // Added shadow here
									.background(Color.White, RoundedCornerShape(16.dp)) // Background to maintain card feel
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
