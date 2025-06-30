package com.ui.feature.places.detailsOfPOI

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil3.compose.AsyncImage
import com.example.tourismgraduationproject.R
import com.ui.feature.home.detailsHomeScreen.ReviewCard
import com.ui.feature.home.detailsHomeScreen.VideoDialog
import com.ui.feature.packages.PackageListUiState
import org.koin.androidx.compose.koinViewModel
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsOfPlacesScreen(
	navController: NavController,
	placeId: String,
	destinationViewModel: DetailsOfPlaceScreenViewModel = koinViewModel(),
) {
	val currentPlace2 by destinationViewModel.currentPlace2.collectAsState()
	val uiState by destinationViewModel.uiState.collectAsState()

	var showImageDialog by remember { mutableStateOf(false) }
	var showVideoDialog by remember { mutableStateOf(false) }


	LaunchedEffect(placeId) {
		destinationViewModel.loadPlaceDetails(placeId)
	}
	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					currentPlace2?.let {
						Text(
							it.name,
							style = MaterialTheme.typography.headlineMedium.copy(
								fontWeight = FontWeight.Bold
							)

						)
					}
				},
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
			)

		}
	) { padding ->
		when (uiState) {
			is DetailsOfPlaceScreenUiState.Loading -> {
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

			is DetailsOfPlaceScreenUiState.Error -> {
				val errorMessage = (uiState as DetailsOfPlaceScreenUiState.Error).message
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
							text = errorMessage,
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.error,
							modifier = Modifier.padding(top = 16.dp)
						)
						Button(
							onClick = { },
							modifier = Modifier.padding(top = 8.dp)
						) {
							Text("Retry")
						}
					}
				}
			}

			is DetailsOfPlaceScreenUiState.Success -> {
				currentPlace2?.let { place ->
					Column(
						modifier = Modifier
							.background(color = Color.White)
							.fillMaxSize()
							.padding(top = padding.calculateTopPadding(), bottom = 0.dp)
							.verticalScroll(rememberScrollState())
					) {
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.height(300.dp)
								.shadow(8.dp),
							shape = RectangleShape,
							colors = CardDefaults.cardColors(containerColor = Color.Transparent)
						) {
							Box(modifier = Modifier.fillMaxSize()) {
								AsyncImage(
									model = place.imageUrl,
									contentDescription = place.name,
									modifier = Modifier.fillMaxSize(),
									contentScale = ContentScale.Crop
								)
							}
						}
						Spacer(modifier = Modifier.height(16.dp))
						Row(
							modifier = Modifier.padding(start = 8.dp, end = 8.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(
								color = Color(0xFF000000),
								modifier = Modifier.weight(1f),
								text = place.name,
								fontSize = 22.sp,
								fontWeight = FontWeight.Bold,
							)
							Row(verticalAlignment = Alignment.CenterVertically) {
								Text(
									text = "$150",
									fontSize = 22.sp,
									fontWeight = FontWeight.Bold,
									color = Color(0xFF64B5F6),
								)
							}
						}
						Spacer(modifier = Modifier.height(8.dp))
						Row(
							modifier = Modifier.padding(start = 8.dp, end = 8.dp),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween,
						) {
							Icon(
								painter = painterResource(id = R.drawable.star),
								contentDescription = "Rating",
								tint = Color(0xFFFFD700),
								modifier = Modifier.size(14.dp)
							)
							Text(
								text = "4.8 Rating",
								fontSize = 14.sp,
								color = Color.Black,
								modifier = Modifier.padding(start = 4.dp)
							)
						}
						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))
						Column(
							modifier = Modifier.padding(8.dp)
						) {
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								IconButton(
									onClick = { showVideoDialog = true },
									modifier = Modifier.size(24.dp)
								) {
									Icon(
										Icons.Filled.PlayArrow,
										contentDescription = "Play",
										tint = Color(0xFF000000),
									)
								}
								if (showVideoDialog) {
									VideoDialog(onDismiss = { showVideoDialog = false })
								}
								Spacer(modifier = Modifier.width(4.dp))
								Text(
									text = "Click to show titre video",
									fontSize = 16.sp,
									color = Color(
										0xFF4FC3F7
									),
									textDecoration = TextDecoration.Underline
								)
							}
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								IconButton(
									onClick = { showImageDialog = true },
									modifier = Modifier.size(24.dp)
								) {
									Icon(
										Icons.AutoMirrored.Filled.List,
										contentDescription = "Show all Photos",
										tint = Color(0xFF000000),
									)
								}
								if (showImageDialog) {
									ImageDialog(
										imageUrls = listOf(
											"https://images.unsplash.com/photo-1553913861-c0fddf2619ee",
											"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
											"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
										),
										onDismiss = { showImageDialog = true }
									)
								}
								Spacer(modifier = Modifier.width(4.dp))
								Text(
									text = "Click to show all Photos",
									fontSize = 16.sp,
									color = Color(
										0xFF4FC3F7
									),
									textDecoration = TextDecoration.Underline
								)
							}
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Icon(
									painter = painterResource(id = R.drawable.location),
									contentDescription = "Distance",
									tint = Color(0xFF000000),
									modifier = Modifier.size(24.dp)
								)
								Spacer(modifier = Modifier.width(4.dp))
								Text(
									text = "Open Google Maps", fontSize = 16.sp, color = Color(
										0xFF4FC3F7
									), textDecoration = TextDecoration.Underline

								)
							}
							Spacer(modifier = Modifier.height(8.dp))
							Row {
								Icon(
									painter = painterResource(id = R.drawable.time),
									contentDescription = "Distance",
									tint = Color(0xFF000000),
									modifier = Modifier.size(24.dp)
								)
								Spacer(modifier = Modifier.width(4.dp))
								Text(
									text = "08 am - 06 pm", fontSize = 16.sp,
									color = Color(0xFF0A0A0A)
								)
							}
							Spacer(modifier = Modifier.height(8.dp))
							Row {
								Icon(
									painter = painterResource(id = R.drawable.calendar_month),
									contentDescription = "Distance",
									tint = Color(0xFF000000),
									modifier = Modifier.size(24.dp)
								)
								Spacer(modifier = Modifier.width(4.dp))
								Text(
									text = "Everyday", fontSize = 16.sp,
									color = Color(0xFF0E0E0E)
								)
							}
						}
						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))
						Card(
							modifier = Modifier.padding(start = 8.dp, end = 8.dp),
							colors = CardDefaults.cardColors(Color.Transparent),
						) {
							Text(
								text = "Description",
								fontSize = 18.sp,
								fontWeight = FontWeight.Bold,
								color = Color(0xFF0E0D0D)
							)
							Spacer(modifier = Modifier.height(8.dp))
							Text(
								text = place.description,
								fontSize = 14.sp,
								color = Color(0xFF030303).copy(.7f)
							)

						}
						Spacer(modifier = Modifier.height(8.dp))
						HorizontalDivider()
						Spacer(modifier = Modifier.height(8.dp))
						Column(
							modifier = Modifier.padding(8.dp),
							verticalArrangement = Arrangement.spacedBy(8.dp)
						) {
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Text(
									text = "Reviews",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold,
									color = Color(0xFF0E0D0D)
								)
								Text(
									text = "(230)",
									fontSize = 12.sp,
									fontWeight = FontWeight.Bold,
									color = Color.Gray,
									modifier = Modifier.weight(1f)
								)
								Text(text = "See All", fontSize = 12.sp, color = Color.Gray)
							}
							Spacer(modifier = Modifier.height(8.dp))
							LazyRow(
								modifier = Modifier.fillMaxWidth(),
							) {
								val reviews = currentPlace2!!.placeData.reviewList
								items(reviews ?: emptyList()) { review ->
									ReviewCard(reviewee = review)
								}
							}
						}
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(8.dp),
							contentAlignment = Alignment.BottomCenter
						) {
							Button(
								onClick = {
									Log.d("CART_UI_EVENT", "Add to Cart button clicked for place ID: ${place.id}")
									destinationViewModel.addCurrentPlaceToCart()
									navController.navigateUp()
								},
								modifier = Modifier
									.fillMaxWidth()
									.height(50.dp),
								colors = ButtonDefaults.buttonColors(Color(0xFF64B5F6)),
								shape = RoundedCornerShape(12.dp)
							) {
								Text(text = "Add To Customize Package", fontSize = 16.sp, color = Color.White)
							}
						}
					}
				}
			}
		}
	}
}

