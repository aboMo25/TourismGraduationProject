package com.ui.feature.splash

import com.example.tourismgraduationproject.R
import com.ui.feature.confirmation.PaymentMethod
import com.ui.feature.confirmation.TripClass
import com.data.model.response.Attraction
import com.data.model.response.Destination

object TourismData {
	val governorates = listOf(
		Destination(
			id = "1",
			name = "Cairo",
			description = "Experience the ancient wonders of Egypt's capital",
			imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
			attractions = listOf(
				Attraction(
					id = "c1",
					name = "Great Pyramids",
					description = "Visit the last remaining wonder of the ancient world",
					price = 50.00
				),
				Attraction(
					id = "c2",
					name = "Egyptian Museum",
					description = "Explore thousands of ancient Egyptian artifacts",
					price = 25.00
				),
				Attraction(
					id = "c3",
					name = "Khan el-Khalili",
					description = "Shop in one of the world's oldest bazaars",
					price = 15.00
				)
			)
		),
		Destination(
			id = "2",
			name = "Luxor",
			description = "An open-air museum of ancient Egyptian monuments",
			imageUrl = "https://images.unsplash.com/photo-1587975844577-56dde2578a1d",
			attractions = listOf(
				Attraction(
					id = "karnak",
					name = "Karnak Temple",
					description = "The largest religious building ever constructed",
					price = 20.0
				),
				Attraction(
					id = "valley",
					name = "Valley of the Kings",
					description = "Ancient burial ground of Egyptian pharaohs",
					price = 22.0
				)
			)
		),
		Destination(
			id = "aswan",
			name = "Aswan",
			description = "A serene Nile Valley destination known for its natural beauty",
			imageUrl = "https://images.unsplash.com/photo-1553913861-c0fddf2619ee",
			attractions = listOf(
				Attraction(
					id = "philae",
					name = "Philae Temple",
					description = "A temple complex dedicated to goddess Isis",
					price = 18.0
				),
				Attraction(
					id = "nubian",
					name = "Nubian Village",
					description = "Experience authentic Nubian culture and hospitality",
					price = 12.0
				)
			)
		)
		// Add more destinations as needed
	)

		val tripClasses = listOf(
		TripClass(
			id = "economy",
			name = "Economy",
			description = "Basic comfortable accommodations",
			priceMultiplier = 1.0
		),
		TripClass(
			id = "business",
			name = "Business",
			description = "Enhanced comfort with additional amenities",
			priceMultiplier = 1.5
		),
		TripClass(
			id = "first",
			name = "First Class",
			description = "Premium experience with luxury services",
			priceMultiplier = 2.0
		)
	)

	val paymentMethods = listOf(
		PaymentMethod("Vodafone Cash", "Mobile money transfer via Vodafone", "card"),
		PaymentMethod("PayPal", "International payment system", "card"),
		PaymentMethod("Fawry", "Local payment service", "card"),
		PaymentMethod("Credit Card", "Visa/Mastercard payment", "card"),
		PaymentMethod("Bank Transfer", "Direct bank transfer", "card"),
		PaymentMethod("UPI", "Unified payment interface", "card")
	)

	val splashScreens = listOf(
		SplashScreenData(
			image = R.drawable.login, // URL for image
			title = "Ancient Wonders",
			description = "Discover the magnificent pyramids of Giza, standing tall for over 4,500 years"
		),
		SplashScreenData(
			image = R.drawable.login, // URL for image
			title = "Nile River Experience",
			description = "Cruise along the legendary Nile River and explore ancient temples and tombs"
		),
		SplashScreenData(
			image = R.drawable.login, // URL for image
			title = "Cultural Heritage",
			description = "Immerse yourself in vibrant markets and experience authentic Egyptian culture"
		)
	)
	val detailsScreen = listOf(
		DetailsScreenData(
			image = R.drawable.mainpic, // URL for image
			title = "Ancient Wonders",
			description = "Discover the magnificent pyramids of Giza, standing tall for over 4,500 years"
		),
		DetailsScreenData(
			image = R.drawable.login, // URL for image
			title = "Nile River Experience",
			description = "Cruise along the legendary Nile River and explore ancient temples and tombs"
		),
		DetailsScreenData(
			image = R.drawable.bbbb, // URL for image
			title = "Cultural Heritage",
			description = "Immerse yourself in vibrant markets and experience authentic Egyptian culture"
		)
	)

}
