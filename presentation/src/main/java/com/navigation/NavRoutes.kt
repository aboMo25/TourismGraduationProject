package com.navigation


import kotlinx.serialization.Serializable

const val PLACE_DETAILS_SCREEN_BASE = "place_details_screen"
fun placeDetailsRoute(placeId: String) = "$PLACE_DETAILS_SCREEN_BASE/$placeId"

const val HOTELS_DETAILS_SCREEN_BASE = "hotels_details_screen"
fun hotelDetailsRoute(hotelId: String) = "$HOTELS_DETAILS_SCREEN_BASE/$hotelId"

object Routes {
	const val HOTEL_DETAILS = "hotels_details_screen/{hotelId}"
	// Helper function to build the route with arguments
	fun getHotelDetailsRoute(hotelId: String) = "hotels_details_screen/$hotelId"
}
sealed class Screens(val route: String) {
	object Home : Screens("home_screen")
	object Cart : Screens("cart_screen")
	// ... other screens

	// Define the route for TicketDetailsScreen with an argument
	object TicketDetails : Screens("ticket_details_screen/{bookingId}") {
		fun createRoute(bookingId: String) = "ticket_details_screen/$bookingId"
	}
}
@Serializable
object HomeScreen

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
object CartScreen
@Serializable
object CartScreen2

@Serializable
object Setting

@Serializable
object CreateScreen

@Serializable
object OrdersScreen

@Serializable
object ProfileScreen

@Serializable
object TicketScreen

@Serializable
object SplashScreen

@Serializable
object PackageListScreen

@Serializable
object CheckoutTicketScreen

@Serializable
object PassengerDetailsScreen
@Serializable
object PaymentMethodScreen

@Serializable
object DetailsScreen

@Serializable
object DetailsOfPlaceScreen

@Serializable
object ConfirmationScreen

@Serializable
object PlacesOfInterestScreen
@Serializable
object CustomizedScreen
@Serializable
object PlacesScreen

@Serializable
object FinalScreen

@Serializable
object HotelsScreen

@Serializable
object TicketDetailsScreen