@Composable
fun ImageDialog(imageUrls: List<String>, onDismiss: () -> Unit) {
	var currentIndex by remember { mutableStateOf(0) }
	val pagerState = rememberScrollState()
	Dialog(onDismissRequest = onDismiss) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(400.dp)
				.padding(8.dp)
				.background(Color.Black, shape = RoundedCornerShape(12.dp))
		) {
			Column(
				modifier = Modifier.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				// Close Button
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp),
					contentAlignment = Alignment.TopEnd
				) {
					IconButton(onClick = onDismiss) {
						Icon(
							imageVector = Icons.Filled.Close,
							contentDescription = "Close",
							tint = Color.White
						)
					}
				}

				// Image Pager (Horizontal Scroll)
				LazyRow(
					modifier = Modifier
						.weight(1f)
						.fillMaxWidth(),
					state = rememberLazyListState()
				) {
					itemsIndexed(imageUrls) { index, imageUrl ->
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.height(300.dp),
							contentAlignment = Alignment.Center
						) {
							Image(
								painter = rememberAsyncImagePainter(imageUrl),
								contentDescription = "Image",
								modifier = Modifier
									.fillMaxWidth()
									.height(300.dp)
									.clip(RoundedCornerShape(12.dp)),
								contentScale = ContentScale.Crop
							)
						}
						currentIndex = index
					}
				}

				Spacer(modifier = Modifier.height(8.dp))

				// Image Counter
				Text(
					text = "${currentIndex + 1} of ${imageUrls.size}",
					fontSize = 14.sp,
					fontWeight = FontWeight.Bold,
					color = Color.White,
					modifier = Modifier.align(Alignment.CenterHorizontally)
				)

				Spacer(modifier = Modifier.height(16.dp))
			}
		}
	}
}
