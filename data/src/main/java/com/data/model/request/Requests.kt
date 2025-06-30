package com.data.model.request

import com.data.model.CustomizeTripResponseDto
import com.data.model.TripsResponseDto
import com.data.model.response.GenericSuccessResponse
import com.domain.model.Ticket
import com.domain.model.request.AddTripToCartRequest
import com.domain.model.request.CreateOrderRequest
import com.domain.model.response.AuthResponse
import com.domain.model.response.CartItemResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

data class RegisterRequestAction(
	val email: String,
	val password: String,
	val name: String
)

data class LoginRequestAction(
	val email: String,
	val password: String
)

interface ApiService {
	@GET("popular/places")
	suspend fun getTrips(): TripsResponseDto

	@GET("custom/places")
	suspend fun getCustomTrips(): CustomizeTripResponseDto

	@POST("user/register") // Replace with your actual endpoint
	suspend fun register(@Body request: RegisterRequestAction): AuthResponse

	@POST("user/login") // Replace with your actual endpoint
	suspend fun login(@Body request: LoginRequestAction): AuthResponse

	// Add endpoint for authenticating with Google token if your backend handles it
	@POST("auth/google") // Replace with your actual endpoint
	suspend fun authenticateWithGoogle(@Body token: String): AuthResponse // Assuming backend accepts raw token string

	@POST("cart/add-trip") // NEW: Endpoint for adding a trip to cart
	suspend fun addTripToCart(@Body request: AddTripToCartRequest): GenericSuccessResponse

	@POST("cart/add") // !!! REPLACE with your actual backend endpoint for adding places !!!
	suspend fun addPlaceToCart(@Body request: AddPlaceToCartRequest): GenericSuccessResponse

	@POST("cart/add-hotel") // !!! REPLACE with your actual backend endpoint for adding hotels !!!
	suspend fun addHotelToCart(@Body request: AddHotelToCartRequest): GenericSuccessResponse

	@GET("cart/{userId}")
	suspend fun getCartItems(@Path("userId") userId: String): List<CartItemResponseDto> // CHANGE: From Int to String

	@DELETE("cart/{userId}/item/{itemId}")
	suspend fun removeItemFromCart(
		@Path("userId") userId: String, // CHANGE: From Int to String
		@Path("itemId") itemId: String
	): GenericSuccessResponse

	@PUT("cart/{userId}/item/{itemId}/quantity")
	suspend fun updateItemQuantity(
		@Path("userId") userId: String, // CHANGE: From Int to String
		@Path("itemId") itemId: String,
		@Query("quantity") quantity: Int
	): GenericSuccessResponse

	@DELETE("cart/{userId}")
	suspend fun clearCart(@Path("userId") userId: String): GenericSuccessResponse // CHANGE: From Int to String

	// NEW: Checkout/Create Order
	@POST("checkout/order") // Define your backend endpoint for creating an order
	suspend fun createOrder(@Body request: CreateOrderRequest): Ticket // Backend returns the Ticket
	@GET("tickets/{bookingId}") // Define your backend endpoint for fetching a ticket
	suspend fun getTicketById(@Path("bookingId") bookingId: String): Ticket
}
// --- END NEW ---}

// https://traveleg.onrender.com/cart/add/:userId/:customPlaceId/:tripId