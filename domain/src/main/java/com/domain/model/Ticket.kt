package com.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate // Required for LocalDate
import kotlinx.serialization.Serializable // Add this for Kotlinx Serialization
import kotlinx.serialization.SerialName // Add this for Kotlinx Serialization

// This is the main Ticket model returned by the backend after order creation
@Serializable // Make the Ticket model serializable
data class Ticket(
	val bookingId: String,
	val userId: String, // Added as it's sent in CreateOrderRequest
	val customerName: String, // To display the user's name on the ticket
	val orderDate: String, // String representation of the order creation date (backend might send this)
	val totalAmount: Double, // Redundant if itemsIncluded always sum up, but good for backend validation/quick display
	@Serializable(with = LocalDateSerializer::class) // Custom serializer for LocalDate
	val tripStartDate: LocalDate, // Use LocalDate for proper date handling
	@Serializable(with = LocalDateSerializer::class) // Custom serializer for LocalDate
	val tripEndDate: LocalDate,   // Use LocalDate for proper date handling
	val itemsIncluded: List<TicketItem> // This will hold the actual items purchased
)
// --- Defining the TicketItem hierarchy (mirroring CartItem2 but for the final ticket) ---
// This is crucial for the backend to understand what to return within 'itemsIncluded'
@Serializable
sealed class TicketItem {
	abstract val id: String
	abstract val name: String
	abstract val price: Double?
	abstract val imageUrl: String
	abstract val itemType: String
}

@Serializable
@SerialName("TRIP") // NEW: Discriminator for Kotlinx Serialization
data class TripTicketItem(
	override val imageUrl: String,
	override val id: String,
	override val name: String,
	override val price: Double,
	override val itemType: String = "TRIP", // Explicit discriminator
	val includedPlaces: List<PlaceTicketItem>, // List of places within the trip
	val includedHotel: HotelTicketItem? // Optional hotel within the trip
) : TicketItem()
@Serializable
data class PlaceTicketItem(
	override val imageUrl: String,
	override val id: String,
	override val name: String,
	val description: String?, // Nullable as per AllPlaces2
	override val price: Double,
	override val itemType: String="PLACE", // Default itemType for places
) : TicketItem()

@Serializable
@SerialName("HOTEL") // Maps to itemType = "HOTEL" in JSON
data class HotelTicketItem(
	override val imageUrl: String,
	override val id: String,
	override val name: String,
	val description: String?, // Nullable
	override val price: Double,
	val location: String?,
	val ratings: String?, // Can be nullable or default
	val numberOfReviews: Int = 0,
	// Note: imagesOfHotelImages are often not needed on the ticket itself, just the main imageUrl
	override val itemType: String = "HOTEL"
) : TicketItem()
//
//@Serializable
//@SerialName("TRANSPORT") // Maps to itemType = "TRANSPORT" in JSON
//data class TransportTicketItem(
//	override val imageUrl: String,
//	override val id: String,
//	override val name: String,
//	override val price: Double,
//	// Add other relevant transport-specific details if needed, e.g., type, route
//	override val itemType: String = "TRANSPORT"
//) : TicketItem()
// --- PassengerDetails model for the ticket ---
