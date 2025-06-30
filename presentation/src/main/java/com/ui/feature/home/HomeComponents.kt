package com.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.domain.model.Trip
import com.example.tourismgraduationproject.R
import com.navigation.CartScreen
import com.navigation.CartScreen2
import com.ui.feature.home.util.AnimationUtils


@Composable
fun ProfileHeader(
	navController: NavController,
) {
	AnimatedVisibility(
		visible = true,
		enter = slideInVertically { -it } + fadeIn(),
		exit = slideOutVertically { -it } + fadeOut()
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp, vertical = 16.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.align(Alignment.CenterStart)
			) {
				Image(
					painter = painterResource(id = R.drawable.ic_profile),
					contentDescription = null,
					modifier = Modifier.size(48.dp)
				)
				Spacer(modifier = Modifier.width(8.dp))
				Column {
					Text(
						color = Color(0xFF050505),
						text = "Welcome ! ",
						style = MaterialTheme.typography.bodySmall
					)
					Text(
						color = Color(0xFF050505),
						text = "Abdo Mohamed",
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.SemiBold
					)
				}
			}
			Row(
				modifier = Modifier.align(Alignment.CenterEnd)
			) {
				Image(
					painter = painterResource(id = R.drawable.notificatino),
					contentDescription = null,
					modifier = Modifier
						.size(48.dp)
						.clip(CircleShape)
						.background(Color(0xFFFFFFFF).copy(alpha = 0.3f))
						.padding(8.dp),
					contentScale = ContentScale.Inside
				)
				Spacer(Modifier.size(8.dp))
				Image(
					painter = painterResource(id = R.drawable.ic_cart),
					contentDescription = null,
					modifier = Modifier
						.size(48.dp)
						.clip(CircleShape)
						.background(Color(0xFFFFFFFF).copy(alpha = 0.3f))
						.padding(8.dp)
						.clickable {
							navController.navigate(CartScreen2)
						},
					contentScale = ContentScale.Inside
				)
			}

		}
	}
}

@Composable
fun SearchBar(
	searchedValue: String,
	onTextChanged: (String) -> Unit,
) {
	AnimatedVisibility(
		visible = true,
		enter = slideInHorizontally { -it } + fadeIn(),
		exit = slideOutHorizontally { -it } + fadeOut()
	) {
		TextField(
			value = searchedValue,
			onValueChange = onTextChanged,
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			shape = RoundedCornerShape(32.dp),
			leadingIcon = {
				Image(
					painter = painterResource(id = R.drawable.ic_search),
					contentDescription = null,
					modifier = Modifier.size(24.dp)
				)
			},
			colors = TextFieldDefaults.colors(
				focusedIndicatorColor = Color.Transparent,
				unfocusedIndicatorColor = Color.Transparent,
				focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
				unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
			),
			placeholder = {
				Text(
					color = Color(0xFF000000),
					text = "Search for Trip",
					style = MaterialTheme.typography.bodySmall
				)
			})
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryChips(
	categories: List<String>,
	icons: List<Int>, // List of icons for each category
) {
	val staggeredDelay = 100 // milliseconds between each chip's animation
	LazyRow(
		modifier = Modifier.animateContentSize()
	) {
		itemsIndexed(categories) { index, category ->
		AnimatedVisibility(
			visible = true,
			enter = fadeIn(animationSpec = tween(delayMillis = index * 100)) +
					scaleIn(animationSpec = tween(delayMillis = index * 100)),
			exit = fadeOut() + scaleOut(),
			modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
		) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Image(
						painter = painterResource(
							id = icons.getOrNull(index) ?: R.drawable.ic_launcher_background
						),
						contentDescription = category,
						modifier = Modifier.size(32.dp)
					)
					Text(
						text = category.replaceFirstChar { it.uppercase() },
						style = MaterialTheme.typography.bodyMedium,
						fontWeight = FontWeight.SemiBold,
						color = Color.Black,
						modifier = Modifier
							.padding(horizontal = 8.dp)
							.padding(8.dp)
					)

				}
			}
		}
	}
}

