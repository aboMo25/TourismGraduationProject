package com.ui.feature.places.mainPlaces

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.domain.model.AllPlaces
import com.domain.model.AllPlaces2
import com.domain.model.Trip
import com.example.tourismgraduationproject.R
import com.ui.feature.home.PopularDestinationCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
	navController: NavController,
	mainPlaceId: String,
	viewModel: PlacesViewModel = koinViewModel()
) {
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	LaunchedEffect(mainPlaceId) {
		viewModel.loadPlacesFor(mainPlaceId)
	}

	val uiState = viewModel.uiState

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Places") },
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
				scrollBehavior = scrollBehavior
			)
		}
	) { innerPadding ->
		when {
			uiState.isLoading -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding),
					contentAlignment = Alignment.Center
				) {
					Text("Loading...")
				}
			}
			uiState.error != null -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding),
					contentAlignment = Alignment.Center
				) {
					Text("Error: ${uiState.error}")
				}
			}
			uiState.places.isEmpty() -> {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding),
					contentAlignment = Alignment.Center
				) {
					Text("No places found.")
				}
			}
			else -> {
				LazyColumn(
					contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp),
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding)
				) {
					items(uiState.places) { place ->
						PopularDestinationCard(
							places = place,
							onClick = {
								navController.navigate("place_details_screen/${place.placeData.id}")
							},
							modifier = Modifier.padding(16.dp)
						)
					}
				}
			}
		}
	}
}

@Composable
fun PopularDestinationCard(
	places: AllPlaces2,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val interactionSource = remember { MutableInteractionSource() }
	val isPressed by interactionSource.collectIsPressedAsState()
	val scale by animateFloatAsState(
		targetValue = if (isPressed) 0.98f else 1f,
		animationSpec = tween(100)
	)
	Card(
		modifier = modifier
			.graphicsLayer(scaleX = scale, scaleY = scale)
			.clickable(
				interactionSource = interactionSource,
				indication = null,
				onClick = onClick
			)
			.animateContentSize()
			.animateContentSize(),
		shape = RoundedCornerShape(16.dp),
		colors = CardDefaults.cardColors(containerColor = Color.White),
		elevation = CardDefaults.cardElevation(if (isPressed) 2.dp else 4.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(180.dp)
		) {
			AsyncImage(
				model = places.imageUrl,
				contentDescription = places.name,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.width(180.dp)
					.fillMaxHeight()
					.clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
			)

			Spacer(modifier = Modifier.width(12.dp))

			Column(
				modifier = Modifier
					.padding(vertical = 12.dp)
					.weight(1f),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Text(
					text = "10% Off",
					color = Color.Gray,
					fontSize = 11.sp,
					fontWeight = FontWeight.SemiBold,
					modifier = Modifier
						.background(Color(0xFFE0E0E0), shape = RoundedCornerShape(6.dp))
						.padding(horizontal = 6.dp, vertical = 2.dp)
				)

				Column {
					Text(
						text = places.name,
						style = MaterialTheme.typography.titleMedium,
						maxLines = 1
					)
					Text(
						text = "Soneva Jani, Maldives",
						fontSize = 13.sp,
						color = Color.Gray
					)
				}

				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						imageVector = Icons.Default.DateRange,
						contentDescription = null,
						tint = Color.Gray,
						modifier = Modifier.size(14.dp)
					)
					Spacer(modifier = Modifier.width(4.dp))
					Text(
						text = "1 Day 1 Night",
						fontSize = 12.sp,
						color = Color.Gray
					)
				}

				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(
						text = "$${places.price}",
						color = Color(0xFF4CAF50),
						fontWeight = FontWeight.Bold,
						fontSize = 16.sp
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = "$86.00",
						color = Color.Red,
						fontSize = 13.sp,
						textDecoration = TextDecoration.LineThrough
					)
				}
			}
		}
	}
}

