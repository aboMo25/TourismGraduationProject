package com.ui.feature.home

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.data.model.response.Destination
import com.domain.model.Product
import com.example.tourismgraduationproject.R
import com.navigation.DetailsScreen
import com.navigation.PackageListScreen
import com.ui.feature.packages.DestinationCard
import com.ui.feature.splash.TourismData
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
	navController: NavController,
	viewModel: HomeViewModel = koinViewModel(),
) {
	val uiState = viewModel.uiState.collectAsState()

	val loading = remember {
		mutableStateOf(false)
	}
	val error = remember {
		mutableStateOf<String?>(null)
	}
	val feature = remember {
		mutableStateOf<List<Product>>(emptyList())
	}
	val popular = remember {
		mutableStateOf<List<Product>>(emptyList())
	}
	val categories = remember {
		mutableStateOf<List<String>>(emptyList())
	}
	Scaffold {
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.padding(it).
				background(color =  Color(0xFFF5E1C0))
				.testTag("homeScreen")
		) {
			when (uiState.value) {

				is HomeScreenUIEvents.Loading -> {
					loading.value = true
					error.value = null
				}

				is HomeScreenUIEvents.Success -> {
					val data = (uiState.value as HomeScreenUIEvents.Success)
					feature.value = data.featured
					popular.value = data.popularProducts
					categories.value = data.categories
					loading.value = false
					error.value = null
				}

				is HomeScreenUIEvents.Error -> {
					val errorMsg = (uiState.value as HomeScreenUIEvents.Error).message
					loading.value = false
					error.value = errorMsg
				}
			}
			HomeContent(
				onCartClicked = {navController.navigate(PackageListScreen)},
                onViewAllClick = {navController.navigate(PackageListScreen)},
                onDestinationSelected = {navController.navigate(DetailsScreen)},
                onCreatePackageClick = {},
			)
		}
	}
}

@Composable
fun HomeContent(
	onCartClicked: () -> Unit,
	onViewAllClick: () -> Unit,
	onDestinationSelected: (Destination) -> Unit,
	onCreatePackageClick: () -> Unit,
) {
	var selectedDestination by remember { mutableStateOf<Destination?>(null) }
	val scrollState = rememberScrollState()
	LazyColumn(
		modifier = Modifier.background(color = Color(0xFFF5E1C0))
	) {
		item {
			ProfileHeader(onCartClicked)
			Spacer(modifier = Modifier.size(16.dp))
			SearchBar(searchedValue = "", onTextChanged = {})
			Spacer(modifier = Modifier.size(16.dp))
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
			SectionTitle(
				"Popular Destinations",
				"Show all",
				onClick = {}
			)
			DestinationList(
				destinations = TourismData.governorates,
				selectedDestination =  selectedDestination ,
				onDestinationSelected = { destination ->
					selectedDestination = destination
					onDestinationSelected(destination)
				}
			)

			Spacer(Modifier.height(16.dp))
			SectionTitle(
				"Recommended for You",
				"Show all",
				onClick = onViewAllClick
			)
			DestinationList(
				destinations = TourismData.governorates,
				selectedDestination = selectedDestination,
				onDestinationSelected = { destination ->
					selectedDestination = destination
					onDestinationSelected(destination)
				}
			)

			// Button at the bottom
//			CreatePackageButton(onCreatePackageClick)

		}
//		item {
//			if (isLoading) {
//				Column(
//					modifier = Modifier.fillMaxSize(),
//					verticalArrangement = Arrangement.Center,
//					horizontalAlignment = Alignment.CenterHorizontally
//				) {
//					CircularProgressIndicator(modifier = Modifier.size(50.dp))
//					Text(text = "Loading...", style = MaterialTheme.typography.bodyMedium)
//				}
//			}
//			errorMsg?.let {
//				Text(
//					text = it,
//					style = MaterialTheme.typography.bodyMedium
//				)
//			}
//			if (categories.isNotEmpty()) {
//				CategoryChips(
//					categories = listOf(
//						"Beach",
//						"Mountain",
//						"Cultural",
//						"Nature",
//						"Relaxation",
//						"Adventure"
//					),
//					selectedCategory = null,
//					onCategorySelected = {}
//				)
//			}
//			if (featured.isNotEmpty()) {
//				HomeProductRow(products = featured, title = "Featured", onClick = onClick)
//				Spacer(modifier = Modifier.size(16.dp))
//			}
//			if (popularProducts.isNotEmpty()) {
//				HomeProductRow(
//					products = popularProducts,
//					title = "Popular Products",
//					onClick = onClick
//				)
//			}
//		}
	}
}

@Composable
fun SectionTitle(
	title: String,
	actionText: String,
	onClick: () -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.bodyMedium,
			color = Color(0xFFE65100)

		)
		Text(
			text = actionText,
			modifier = Modifier.clickable { onClick() },
			style = MaterialTheme.typography.bodyMedium,
			color = Color(0xFFE65100)
		)
	}
}


@Composable
fun DestinationList(
	destinations: List<Destination>,
	selectedDestination: Destination?,
	onDestinationSelected: (Destination) -> Unit,
) {
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		items(destinations) { destination ->
			DestinationCardForHome(
				destination = destination,
				isSelected = selectedDestination == destination,
				onClick = { onDestinationSelected(destination) }
			)
		}
	}
}
@Composable
fun DestinationCardForHome(
	destination: Destination,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier
			.width(400.dp)
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
							.background(Color(0xFFF5E1C0).copy(alpha = 0.8f))
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
				.offset(y = 8.dp)
				.fillMaxWidth(0.8f)
				.shadow(6.dp, RoundedCornerShape(16.dp)),
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E1C0))
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
							shadow = Shadow(color = Color(0xFFE65100).copy(alpha = 0.2f), blurRadius = 2f)
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
fun CreatePackageButton(onClick: () -> Unit) {
	Button(
		onClick = onClick,
		modifier = Modifier
			.fillMaxWidth()
			.padding(24.dp)
	) {
		Text("Create Your Own Package")
	}
}

@Composable
@Preview
fun HomeContentPreview() {

}