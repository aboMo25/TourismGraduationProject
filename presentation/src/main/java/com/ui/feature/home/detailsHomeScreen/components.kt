package com.ui.feature.home.detailsHomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.bumptech.glide.Glide
import com.domain.model.Review
import com.example.tourismgraduationproject.R

@Composable
fun ReviewCard(
	reviewee: Review,
) {
	Card(
		shape = RoundedCornerShape(8.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
		modifier = Modifier
			.width(400.dp)
			.padding(16.dp),
		colors = CardDefaults.cardColors(Color.White)
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
		) {
			// Rating section
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(bottom = 8.dp)
			) {
				Text(
					text = "4.5/5",
					fontSize = 24.sp,
					fontWeight = FontWeight.Bold,
					color = Color(0xFF2196F3),
					modifier = Modifier.padding(end = 4.dp)
				)
				Text(
					text = "(very good)",
					fontSize = 24.sp,
					color = Color.Gray
				)
			}
			// Rating stars (optional - add if you want visual stars)
			Row(
				modifier = Modifier.padding(top = 8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				repeat(5) { index ->
					Icon(
						imageVector = Icons.Default.Star,
						contentDescription = null,
						tint = if (index < 4) Color(0xFFFFD700) else Color.LightGray,
						modifier = Modifier.size(16.dp)
					)
					if (index < 4) Spacer(modifier = Modifier.width(2.dp))
				}
			}
			// Review text
			Text(
				text = reviewee.comment.toString(),
				fontSize = 16.sp,
				modifier = Modifier.padding(vertical = 8.dp)
			)
			// Reviewer info
			Row(
				modifier = Modifier.padding(top= 8.dp),
				verticalAlignment = Alignment.CenterVertically
			){
				AsyncImage(
					model = reviewee.imageUrl,
					contentDescription = "Place Image",
					modifier = Modifier
						.size(48.dp)
						.clip(RoundedCornerShape(16.dp))
						.padding(end =8.dp)
						.border(0.dp, Color(0xFF4FC3F7), CircleShape),
					contentScale = ContentScale.Crop
				)
				Column(modifier = Modifier.padding(top = 8.dp)) {
					Text(
						text = "David Thompson",
						fontSize =24.sp,
						fontWeight = FontWeight.Bold
					)
					Text(
						text = "12/08/2024",
						fontSize = 16.sp,
						color = Color.Gray
					)
				}
			}
		}
	}

}


@Composable
fun VideoDialog(onDismiss: () -> Unit) {
	Dialog(onDismissRequest = onDismiss) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(400.dp)
				.padding(8.dp)
				.background(Color.Black, shape = RoundedCornerShape(12.dp))
		) {
			Column(
				modifier = Modifier.padding(8.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				// Close button
				IconButton(
					onClick = onDismiss,
					modifier = Modifier.align(Alignment.End)
				) {
					Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
				}
				// Video player
				OnlinePlayer2()
			}
		}
	}
}

@Composable
fun OnlinePlayer2() {
	var showThumbnail by remember { mutableStateOf(true) }
	val context = LocalContext.current
	val videoUrl = "https://cdn.pixabay.com/video/2024/04/18/208442_large.mp4"

	val thumbnailBitmap = remember {
		mutableStateOf<Bitmap?>(null)
	}

	LaunchedEffect(videoUrl) {
		val request = Glide.with(context)
			.asBitmap()
			.load(videoUrl)
			.frame(1000000)
			.submit()

		try {
			thumbnailBitmap.value = request.get() as Nothing?
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	val exoPlayer = remember {
		ExoPlayer.Builder(context).build().apply {
			setMediaItem(MediaItem.fromUri(videoUrl))
			prepare()
			playWhenReady = false
		}
	}

	DisposableEffect(Unit) {
		onDispose {
			exoPlayer.release()
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		if (!(!showThumbnail || thumbnailBitmap.value == null)) {
			androidx.compose.foundation.Image(
				bitmap = thumbnailBitmap.value!!.asImageBitmap(),
				contentDescription = "Video Thumbnail",
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop
			)

			IconButton(
				onClick = {
					showThumbnail = false
					exoPlayer.playWhenReady = true
				},
				modifier = Modifier
					.align(Alignment.Center)
					.size(64.dp)
					.background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
			) {
				Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White)
			}
		} else {
			AndroidView(
				modifier = Modifier.fillMaxSize(),
				factory = {
					PlayerView(context).apply {
						player = exoPlayer
					}
				}
			)
		}
	}
}
@Composable
fun GuestRatingsCard() {
	Card(
		shape = RoundedCornerShape(16.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
		modifier = Modifier
			.padding(8.dp)
			.fillMaxWidth(),
		colors = CardDefaults.cardColors(Color.White)
	) {
		Column(modifier = Modifier.padding(16.dp)) {
			Text("Guest Ratings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
			Spacer(modifier = Modifier.height(4.dp))
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text("4.5", fontSize = 28.sp, fontWeight = FontWeight.Bold)
				Spacer(modifier = Modifier.width(8.dp))
				Row {
					repeat(5) {
						Icon(
							imageVector = Icons.Default.Star,
							contentDescription = "Star",
							tint = Color(0xFFFFC107), // Gold
							modifier = Modifier.size(20.dp)
						)
					}
				}
				Spacer(modifier = Modifier.width(8.dp))
				Text("128 verified reviews", fontSize = 12.sp, color = Color.Gray)
			}

			Spacer(modifier = Modifier.height(12.dp))

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Text("Categories", fontWeight = FontWeight.SemiBold)
				Text("See all", color = Color.Gray, fontSize = 12.sp)
			}

			Spacer(modifier = Modifier.height(8.dp))

			val ratings = listOf(4.5f, 4.8f, 4.1f, 4.5f)

			ratings.forEach { rating ->
				RatingRow("Cleanliness", rating)
			}
		}
	}
}

@Composable
fun RatingRow(label: String, rating: Float) {
	Column(modifier = Modifier.padding(vertical = 6.dp)) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(label, fontSize = 14.sp)
			Text(String.format("%.1f", rating), fontSize = 14.sp)
		}

		LinearProgressIndicator(
			progress = { rating / 5f },
			modifier = Modifier
				.fillMaxWidth()
				.height(6.dp)
				.clip(RoundedCornerShape(10.dp)),
			color = Color(0xFFB4EC51), // Light Green
			trackColor = Color.LightGray,
		)
	}
}


