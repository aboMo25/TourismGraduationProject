package com

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tourismgraduationproject.R
import com.navigation.CartScreen2
import com.navigation.CheckoutTicketScreen
import com.navigation.CreateScreen
import com.navigation.CustomizedScreen
import com.navigation.FinalScreen
import com.navigation.HomeScreen
import com.navigation.HotelsScreen
import com.navigation.LoginScreen
import com.navigation.PackageListScreen
import com.navigation.PassengerDetailsScreen
import com.navigation.PaymentMethodScreen
import com.navigation.PlacesOfInterestScreen
import com.navigation.ProfileScreen
import com.navigation.RegisterScreen
import com.navigation.Routes
import com.navigation.Screens
import com.navigation.SplashScreen
import com.navigation.TicketDetailsScreen
import com.navigation.TicketScreen
import com.ui.feature.account.login.LoginScreen
import com.ui.feature.account.register.RegisterScreen
import com.ui.feature.cart.CartScreen2
import com.ui.feature.checkout.checkout_ticket_screen.CheckoutTicketScreen
import com.ui.feature.checkout.finalScreen.FinalScreen
import com.ui.feature.checkout.passenger_details_screen.PassengerDetailsScreen
import com.ui.feature.checkout.payment_method_screen.PaymentMethodScreen
import com.ui.feature.customized.CustomizedScreen
import com.ui.feature.home.HomeScreen
import com.ui.feature.home.detailsHomeScreen.DetailsScreen
import com.ui.feature.hotels.HotelDetailScreen
import com.ui.feature.hotels.HotelsScreen
import com.ui.feature.packages.PackageListScreen
import com.ui.feature.places.detailsOfPOI.DetailsOfPlacesScreen
import com.ui.feature.places.governates.PlacesOfInterestScreen
import com.ui.feature.places.mainPlaces.PlacesScreen
import com.ui.feature.profile.ProfileScreen
import com.ui.feature.splash.SplashScreen
import com.ui.feature.ticket.MainTicket
import com.ui.feature.ticket.TicketDetailsScreen
import com.ui.theme.AutomatedTourismProjectTheme
import org.koin.android.ext.android.inject

