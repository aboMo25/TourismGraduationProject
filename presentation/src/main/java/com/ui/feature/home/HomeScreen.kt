package com.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.domain.model.Trip
import com.example.tourismgraduationproject.R
import com.navigation.PackageListScreen
import com.ui.feature.home.util.AnimationUtils
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
	navController: NavController,
	viewModel: HomeViewModel = koinViewModel(),
) {
	val uiState by viewModel.uiState.collectAsState()
	val trips by viewModel.trips.collectAsState()

	LaunchedEffect(Unit) {
		if (trips.isEmpty()) viewModel.loadTrips()
	}

	Scaffold { padding ->
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.padding(bottom = padding.calculateBottomPadding())
		) {
			when (uiState) {
				is HomeScreenUIEvents.Loading -> LoadingState()
				is HomeScreenUIEvents.Error -> ErrorState((uiState as HomeScreenUIEvents.Error).message)
				is HomeScreenUIEvents.Success -> SuccessContent(trips, navController)
			}
		}
	}
}

@Composable
fun LoadingState() {
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		Column(horizontalAlignment = Alignment.CenterHorizontally) {
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeScaleIn) {
				CircularProgressIndicator(modifier = Modifier.size(48.dp))
			}
			Spacer(modifier = Modifier.height(16.dp))
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeSlideInBottom) {
				Text("Loading trips...", style = MaterialTheme.typography.bodyLarge)
			}
		}
	}
}

@Composable
fun ErrorState(message: String) {
	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		AnimatedVisibility(visible = true, enter = AnimationUtils.FadeSlideInStart) {
			Text(message, color = MaterialTheme.colorScheme.error)
		}
	}
}

@Composable
fun SuccessContent(trips: List<Trip>, navController: NavController) {
	LazyColumn(modifier = Modifier.fillMaxSize()) {
		item {
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeSlideInTop) {
				ProfileHeader(navController)
			}
		}
		item {
			Spacer(modifier = Modifier.height(16.dp))
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeSlideInStart) {
				SearchBar(searchedValue = "", onTextChanged = {})
			}
		}
		item {
			Spacer(modifier = Modifier.height(16.dp))
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeScaleIn) {
				CategoryChips(
					categories = listOf("Beach", "Adventure", "Mountain", "Relaxation", "Nature"),
					icons = listOf(
						R.drawable.beach,
						R.drawable.adventure,
						R.drawable.mountain,
						R.drawable.relaxation,
						R.drawable.island_on_water
					)
				)
			}
		}
		item {
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeSlideInBottom) {
				SectionTitle("Popular Destinations", "Show all") {
					navController.navigate(PackageListScreen)
				}
			}
		}
		item {
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeExpandIn) {
				DestinationListRow(trips = trips, navController = navController)
			}
		}
		item {
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeSlideInBottom) {
				SectionTitle("Recommended for You", "Show all") {
					navController.navigate(PackageListScreen)
				}
			}
		}
		item {
			AnimatedVisibility(visible = true, enter = AnimationUtils.FadeExpandIn) {
				DestinationListColumn(trips = trips, navController = navController)
			}
		}
		item { Spacer(modifier = Modifier.height(46.dp)) }
	}
}

