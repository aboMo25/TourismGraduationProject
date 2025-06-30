package com.ui.feature.customized

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.domain.model.CustomizedTripData
import com.navigation.HotelsScreen
import com.navigation.PlacesOfInterestScreen
import com.navigation.PlacesScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizedScreen(
	navController: NavController,
	viewModel: CustomizedScreenViewModel = koinViewModel(),
) {
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
	val uiState by viewModel.uiState.collectAsState()
	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(
				title = {
					Text(
						"Customize Your Nice Trip !",
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
			is CustomizedScreenUiState.Loading -> {
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

			is CustomizedScreenUiState.Error -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding),
					contentAlignment = Alignment.Center
				) {
					Text(text = (uiState as CustomizedScreenUiState.Error).message)
				}
			}

			is CustomizedScreenUiState.Success -> {
				val customizedTripData = (uiState as CustomizedScreenUiState.Success).customizedTripData
				LazyVerticalGrid(
					columns = GridCells.Adaptive(225.dp),
					contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.fillMaxSize()
						.padding(top = innerPadding.calculateTopPadding())
				) {
					items(customizedTripData) {customizedTripData ->
						MainUiCard (
							customizedTripData = customizedTripData,
							onClick = {
								when(customizedTripData.id){
									"1" -> navController.navigate(PlacesOfInterestScreen)
									"2" -> navController.navigate(HotelsScreen)
									"3" -> navController.navigate(HotelsScreen)
								}
							}
						)
					}
				}
			}
		}
	}
}

@Composable
fun MainUiCard(
	customizedTripData: CustomizedTripData,
	onClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.clip(RoundedCornerShape(16.dp))
			.fillMaxWidth()
	) {
		// Details Row
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 12.dp, vertical = 8.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = customizedTripData.name,
				style = TextStyle(
					fontSize = 24.sp,
					fontWeight = FontWeight.Bold,
					shadow = Shadow(
						color = Color(0xFF000000).copy(alpha = 0.2f),
						blurRadius = 2f
					)
				),
				color = Color(0xFF000000),
				modifier = Modifier.weight(1f),
			)
		}
		// Image Card
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(235.dp)
				.clickable { onClick() },
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = customizedTripData.imageUrl,
					contentDescription = customizedTripData.name,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.Crop
				)
			}
		}
	}
}
