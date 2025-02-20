package com.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.domain.model.Product
import com.example.tourismgraduationproject.R
import com.ui.feature.splash.DetailsScreenData


@Composable
fun ProfileHeader(
	onCartClicked: () -> Unit,
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
					color = Color(0xFFE65100),
					text = "Hello,",
					style = MaterialTheme.typography.bodySmall
				)
				Text(
					color = Color(0xFFE65100),
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
					.background(Color(0xFFE65100).copy(alpha = 0.3f))
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
					.background(Color(0xFFE65100).copy(alpha = 0.3f))
					.padding(8.dp)
					.clickable {
						onCartClicked()
					},
				contentScale = ContentScale.Inside
			)
		}

	}
}

@Composable
fun SearchBar(
	searchedValue: String,
	onTextChanged: (String) -> Unit,
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
				color = Color(0xFFE65100),
				text = "Search for products",
				style = MaterialTheme.typography.bodySmall
			)
		})

}

@Composable
fun HomeProductRow(
	products: List<Product>,
	title: String,
	onClick: (Product) -> Unit,
) {
	Column {
		Box(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth()
		) {
			Text(
				color = Color(0xFFE65100),
				text = title,
				style = MaterialTheme.typography.titleMedium,
				modifier = Modifier.align(
					Alignment.CenterStart
				),
				fontWeight = FontWeight.SemiBold
			)
			Text(
				text = "View all",
				style = MaterialTheme.typography.bodyMedium,
				color = Color(0xFFE65100),
				modifier = Modifier.align(
					Alignment.CenterEnd
				)
			)
		}
		Spacer(modifier = Modifier.size(8.dp))
		LazyRow {
			items(products) { product ->
				val isVisible = remember {
					mutableStateOf(false)
				}
				LaunchedEffect(true) {
					isVisible.value = true
				}
				androidx.compose.animation.AnimatedVisibility(
					visible = isVisible.value, enter = fadeIn() + expandVertically()
				) {
					ProductItem(product = product, onClick)
				}
			}
		}
	}
}


@Composable
fun ProductItem(
	product: Product,
	onClick: (Product) -> Unit,
) {
	Card(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.size(width = 220.dp, height = 180.dp)
			.clickable { onClick(product) },
		shape = RoundedCornerShape(16.dp),
		colors = CardDefaults.cardColors(containerColor = Color.Transparent)
	) {
		Box(
			modifier = Modifier.fillMaxSize()
		) {
			AsyncImage(
				model = product.image,
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize()
			)
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomStart)
					.background(
						brush = Brush.verticalGradient(
							colors = listOf(Color.Transparent, Color(0xFFF5E1C0).copy(alpha = 0.1f))
						)
					)
					.padding(8.dp)
			) {
				Column {
					// Title and Rating Row
					Row(verticalAlignment = Alignment.CenterVertically) {
						Text(
							color = Color(0xFFE65100),
							text = product.title,
							style = MaterialTheme.typography.bodyMedium,
							fontWeight = FontWeight.SemiBold,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
							modifier = Modifier.weight(1f) // Push rating to the right
						)
						Icon(
							imageVector = Icons.Default.Star,
							contentDescription = "Rating",
							tint = Color(0xFFFFD700), // Gold star
							modifier = Modifier.size(16.dp)
						)
						Spacer(modifier = Modifier.width(4.dp))
						Text(
							text = "4.5",
							style = MaterialTheme.typography.bodySmall,
							fontWeight = FontWeight.SemiBold,
							color = Color.White
						)
					}

					// Price
					Text(
						text = "$100",
						style = MaterialTheme.typography.bodySmall,
						color = Color(0xFFE65100).copy(alpha = 0.8f),
						fontWeight = FontWeight.SemiBold
					)

					// Location Row
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							imageVector = Icons.Default.LocationOn,
							contentDescription = "Location",
							tint = Color(0xFF757575), // Grey color
							modifier = Modifier.size(14.dp)
						)
						Spacer(modifier = Modifier.width(4.dp))
						Text(
							text = "Atwab",
							style = MaterialTheme.typography.bodySmall,
							color = Color(0xFF757575),
							maxLines = 1,
							overflow = TextOverflow.Ellipsis
						)
					}
				}
			}
		}
	}
}

@Composable
fun CategoryChips(
	categories: List<String>,
	selectedCategory: String?,
	onCategorySelected: (String?) -> Unit,
) {
	LazyRow {
		items(categories, key = { it }) { category ->
			val isVisible = remember {
				mutableStateOf(false)
			}
			LaunchedEffect(true) {
				isVisible.value = true
			}
			AnimatedVisibility(
				visible = isVisible.value, enter = fadeIn() + expandVertically()
			) {
				Text(
					text = category.replaceFirstChar { it.uppercase() },
					style = MaterialTheme.typography.bodyMedium,
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.onPrimary,
					modifier = Modifier
						.padding(horizontal = 8.dp)
						.clip(RoundedCornerShape(8.dp))
						.background(Color(0xFFE65100))
						.padding(8.dp)
				)
			}
		}

	}
	Spacer(modifier = Modifier.size(16.dp))
}