@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.R)

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

		enableEdgeToEdge()
		setContent {
			val tourismSession: TourismSession by inject()
			AutomatedTourismProjectTheme {
				val shouldShowBottomNav = remember { mutableStateOf(true) }
				val navController = rememberNavController()

				Scaffold(
					modifier = Modifier.fillMaxSize(),
					containerColor = Color(0xFFF5E1C0),
					// Set container color to match background
					bottomBar = {
						AnimatedVisibility(
							visible = shouldShowBottomNav.value,
							enter = fadeIn()
						) {
							BottomNavigationBar(navController)
						}
					}
				) { paddingValues ->
					Box(
						modifier = Modifier
							.fillMaxSize()
							.padding(
								top = paddingValues.calculateTopPadding(),
								bottom = if (shouldShowBottomNav.value) 56.dp else paddingValues.calculateBottomPadding()
							)
					) {
						NavHost(
							navController = navController,
							// FIX: Use tourismSession.isLoggedIn() instead of tourismSession.getUser()
							startDestination = if (tourismSession.isLoggedIn()) {
								HomeScreen
							} else {
								SplashScreen
							}
						) {
							composable<LoginScreen> {
								shouldShowBottomNav.value = false
								LoginScreen(navController)
							}
//							composable<CartScreen> {
//								shouldShowBottomNav.value = false
//								CartScreen(navController)
//							}
							composable<CartScreen2> {
								shouldShowBottomNav.value = false
								CartScreen2(navController)
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
							composable<TicketScreen> {
								shouldShowBottomNav.value = true
								MainTicket(navController)
							}
							composable<PackageListScreen> {
								shouldShowBottomNav.value = false
								PackageListScreen(navController)
							}
							composable<CheckoutTicketScreen> {
								shouldShowBottomNav.value = false
								CheckoutTicketScreen(navController)
							}
							composable<PassengerDetailsScreen> {
								shouldShowBottomNav.value = false
								PassengerDetailsScreen(navController)
							}
							composable<PaymentMethodScreen> {
								shouldShowBottomNav.value = false
								PaymentMethodScreen(navController)
							}
							composable(
								route = "details_screen/{tripId}",
								arguments = listOf(navArgument("tripId") {
									type = NavType.StringType
								})
							) { backStackEntry ->
								val tripId = backStackEntry.arguments?.getString("tripId")
								shouldShowBottomNav.value = false
								if (tripId != null) {
									DetailsScreen(navController = navController, tripId = tripId)
								} else {
									Text("Invalid Trip ID")
								}
							}
							composable(
								route = Screens.TicketDetails.route,
								arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
							) { backStackEntry ->
								val bookingId = backStackEntry.arguments?.getString("bookingId")
								if (bookingId != null) {
									TicketDetailsScreen(navController = navController) // ViewModel will retrieve bookingId via SavedStateHandle
								} else {
									// Handle error: bookingId is null (shouldn't happen if route is correctly formed)
									Text("Error: Booking ID missing.", color = Color.Red)
								}
							}
							composable(
								route = Routes.HOTEL_DETAILS,
								arguments = listOf(navArgument("hotelId") { type = NavType.StringType })
							) { backStackEntry ->
								val hotelId = backStackEntry.arguments?.getString("hotelId")
								if (hotelId != null) {
									HotelDetailScreen(
										navController = navController,
										hotelId = hotelId)
								} else {
									Text("Invalid Hotel ID")
								}
							}
							composable(
								route = "place_details_screen/{placeId}",
								arguments = listOf(navArgument("placeId") { type = NavType.StringType })
							) { backStackEntry ->

								val placeId = backStackEntry.arguments?.getString("placeId") ?: return@composable
								DetailsOfPlacesScreen(
									navController = navController,
									placeId = placeId
								)
							}
							composable<PlacesOfInterestScreen> {
								shouldShowBottomNav.value = false
								PlacesOfInterestScreen(navController)
							}
							composable<CustomizedScreen> {
								shouldShowBottomNav.value = false
								CustomizedScreen(navController)
							}
							composable(
								route = "places_screen/{mainPlaceId}",
								arguments = listOf(navArgument("mainPlaceId") { type = NavType.StringType })
							) { backStackEntry ->
								val mainPlaceId = backStackEntry.arguments?.getString("mainPlaceId") ?: ""
								PlacesScreen(navController = navController, mainPlaceId = mainPlaceId)
							}


							composable<ProfileScreen> {
								shouldShowBottomNav.value = true
								ProfileScreen(navController)
							}

							composable<FinalScreen> {
								shouldShowBottomNav.value = false
								FinalScreen(navController)
							}
							composable<TicketDetailsScreen> {
								shouldShowBottomNav.value = false
								TicketDetailsScreen(navController)
							}
							composable<HotelsScreen> {
								shouldShowBottomNav.value = false
								HotelsScreen(navController)
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
		modifier = Modifier
			.fillMaxWidth()
			.height(130.dp)
	) {
		// Bottom Navigation Bar
		NavigationBar(
			modifier = Modifier
				.align(Alignment.Center)
				.fillMaxWidth(),
			tonalElevation = 0.dp // Remove elevation to prevent white edges
		) {
			val currentRoute =
				navController.currentBackStackEntryAsState().value?.destination?.route
			val items = listOf(
				BottomNavItems.Home,
				BottomNavItems.Ticket,
				BottomNavItems.Profile
			)

			items.forEach { item ->
				val isSelected =
					currentRoute?.substringBefore("?") == item.route::class.qualifiedName
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
							color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.7f)
						)
					},
					icon = {
						Image(
							painter = painterResource(id = item.icon),
							contentDescription = null,
							colorFilter = ColorFilter.tint(
								if (isSelected) Color.Black else Color.Black.copy(alpha = 0.7f)
							),
							modifier = Modifier.size(24.dp)
						)
					},
					colors = NavigationBarItemDefaults.colors(
						selectedIconColor = Color.White,
						selectedTextColor = Color.White,
						unselectedTextColor = Color.White.copy(alpha = 0.7f),
						unselectedIconColor = Color.White.copy(alpha = 0.7f),
						indicatorColor = Color(0xFFB6B4B4)
					)
				)
			}
		}

		// Centered FAB that cuts into the bottom bar
		FloatingActionButton(
			onClick = {
				navController.navigate(CustomizedScreen)
			},
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(8.dp)
				.offset(y = (-130).dp) // Increased negative offset for better cutoff
				.size(80.dp), // Increased size for better visibility
			containerColor = Color.White,
			shape = CircleShape,
			elevation = FloatingActionButtonDefaults.elevation(
				defaultElevation = 8.dp,
				pressedElevation = 12.dp
			)
		) {
			Icon(
				painter = painterResource(id = R.drawable.add),
				contentDescription = "Create Package",
				tint = Color(0xFF000000),
				modifier = Modifier.size(40.dp)
			)
		}
	}
}

sealed class BottomNavItems(val route: Any, val title: String, val icon: Int) {
	object Home : BottomNavItems(HomeScreen, "Home", icon = R.drawable.ic_home)
	object Ticket : BottomNavItems(TicketDetailsScreen, "Ticket", icon = R.drawable.baseline_airplane_ticket_24)
	object Profile : BottomNavItems(ProfileScreen, "Profile", icon = R.drawable.ic_profile_bn)
}