package com.ui.feature.details

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.tourismgraduationproject.R
import com.navigation.ConfirmationScreen
import com.ui.feature.packages.PackageListUiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
	navController: NavController,
	destinationViewModel: DetailsScreenViewModel = koinViewModel(),
) {
	var isExpanded by remember { mutableStateOf(false) }
	val currentIndex by destinationViewModel.currentIndex.collectAsState()
	val currentDetailsScreenData by destinationViewModel.currentDetailsScreen.collectAsState()
	val uiState by destinationViewModel.uiState.collectAsState()
	val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).background(color =Color(0xFFF5E1C0) ),
		topBar = {
			TopAppBar(
				title = {
					Text(
						"All Destinations Of Your Journey",
                        color = Color(0xFFE65100)

					)
				},
				navigationIcon = {
					IconButton(onClick = { navController.navigateUp() }) {
						Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
					}
				},
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Color(0xFFF5E1C0)
				),
				scrollBehavior = scrollBehavior
			)

		}
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
							text = "Loading destinations...",
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
				Box(
					modifier = Modifier
						.background(color = Color(0xFFF5E1C0))
						.padding(padding)
						.fillMaxSize()
				) {
					Column(
						modifier = Modifier
							.fillMaxSize()
							.verticalScroll(rememberScrollState())
							.padding(bottom = 80.dp) // Space for the button
					) {
						Card(
							modifier = Modifier
								.fillMaxWidth()
								.height(380.dp)
								.shadow(
									8.dp,
									RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
								)
								.clickable { },
							shape = RoundedCornerShape(16.dp),
							colors = CardDefaults.cardColors(containerColor = Color.Transparent)
						) {
							Box(modifier = Modifier.fillMaxSize()) {
								AsyncImage(
									model = currentDetailsScreenData.image,
									contentDescription = "Bali Image",
									modifier = Modifier.fillMaxSize(),
									contentScale = ContentScale.Crop
								)

								IconButton(
									onClick = { },
									modifier = Modifier
										.align(Alignment.TopEnd)
										.padding(16.dp)
								) {
									Icon(
										Icons.Filled.PlayArrow,
										contentDescription = "Favorite",
										tint = Color.White
									)
								}
								IconButton(
									onClick = { destinationViewModel.onBackClicked() },
									modifier = Modifier
										.align(Alignment.CenterStart)
										.padding(16.dp)
								) {
									Icon(
										Icons.AutoMirrored.Filled.ArrowBack,
										contentDescription = "Arrow Forward",
										tint = Color.White
									)
								}
								IconButton(
									onClick = { destinationViewModel.onNextClicked() },
									modifier = Modifier
										.align(Alignment.CenterEnd)
										.padding(16.dp)
								) {
									Icon(
										Icons.AutoMirrored.Filled.ArrowForward,
										contentDescription = "Arrow Back",
										tint = Color.White
									)
								}

								// Indicators at the bottom
								Row(
									modifier = Modifier
										.align(Alignment.BottomCenter)
										.padding(bottom = 8.dp),
									horizontalArrangement = Arrangement.Center
								) {
									repeat(3) { index ->
										Box(
											modifier = Modifier
												.padding(horizontal = 4.dp)
												.size(if (currentIndex == index) 10.dp else 8.dp)
												.clip(CircleShape)
												.background(
													if (currentIndex == index)
														Color(0xFFE65100)
													else
														Color.White.copy(alpha = 0.5f)
												)
										)
									}
								}
							}
						}


						Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Text(
									color = Color(0xFFE65100),
									modifier = Modifier.weight(1f),
									text = currentDetailsScreenData.title,
									fontSize = 22.sp,
									fontWeight = FontWeight.Bold,
								)
								Row(verticalAlignment = Alignment.CenterVertically) {
									Text(
										text = "$150",
										fontSize = 22.sp,
										fontWeight = FontWeight.Bold,
										color = Color(0xFF825D46),
									)
									Text(
										color = Color(0xFFE65100).copy(.7f),
										text = "/per day",
										fontSize = 14.sp,
										modifier = Modifier.padding(start = 4.dp)
									)
								}
							}
							Row(
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Text(
									text = "Asia, Indonesia | 5697 km",
									fontSize = 14.sp,
									color = Color(0xFFE65100).copy(.7f),
								)
								Spacer(modifier = Modifier.width(8.dp))
								Icon(
									painter = painterResource(id = R.drawable.location),
									contentDescription = "Distance",
									tint = Color(0xFFE65100),
									modifier = Modifier.size(14.dp)
								)
								Text(
									text = "35.5 km",
									fontSize = 14.sp,
									color = Color(0xFFE65100).copy(.7f)
								)
							}
							Spacer(modifier = Modifier.height(8.dp))
							Row(
								verticalAlignment = Alignment.CenterVertically
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
									color = Color.Blue,
									modifier = Modifier.padding(start = 4.dp)
								)
								Text(
									text = " (156 Reviews)",
									fontSize = 14.sp,
									color = Color.Blue,
									modifier = Modifier.padding(start = 4.dp)
								)
							}
							Spacer(modifier = Modifier.height(8.dp))
							Box(
								modifier = Modifier
									.fillMaxWidth()
									.clip(RoundedCornerShape(5.dp))
									.height(35.dp)
									.background(Color.Transparent), // Set background color here
							) {
								Row(
									horizontalArrangement = Arrangement.SpaceBetween,
									verticalAlignment = Alignment.CenterVertically
								) {
									Icon(
										painter = painterResource(id = R.drawable.time),
										contentDescription = "Rating",
										tint = Color(0xFFE65100),
										modifier = Modifier.size(24.dp)
									)
									Spacer(modifier = Modifier.width(4.dp))
									Text(
										text = "1 week duration",
										fontSize = 14.sp,
										color = Color(0xFFE65100)
									)
									Spacer(modifier = Modifier.width(16.dp))
									Text(text = "|", fontSize = 14.sp, color = Color(0xFFE65100))
									Spacer(modifier = Modifier.width(16.dp))
									Icon(
										painter = painterResource(id = R.drawable.people),
										contentDescription = "Rating",
										tint = Color(0xFFE65100),
										modifier = Modifier.size(24.dp)
									)
									Text(
										text = "Max 15 people",
										fontSize = 14.sp,
										color = Color(0xFFE65100)
									)
								}
							}
							Spacer(modifier = Modifier.height(16.dp))
							Column(
								modifier = Modifier
									.fillMaxWidth()
									.padding(16.dp)
									.clickable { isExpanded = true } // Expand when clicked
							) {
								Text(
									color = Color(0xFFE65100),
									text = "Description",
									fontSize = 18.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(bottom = 8.dp)
								)

								Box(
									modifier = Modifier
										.fillMaxWidth()
										.clip(RoundedCornerShape(8.dp))
										.height(if (isExpanded) 200.dp else 100.dp) // Adjust initial height
										.padding(8.dp)
								) {
									val scrollState = rememberScrollState()

									Box(
										modifier = Modifier
											.fillMaxSize()
											.verticalScroll(scrollState)
									) {
										Text(
											text = currentDetailsScreenData.description,
											fontSize = 14.sp,
											color = Color(0xFFE65100).copy(.7f),
											modifier = Modifier.fillMaxWidth()
										)

										// Show gradient fade when not expanded
										if (!isExpanded) {
											Box(
												modifier = Modifier
													.fillMaxWidth()
													.height(40.dp)
													.align(Alignment.BottomCenter)
													.background(
														Brush.verticalGradient(
															colors = listOf(
																Color.Transparent,
																Color.White
															)
														)
													)
											)
										}
									}
								}
							}
							Spacer(modifier = Modifier.height(20.dp))
						}
					}
					// Fixed Bottom Button
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.align(Alignment.BottomCenter)
							.padding(16.dp)
					) {
						Button(
							onClick = {
								navController.navigate(ConfirmationScreen)
							},
							modifier = Modifier
								.fillMaxWidth()
								.height(50.dp),
							colors = ButtonDefaults.buttonColors(Color(0xFFE65100)),
							shape = RoundedCornerShape(12.dp)
						) {
							Text(text = "Book now", fontSize = 16.sp, color = Color.White)
						}
					}
				}
			}

		}
	}
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YoutubePlayer(videoId: String) {
	AndroidView(
		modifier = Modifier.fillMaxSize(),
		factory = { context ->
			WebView(context).apply {
				webViewClient = WebViewClient()
				settings.javaScriptEnabled = true
				loadUrl("https://youtu.be/$videoId")
			}
		}
	)
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun OnlinePlayer() {

	// Lifecycle :->
	var lifecycle by remember {
		mutableStateOf(Lifecycle.Event.ON_CREATE)
	}

	// Get context :->
	val context = LocalContext.current

	// Media Item :->
	val mediaItem =
		MediaItem.fromUri("https://cdn.pixabay.com/video/2024/04/18/208442_large.mp4") // Apply Changes.

	// Media source :->
	val mediaSource: MediaSource =
		ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory()) // Add this.
			.createMediaSource(mediaItem)

	val exoPlayer = remember {
		ExoPlayer.Builder(context).build().apply {
			setMediaSource(mediaSource)  // Apply Changes.
			prepare()
			playWhenReady = true
		}
	}

	// Lifecycle for composable :->
	val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
	DisposableEffect(key1 = lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			lifecycle = event
		}
		lifecycleOwner.lifecycle.addObserver(observer)

		onDispose {
			exoPlayer.release()
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	// Android View :->
	AndroidView(
		modifier = Modifier
			.fillMaxSize()
			.aspectRatio(23f / 21f),
		factory = {
			PlayerView(context).also { playerView ->
				playerView.player = exoPlayer
			}
		},
		update = {
			when (lifecycle) {
				Lifecycle.Event.ON_RESUME -> {
					it.onPause()
					it.player?.pause()
				}

				Lifecycle.Event.ON_PAUSE -> {
					it.onResume()
				}

				else -> Unit
			}
		}
	)

}