@Composable
fun SectionTitle(
	title: String,
	actionText: String,
	onClick: () -> Unit,
) {
	AnimatedVisibility(
		visible = true,
		enter = slideInVertically { it } + fadeIn(),
		exit = slideOutVertically { it } + fadeOut()
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleLarge,
				color = Color(0xFF000000)

			)
			Text(
				fontSize = 18.sp,
				text = actionText,
				modifier = Modifier.clickable { onClick() },
				style = MaterialTheme.typography.titleMedium,
				color = Color(0xFF000000)
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DestinationListRow(
	trips: List<Trip>,
	navController: NavController,
) {
	val lazyListState = rememberLazyListState()
	LazyRow(
		modifier = Modifier.fillMaxWidth(),
		state = lazyListState,
		contentPadding = PaddingValues(horizontal = 8.dp)
	) {
		itemsIndexed(trips, key = { _, it -> it.id }) { index, trip ->
			AnimatedVisibility(
				visible = true,
				enter = AnimationUtils.staggeredScaleEnter(index),
				modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
			) {
				DestinationCard(
					trip = trip,
					onClick = {
						navController.navigate("details_screen/${trip.id}")
					},
					modifier = Modifier.padding(vertical = 16.dp)
				)
			}
		}
	}
}
@Composable
fun DestinationListColumn(
	trips: List<Trip>,
	navController: NavController,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.animateContentSize()
	) {
		trips.forEachIndexed { index, trip ->
			key(trip.id) { // Important for animations
				AnimatedVisibility(
					visible = true,
					enter = fadeIn() + slideInVertically { it / 2 },
					modifier = Modifier // Remove animateItemPlacement here
				) {
					PopularDestinationCard(
						trip = trip,
						onClick = { navController.navigate("details_screen/${trip.id}") },
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp, vertical = 8.dp)
							.height(180.dp)
					)
				}
			}
		}
	}
}
@Composable
fun PopularDestinationCard(
	trip: Trip,
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
				model = trip.imageUrl,
				contentDescription = trip.name,
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
						text = trip.name,
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
						text = "$${trip.price}",
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
@Composable
fun DestinationCard(
	trip: Trip,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	// Define larger dimensions
	val cardWidth = 360.dp
	val imageHeight = 280.dp
	val detailsPaddingTop = 290.dp

	Box(
		modifier = modifier
			.width(cardWidth)
			.height(380.dp) // Total height including image and details
			.padding( 8.dp)
	) {
		// Image Card - Made larger
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.height(imageHeight)
				.shadow(8.dp, RoundedCornerShape(16.dp))
				.clickable { onClick() },
			shape = RoundedCornerShape(16.dp),
			colors = CardDefaults.cardColors(containerColor = Color.White)
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				AsyncImage(
					model = trip.imageUrl,
					contentDescription = trip.name,
					modifier = Modifier.fillMaxSize(),
					contentScale = ContentScale.Crop
				)

				// Favorite Icon - Made slightly larger
				Box(
					modifier = Modifier
						.clip(CircleShape)
						.align(Alignment.TopEnd)
						.padding(16.dp),
					contentAlignment = Alignment.TopEnd
				) {
					var onFavouriteClick by rememberSaveable() { mutableStateOf(false) }
					val icon = if (onFavouriteClick) {
						Icons.Filled.Favorite
					} else {
						Icons.Filled.FavoriteBorder
					}
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = if (onFavouriteClick) Color.Red else Color.White,
						modifier = Modifier
							.size(40.dp)
							.clip(CircleShape)
							.background(Color.Black.copy(alpha = 0.4f))
							.clickable {
								onFavouriteClick = !onFavouriteClick
							}
							.padding(8.dp)
					)
				}
			}
		}

		// Details Section Below Image - Adjusted positioning
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = detailsPaddingTop, start = 12.dp, end = 12.dp)
				.background(
					brush = Brush.verticalGradient(
						colors = listOf(
							Color.Transparent,
							Color.LightGray.copy(alpha = 0.3f)
						),
						startY = 0f,
						endY = 100f
					),
					shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
				)
				.padding(8.dp)
		) {
			// First row: Name and Price - Increased font sizes
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = trip.name,
					style = MaterialTheme.typography.titleLarge.copy(
						fontWeight = FontWeight.Bold,
						shadow = Shadow(
							color = Color.Black.copy(alpha = 0.2f),
							blurRadius = 2f
						)
					),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier.weight(1f)
				)

				Spacer(modifier = Modifier.width(8.dp))

				Text(
					text = "$${trip.price}",
					style = MaterialTheme.typography.titleLarge.copy(
						fontWeight = FontWeight.Bold,
						color = Color(0xFF4CAF50)
					)
				)
			}

			Spacer(modifier = Modifier.height(8.dp))

			// Second row: Location and Rating - Increased font sizes
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
						modifier = Modifier.size(18.dp)
					)
					Text(
						text = "${trip.rating} (${trip.reviews})",
						style = MaterialTheme.typography.bodyLarge,
						modifier = Modifier.padding(start = 4.dp)
					)
				}

				Spacer(modifier = Modifier.width(8.dp))

				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						painter = painterResource(id = R.drawable.location),
						contentDescription = "Distance",
						tint = Color.Gray,
						modifier = Modifier.size(18.dp)
					)
					Text(
						text = "${trip.distance} km",
						style = MaterialTheme.typography.bodyLarge,
						modifier = Modifier.padding(start = 4.dp)
					)
				}
			}
		}
	}
}

