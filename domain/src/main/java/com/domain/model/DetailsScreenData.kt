package com.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable // Important!

 // Add this if you use Kotlinx Serialization
 @Serializable // If you need to serialize this model for requests, keep @Serializable
 data class DetailsScreenData(
	 val id: String?, // This should be the backend's _id (e.g., "685d77d19594604509f1602d")
	 val image: String,
	 val title: String,
	 val listOfImages: List<String>,
	 val description: String,
	 val rating: Double,
	 val location: String,
	 val reviewList: List<Review>,
	 val timeToOpen: String,
	 val price: Int // Use Int if backend price is Int, convert to Double if needed at display layer
 )