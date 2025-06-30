@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.ui.feature.home.detailsHomeScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.tourismgraduationproject.R
import com.navigation.Routes
import com.ui.feature.packages.PackageListUiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
	navController: NavController,
	tripId: String,
	detailsScreenViewModel: DetailsScreenViewModel = koinViewModel(),
) {
	val currentTrip by detailsScreenViewModel.currentTrip.collectAsState()
	val currentDetailsScreenIndex by detailsScreenViewModel.currentDetailsScreenIndex.collectAsState()
	val hotels by detailsScreenViewModel.hotelsList.collectAsState()
	val firstHotel = hotels.firstOrNull()
	val uiState by detailsScreenViewModel.uiState.collectAsState()
	val pagerState = rememberPagerState(pageCount = { currentTrip?.detailsScreenData?.size ?: 0 })
	val previousPage = remember { mutableIntStateOf(pagerState.currentPage
	) }
	var showVideoDialog by remember { mutableStateOf(false) }
	val userMessage by detailsScreenViewModel.userMessage.collectAsState() // NEW
	val context = LocalContext.current // To show Toast
	val scope = rememberCoroutineScope() // To launch coroutines for button click
	// For getting userId (assuming TourismSession is injectable via Koin)


//	val reviews by detailsScreenViewModel.reviewsList.collectAsState()
//	val firstReview = currentTrip!!.detailsScreenData[currentDetailsScreenIndex].reviewList

	LaunchedEffect(Unit) {
		detailsScreenViewModel.loadHotels()
	}

	LaunchedEffect(tripId) {
		detailsScreenViewModel.loadTrip(tripId)
	}
	// Observe user messages (e.g., for Toast)
	LaunchedEffect(userMessage) {
		userMessage?.let {
			Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
		}
	}

	LaunchedEffect(pagerState.currentPage) {
		if (pagerState.currentPage > previousPage.intValue) {

			detailsScreenViewModel.onNextClicked()
		} else if (pagerState.currentPage < previousPage.intValue) {

			detailsScreenViewModel.onBackClicked()
		}
		previousPage.intValue = pagerState.currentPage
	}
	Scaffold(
		modifier = Modifier.fillMaxSize() // Ensure Scaffold fills the entire screen
	) { padding ->
		when (uiState) {
			is DetailsScreenUiState.Loading -> {
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

			is DetailsScreenUiState.Error -> {
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
							onClick = { },
							modifier = Modifier.padding(top = 8.dp)
						) {
							Text("Retry")
						}
					}
				}
			}

			is DetailsScreenUiState.Success -> {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(
							top = padding.calculateTopPadding(),
							bottom = 0.dp
						)
						.verticalScroll(rememberScrollState())
				) {
					HorizontalPager(state = pagerState) { page ->
						Card(
							Modifier
								.fillMaxWidth()
								.graphicsLayer {
									val pageOffset = (
											(pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
											).absoluteValue

									alpha = lerp(
										start = 0.5f,
										stop = 1f,
										fraction = 1f - pageOffset.coerceIn(0f, 1f)
									)
								}
						) {
							Card(
								modifier = Modifier
									.fillMaxWidth()
									.height(400.dp),
							) {
								Box(modifier = Modifier.fillMaxSize()) {
									AsyncImage(
										model = currentTrip!!.detailsScreenData[currentDetailsScreenIndex].image,
										contentDescription = "Image",
										modifier = Modifier.fillMaxSize(),
										contentScale = ContentScale.Crop
									)

									// Top-left Back Arrow
									IconButton(
										onClick = { navController.popBackStack() },
										modifier = Modifier
											.align(Alignment.TopStart)
											.padding(12.dp)
											.background(Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(50))
											.size(64.dp)
									) {
										Icon(
											imageVector = Icons.AutoMirrored.Filled.ArrowBack, // mirrored = back arrow in LTR
											contentDescription = "Back",
											tint = Color.Black
										)
									}
									// Page number at bottom center
									Box(
										modifier = Modifier
											.align(Alignment.BottomCenter)
											.padding(bottom = 8.dp)
											.background(
												Color.LightGray.copy(alpha = 0.5f),
												RoundedCornerShape(16.dp)
											)
											.padding(horizontal = 12.dp, vertical = 4.dp)
									) {
										Text(
											text = "${pagerState.currentPage + 1} of ${pagerState.pageCount}",
											fontSize = 14.sp,
											fontWeight = FontWeight.Bold,
											color = Color.Black,
										)
									}
								}

							}
						}
					}
					Spacer(modifier = Modifier.height(24.dp))
					// images related to each place of trip
					LazyRow(
						modifier = Modifier.fillMaxWidth().padding(start =16.dp)
					) {
						val image = currentTrip!!.detailsScreenData[currentDetailsScreenIndex].listOfImages
						items(image ?: emptyList()) { hotelImage ->
							Card(
								modifier = Modifier
									.width(180.dp)
									.height(130.dp)
									.padding(end = 16.dp)
									.clickable { },
								shape = RoundedCornerShape(16.dp),
								colors = CardDefaults.cardColors(containerColor = Color.White)
							) {
								Box(modifier = Modifier.fillMaxSize()) {
									AsyncImage(
										model = hotelImage,
										contentDescription = "Image",
										modifier = Modifier.fillMaxSize(),
										contentScale = ContentScale.Crop
									)
								}
							}
						}
					}
					Spacer(modifier = Modifier.height(16.dp))
					// Show current page name
					Column(
						modifier = Modifier.padding(16.dp),
						horizontalAlignment = Alignment.Start,
						verticalArrangement = Arrangement.Top
					) {
						Text(
							color = Color(0xFF000000),
							text = currentTrip!!.detailsScreenData[currentDetailsScreenIndex].title.toString(),
							fontSize = 28.sp,
							fontWeight = FontWeight.Bold,
						)
						Text(
							text = "Cairo, Egypt",
							fontSize = 18.sp,
							color = Color(0xFF474444)
						)
						Spacer(modifier = Modifier.height(8.dp))
						Text(
							text = currentTrip!!.detailsScreenData[currentDetailsScreenIndex].description.toString(),
							fontSize = 14.sp,
							color = Color(0xFF030303).copy(.7f)
						)
						Spacer(modifier = Modifier.height(16.dp))
						Card(
							modifier = Modifier
								.padding(start = 8.dp, end = 8.dp)
								.shadow(3.dp, shape = RoundedCornerShape(16.dp))
								.border(
									width = 1.dp,
									color = Color.Gray.copy(alpha = 0.5f),
									shape = RoundedCornerShape(16.dp)
								),
							colors = CardDefaults.cardColors(Color.White),
							shape = RoundedCornerShape(16.dp),
						) {
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.clip(RoundedCornerShape(16.dp))
									.background(Color.White)
									.padding(16.dp),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceEvenly // more balanced layout
							) {

								// Opening Time
								Column(
									modifier = Modifier.padding(end = 16.dp),
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									Row(verticalAlignment = Alignment.CenterVertically) {
										Icon(
											modifier = Modifier
												.size(24.dp)
												.padding(end = 4.dp),
											painter = painterResource(id = R.drawable.time),
											tint = Color(0xFF07ABF3),
											contentDescription = "Time"
										)
										Text(text = "Opens at", fontWeight = FontWeight.Bold)
									}
									Text(
										text = "10:00am",
										fontSize = 20.sp,
										fontWeight = FontWeight.Bold
									)
								}
								VerticalDivider(modifier = Modifier.height(24.dp))
								// Ratings
								Column(
									modifier = Modifier.padding(horizontal = 16.dp),
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									Row(verticalAlignment = Alignment.CenterVertically) {
										Icon(
											modifier = Modifier
												.size(24.dp)
												.padding(end = 4.dp),
											painter = painterResource(id = R.drawable.star),
											tint = Color(0xFFFFD700),
											contentDescription = "Rating Icon"
										)
										Text(
											text = "Ratings",
											fontSize = 16.sp,
											fontWeight = FontWeight.Bold
										)
									}
									Text(
										text = "4.5",
										fontSize = 20.sp,
										fontWeight = FontWeight.Bold
									)
								}
								VerticalDivider(modifier = Modifier.height(24.dp))
								// Loved
								Column(
									modifier = Modifier.padding(horizontal = 16.dp),
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									Row(verticalAlignment = Alignment.CenterVertically) {
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
										Text(
											text = "Play Video",
											fontSize = 16.sp,
											color = Color(
												0xFF4FC3F7
											),
											fontWeight = FontWeight.Bold,
											textDecoration = TextDecoration.Underline
										)

									}
									Text(
										text = "678",
										fontSize = 20.sp,
										fontWeight = FontWeight.Bold
									)
								}
							}
						}
						Spacer(modifier = Modifier.height(16.dp))
						GuestRatingsCard()
						Spacer(modifier = Modifier.height(16.dp))
						// Place review comments
						LazyRow(
							modifier = Modifier.fillMaxWidth(),
						) {
							val currentReviews = currentTrip!!.detailsScreenData[currentDetailsScreenIndex].reviewList
							items(currentReviews ?: emptyList()) { review ->
								ReviewCard(reviewee = review)
							}
						}
						OutlinedButton(
							onClick = { /* Handle click */ },
							modifier = Modifier
								.padding(16.dp)
								.fillMaxWidth(),
							colors = ButtonDefaults.outlinedButtonColors(
								contentColor = Color.Gray.copy(.8f)
							),
							border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
							shape = RoundedCornerShape(8.dp)
						) {
							Text(
								text = "See all 54 reviews",
								fontSize = 18.sp,
								fontWeight = FontWeight.Medium
							)
							Spacer(Modifier.width(8.dp))
							Icon(
								imageVector = Icons.AutoMirrored.Filled.ArrowForward,
								contentDescription = null,
								modifier = Modifier.size(24.dp)
							)
						}

						// Booked Hotel
						Column(
							modifier = Modifier.padding(8.dp),
							verticalArrangement = Arrangement.spacedBy(8.dp)
						) {
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Text(
									text = "Booked Hotel",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold,
									color = Color(0xFF0E0D0D),
									modifier = Modifier.weight(1f)
								)
								Text(
									text = "Show Hotel Details",
									fontSize = 14.sp,
									color = Color.Gray,
									fontWeight = FontWeight.SemiBold,
									modifier = Modifier.clickable {
										firstHotel?.let {
											navController.navigate(Routes.getHotelDetailsRoute(it.id.toString()))										}
									}
								)
							}
							Spacer(modifier = Modifier.height(8.dp))
							// Replace the existing LazyRow section with this:
							LazyRow(
								modifier = Modifier.fillMaxWidth()
							) {
								item {
									currentTrip?.hotels?.let { hotel ->
										Card(
											modifier = Modifier
												.fillParentMaxWidth()
												.height(250.dp)
												.padding(end = 16.dp)
												.clickable {
													try {
														// Safe navigation with null checks
														if (hotel.id.toString().isNotBlank()) {
															navController.navigate(
																Routes.getHotelDetailsRoute(hotel.id.toString())
															) {
																// Optional navigation options
																launchSingleTop = true
																restoreState = true
															}
														} else {
															Log.e("Navigation", "Hotel ID is empty")
														}
													} catch (e: Exception) {
														Log.e("Navigation", "Failed to navigate: ${e.message}")
													}
												},
											shape = RoundedCornerShape(16.dp),
											colors = CardDefaults.cardColors(containerColor = Color.White)
										) {
											Box(modifier = Modifier.fillMaxSize()) {
												AsyncImage(
													model = hotel.imageUrl,
													contentDescription = "Hotel Image",
													modifier = Modifier.fillMaxSize(),
													contentScale = ContentScale.Crop
												)

												// Optional: Add a visual indicator that this is clickable
												Box(
													modifier = Modifier
														.align(Alignment.BottomStart)
														.padding(16.dp)
														.background(
															color = Color.Black.copy(alpha = 0.5f),
															shape = RoundedCornerShape(8.dp)
														)
														.padding(8.dp)
												) {
													Text(
														text = "View Hotel Details",
														color = Color.White,
														fontSize = 14.sp
													)
												}
											}
										}
									} ?: run {
										// Fallback UI if hotel is null
										Box(
											modifier = Modifier
												.fillParentMaxWidth()
												.height(250.dp)
												.background(Color.LightGray),
											contentAlignment = Alignment.Center
										) {
											Text("No hotel information available")
										}
									}
								}
							}
							Button(
								onClick = {
									scope.launch {
										detailsScreenViewModel.addCurrentTripToCart()
									}							},
								modifier = Modifier.fillMaxWidth(),
								colors = ButtonDefaults.buttonColors(
									containerColor = Color(0xFF07ABF3),
									contentColor = Color.White
								),
								shape = RoundedCornerShape(8.dp)
							) {
								Text(text = "Book Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
							}
						}
					}
				}
			}
		}
	}
}



