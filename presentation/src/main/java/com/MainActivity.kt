package com

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.navigation.HomeScreen
import com.navigation.LoginScreen
import com.navigation.OrdersScreen
import com.navigation.ProfileScreen
import com.navigation.RegisterScreen
import com.ui.feature.account.login.LoginScreen
import com.ui.feature.account.register.RegisterScreen
import com.ui.feature.home.HomeScreen
import com.ui.theme.AutomatedTourismProjectTheme
import com.example.tourismgraduationproject.R
import com.navigation.ConfirmationScreen
import com.navigation.CreateScreen
import com.navigation.DetailsScreen
import com.navigation.PackageListScreen
import com.navigation.SplashScreen
import com.ui.feature.confirmation.ConfirmationScreen
import com.ui.feature.details.DetailsScreen
import com.ui.feature.packages.PackageListScreen
import com.ui.feature.profile.ProfileScreen
import com.ui.feature.splash.SplashScreen
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val tourismSession : TourismSession by inject()

			AutomatedTourismProjectTheme {
				val shouldShowBottomNav = remember { mutableStateOf(true) }
				val navController = rememberNavController()

				Scaffold(
					modifier = Modifier.fillMaxSize().background( Color(0xFFF5E1C0)),
					bottomBar = {
						AnimatedVisibility(
							visible = shouldShowBottomNav.value,
							enter = fadeIn()
						) {
							BottomNavigationBar(navController)
						}

					}
				) {
					Surface(
						modifier = Modifier
							.fillMaxSize()
							.background( Color(0xFFF5E1C0))
							.padding(it)
					) {
						NavHost(
							navController = navController,
							startDestination = if (tourismSession.getUser() != null) {
								HomeScreen
							} else {
								SplashScreen
							}
						) {

							composable<LoginScreen> {
								shouldShowBottomNav.value = false
								LoginScreen(navController)
							}
							composable<RegisterScreen> {
								shouldShowBottomNav.value = false
								RegisterScreen(navController)
							}
							composable<SplashScreen> {
								shouldShowBottomNav.value = false
								SplashScreen(navController)
							}
							composable<HomeScreen> {
								shouldShowBottomNav.value = true
								HomeScreen(navController)
							}

							composable<PackageListScreen> {
								shouldShowBottomNav.value = false
								PackageListScreen(navController)
							}
							composable<DetailsScreen> {
								shouldShowBottomNav.value = false
								DetailsScreen(navController)
							}
							composable<ConfirmationScreen> {
								shouldShowBottomNav.value = false
								ConfirmationScreen(navController)
							}
							composable<ProfileScreen> {
								shouldShowBottomNav.value = true
								ProfileScreen(navController)
							}
						}
					}
				}
			}
		}
	}
}

@Composable
fun BottomNavigationBar(navController: NavController) {
	Box(
		modifier = Modifier.fillMaxWidth().background(color = Color(0xFFF5E1C0)),
		contentAlignment = Alignment.BottomCenter
	) {
		NavigationBar(
			modifier = Modifier
				.background(Color(0xFFF5E1C0).copy(alpha = 0.8f)) // Sand color background
		) {
			val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
			val items = listOf(
				BottomNavItems.Home,
				BottomNavItems.Orders,
				BottomNavItems.Profile
			)

			items.forEach { item ->
				val isSelected = currentRoute?.substringBefore("?") == item.route::class.qualifiedName
				NavigationBarItem(
					selected = isSelected,
					onClick = {
						navController.navigate(item.route) {
							navController.graph.startDestinationRoute?.let { startRoute ->
								popUpTo(startRoute) {
									saveState = true
								}
							}
							launchSingleTop = true
							restoreState = true
						}
					},
					label = {
						Text(
							text = item.title,
							color = if (isSelected) Color(0xFFE65100) else Color(0xFF8D6E63)
						)
					},
					icon = {
						Image(
							painter = painterResource(id = item.icon),
							contentDescription = null,
							colorFilter = ColorFilter.tint(
								if (isSelected) Color(0xFF8D6E63) else Color(0xFFE65100)
							)
						)
					},
					colors = NavigationBarItemDefaults.colors(
						selectedIconColor = Color(0xFF8D6E63),
						selectedTextColor = Color(0xFFE65100),
						unselectedTextColor = Color(0xFF8D6E63),
						unselectedIconColor = Color(0xFFE65100)
					)
				)
			}
		}

		// Floating Create Button
		FloatingActionButton(
			onClick = { navController.navigate(BottomNavItems.Create.route) },
			modifier = Modifier
				.size(72.dp)
				.align(Alignment.BottomEnd)
				.offset(y = (-64).dp), // Moves half of the FAB inside the NavigationBar
			containerColor = Color(0xFFE65100), // Orange color
			shape = CircleShape,
			elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
		) {
			Icon(
				painter = painterResource(id = R.drawable.add),
				contentDescription = "Create Package",
				tint = Color.White
			)
		}
	}
}



sealed class BottomNavItems(val route: Any, val title: String, val icon: Int) {
	object Home : BottomNavItems(HomeScreen, "Home", icon = R.drawable.ic_home)
	object Create : BottomNavItems(CreateScreen, "Create", icon = R.drawable.add)
	object Setting : BottomNavItems(com.navigation.Setting, "Setting", icon = R.drawable.settings)
	object Orders : BottomNavItems(OrdersScreen, "Orders", icon = R.drawable.ic_orders)
	object Profile : BottomNavItems(ProfileScreen, "Profile", icon = R.drawable.ic_profile_bn)
}