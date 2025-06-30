package com.ui.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tourismgraduationproject.R
import com.navigation.LoginScreen
import com.navigation.ProfileScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
	navController: NavController,
	viewModel: ProfileScreenViewModel = koinViewModel(),
) {
	val scrollState = rememberScrollState()
	fun handleLogout() {
		viewModel.logoutUser()
		navController.navigate(LoginScreen) {
			popUpTo(ProfileScreen) { inclusive = true }
		}
	}
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.White)
			.verticalScroll(scrollState)
	) {
		// Profile Section
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(Color.White)
//				.offset(y = (-50).dp)
				.padding(32.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			// Profile Picture
			Surface(
				modifier = Modifier
					.size(120.dp)
					.clip(CircleShape),
				shadowElevation = 4.dp
			) {
				Image(
					painter = painterResource(id = R.drawable.profile2),
					contentDescription = "Profile Picture",
					contentScale = ContentScale.Crop,
					modifier = Modifier.fillMaxSize()
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			// Basic Information
			Text(
				color = Color(0xFF000000),
				text = "Abdel-Rahman Mohamed",
				fontSize = 24.sp,
				fontWeight = FontWeight.Bold
			)

			Spacer(modifier = Modifier.height(16.dp))
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					imageVector = Icons.Default.Email,
					contentDescription = "Email",
					tint = Color(0xFF000000).copy(.7f)
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text = "abdo@gmail.com", color = Color(0xFF000000),
				)
			}
			Spacer(modifier = Modifier.height(8.dp))
			Button(
				onClick = {},
				modifier = Modifier
					.width(220.dp),
				shape = RoundedCornerShape(16.dp),
				colors = ButtonDefaults.buttonColors(Color(0xFF4FC3F7)),
				elevation = ButtonDefaults.buttonElevation(
					defaultElevation = 6.dp,
					pressedElevation = 8.dp
				)
			) {
				Text(
					color = Color(0xFF000000),
					text = "View Profile",
					style = MaterialTheme.typography.titleMedium.copy(
						color = Color(0xFF000000), // Explicitly setting text color
						shadow = Shadow(
							color = Color.Black.copy(alpha = 0.25f),
							blurRadius = 4f
						)
					),
					fontWeight = FontWeight.Bold,
					modifier = Modifier.padding(vertical = 8.dp)
				)
			}
		}
		Text(
			text = "Profile Settings",
			fontSize = 16.sp,
			color = Color(0xFF000000),
			modifier = Modifier.padding(8.dp)
		)
		Spacer(modifier = Modifier.height(8.dp))
		ProfileSettingsItem(title = "Personal Information", icon = Icons.Default.Person)
		Spacer(modifier = Modifier.height(8.dp))
		ProfileSettingsItem(title = "Favorites", icon = Icons.Default.Favorite)
		Spacer(modifier = Modifier.height(8.dp))
		ProfileSettingsItem(title = "Chart Destinations", icon = Icons.Default.ShoppingCart)
		Spacer(modifier = Modifier.height(8.dp))
		ProfileSettingsItem(title = "Rides", icon = Icons.Default.DateRange)
		Spacer(modifier = Modifier.height(8.dp))
		Text(
			text = "Account Information",
			fontSize = 16.sp,
			color = Color(0xFF000000),
			modifier = Modifier.padding(8.dp)
		)
		ProfileSettingsItem(title = "Change Email", icon = Icons.Default.Email)
		Spacer(modifier = Modifier.height(8.dp))
		ProfileSettingsItem(title = "Change Password", icon = Icons.Default.Lock)
		Spacer(modifier = Modifier.height(24.dp))
		Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
			ButtonDesign(text = "Logout", onClick =  { handleLogout() })
		}


	}
}

@Composable
fun ProfileSettingsItem(
	title: String,
	icon: ImageVector,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(Color.Transparent),
	) {
		Row(
			modifier = Modifier.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Icon(
				modifier = Modifier
					.size(42.dp)
					.clip(CircleShape)
					.background(Color(0xFFF8F8F8).copy(alpha = 0.3f))
					.padding(8.dp),
				tint = Color(0xFF000000).copy(.7f),
				imageVector = icon,
				contentDescription = "Personal",
			)
			Spacer(modifier = Modifier.width(8.dp)) // Gap between icon and text
			Text(
				color = Color(0xFF000000),
				text = title,
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold,
				modifier = Modifier.weight(1f) // Pushes the arrow to the end

			)
			Icon(
				modifier = Modifier.padding(horizontal = 16.dp),
				tint = Color(0xFF4FC3F7).copy(.7f),
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
				contentDescription = "Personal",
			)


		}
	}
}

@Composable
fun ButtonDesign(
	text: String,
	onClick: () -> Unit,
) {
	Button(
		onClick = onClick,
		modifier = Modifier.width(320.dp).padding(bottom = 16.dp),
		colors = ButtonDefaults.buttonColors(Color(0xFF4FC3F7)),
		shape = RoundedCornerShape(16.dp),
		elevation = ButtonDefaults.buttonElevation(
			defaultElevation = 6.dp,
			pressedElevation = 8.dp
		)
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.titleMedium.copy(
				color = Color(0xFFFFFFFF), // Explicitly setting text color
				shadow = Shadow(
					color = Color.Black.copy(alpha = 0.25f),
					blurRadius = 4f
				)
			),
			fontWeight = FontWeight.Bold,
			modifier = Modifier.padding(vertical = 8.dp)
		)
	}
}
