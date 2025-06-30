package com.data.model.local

import com.domain.model.CustomizedTripData
import com.domain.model.HotelImages
import com.domain.model.Hotels2
import com.domain.model.SplashScreenData

object TourismData {

	val customizedTripData = listOf(
		CustomizedTripData(
			id = "1",
			name = "Places",
			imageUrl = "https://th.bing.com/th/id/OIP.uaqGCBE4FYVxt0fZWzz46QHaE8?w=254&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"
		),
		CustomizedTripData(
			id = "2",
			name = "Hotels",
			imageUrl = "https://th.bing.com/th/id/R.fe205ac4bb2c1bb85f44fb21c8a6a5e2?rik=jc688P%2bQAknnYQ&riu=http%3a%2f%2fa.mktgcdn.com%2fp%2fwQDGIWO4Uf9ySCJGGh9zNdVgGD1rIEbSo1EK5j-FQqw%2f3839x2475.jpg&ehk=f2NMN6gaoJ37%2fPWYoFX58e152zETyTInQ9EZLLwjnlc%3d&risl=&pid=ImgRaw&r=0",
		),
		CustomizedTripData(
			id = "3",
			name = "Transports",
			imageUrl = "https://th.bing.com/th/id/OIP.uaqGCBE4FYVxt0fZWzz46QHaE8?w=254&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"
		)
	)
	val categories = listOf(
		"Beach",
		"Adventure",
		"Mountain",
		"Relaxation",
		"Nature"
	)
	val splashScreens = listOf(
		SplashScreenData(
			image = "https://i.pinimg.com/1200x/ab/8c/50/ab8c5037cd8eed0833260cb540b899ac.jpg", // URL for image
			title = "Ancient Wonders",
			description = "Discover the magnificent pyramids of Giza, standing tall for over 4,500 years"
		),
		SplashScreenData(
			image = "https://i.pinimg.com/1200x/ab/8c/50/ab8c5037cd8eed0833260cb540b899ac.jpg", // URL for image
			title = "Nile River Experience",
			description = "Cruise along the legendary Nile River and explore ancient temples and tombs"
		),
		SplashScreenData(
			image = "https://i.pinimg.com/1200x/ab/8c/50/ab8c5037cd8eed0833260cb540b899ac.jpg", // URL for image
			title = "Cultural Heritage",
			description = "Immerse yourself in vibrant markets and experience authentic Egyptian culture"
		)
	)
//	val mainPlaces2 = listOf(
//		MainPlace2(
//			id = "1",
//			name = "cairo",
//			imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//			allPlaces = listOf<AllPlaces2>(
//				AllPlaces2(
//					id = "giza",
//					name = "Giza",
//					price = 120.0,
//					placeData = DetailsScreenData(
//						id =1,  // Unique ID for Giza
//						rating = 4.5,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Egyptian Museum",
//						description = "Discover the magnificent pyramids of Giza",
//						location = "Giza, Egypt",
//						timeToOpen = "9:00 AM - 5:00 PM",
//						listOfImages = listOf(
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//						),
//						price =100.0,
//					),
//					description = "The capital and largest city of Egypt",
//					imageUrl = "https://th.bing.com/th/id/OIP.uaqGCBE4FYVxt0fZWzz46QHaE8",
//					quantity = 1
//				),
//				AllPlaces2(
//					id = "citadel",
//					name = "Citadel",
//					price = 120.0,
//					placeData = DetailsScreenData(
//						id = 2,  // Unique ID for Citadel
//						rating = 4.3,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Cairo Citadel",
//						description = "Historic Islamic fortification",
//						timeToOpen = "8:00 AM - 6:00 PM",
//						listOfImages =  listOf(
//							"https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg",
//							"https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg",
//							"https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg"
//						),
//						location =  "Cairo, Egypt",
//						price = 200.0
//					),
//					description = "A medieval Islamic fortification",
//					imageUrl = "https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg",
//					quantity = 1
//				),
//				AllPlaces2(
//					id = "zoo",
//					name = "Zoo",
//					price = 120.0,
//					description = "The capital and largest city of Egypt",
//					imageUrl = "https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6",
//					quantity = 1,
//					placeData = DetailsScreenData(
//						id = 3,  // Unique ID for Zoo
//						rating = 4.3,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Giza Zoo",
//						description = "One of the oldest zoos in Africa",
//						listOfImages = listOf(
//							"https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6",
//							"https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6",
//							"https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6"
//						),
//						timeToOpen =  "9:00 AM - 5:00 PM",
//						location = "Giza, Egypt",
//						price = 150.0
//					),
//				),
//				// Add other places with unique IDs...
//			)
//		),
//		MainPlace2(
//			id = "2",
//			name = "luxor",
//			imageUrl = "https://th.bing.com/th/id/OIP.DbFUJpmyveKLXno2DaRO5gHaGj",
//			allPlaces = listOf<AllPlaces2>(
//				AllPlaces2(
//					id = "karnak",
//					name = "Karnak",
//					price = 120.5,
//					placeData = DetailsScreenData(
//						id =4,  // Unique ID for Karnak
//						rating = 4.2,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Karnak Temple",
//						description = "Vast temple complex",
//						listOfImages =  listOf(
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//						),
//						location =  "Luxor, Egypt",
//						timeToOpen = "6:00 AM - 5:00 PM",
//						price = 180.0
//					),
//					description = "The largest ancient religious site",
//					imageUrl = "",
//					quantity = 1
//				),
//				AllPlaces2(
//					id = "philae",
//					name = "Philae Temple",
//					price = 120.2,
//					placeData = DetailsScreenData(
//						id = 5,  // Unique ID for Philae
//						rating = 4.2,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Philae Temple",
//						description = "Temple complex dedicated to Isis",
//						listOfImages = listOf(
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//						),
//						location = "Aswan, Egypt",
//						timeToOpen = "7:00 AM - 6:00 PM",
//						price = 150.0
//					),
//					description = "A temple complex dedicated to goddess Isis",
//					imageUrl = "",
//					quantity = 1
//				)
//			)
//		),
//		MainPlace2(
//			id = "1",
//			name = "cairo",
//			imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//			allPlaces = listOf<AllPlaces2>(
//				AllPlaces2(
//					id = "giza",
//					name = "Giza",
//					price = 120.0,
//					placeData = DetailsScreenData(
//						id = 1,  // Unique ID for Giza
//						rating = 4.5,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Egyptian Museum",
//						description = "Discover the magnificent pyramids of Giza",
//						location = "Giza, Egypt",
//						timeToOpen = "9:00 AM - 5:00 PM",
//						listOfImages = listOf(
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//							"https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//						),
//						price =100.0,
//					),
//					description = "The capital and largest city of Egypt",
//					imageUrl = "https://th.bing.com/th/id/OIP.uaqGCBE4FYVxt0fZWzz46QHaE8",
//					quantity = 1
//				),
//				AllPlaces2(
//					id = "citadel",
//					name = "Citadel",
//					price = 120.0,
//					placeData = DetailsScreenData(
//						id = 2,  // Unique ID for Citadel
//						rating = 4.8,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Cairo Citadel",
//						description = "Historic Islamic fortification",
//						timeToOpen = "8:00 AM - 6:00 PM",
//						listOfImages =  listOf(
//							"https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg",
//							"https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg",
//							"https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg"
//						),
//						location =  "Cairo, Egypt",
//						price = 200.0
//					),
//					description = "A medieval Islamic fortification",
//					imageUrl = "https://www.annees-de-pelerinage.com/wp-content/uploads/2018/08/entrance-cairo-citadel.jpg",
//					quantity = 1
//				),
//				AllPlaces2(
//					id = "zoo",
//					name = "Zoo",
//					price = 120.0,
//					placeData = DetailsScreenData(
//						id = 3,  // Unique ID for Zoo
//						rating =4.1,
//						image = "",
//						reviewList = listOf(
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2025",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 3.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Osama Ahmed",
//								date = "25/5/2026",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a"
//
//							),
//							Review(
//								id = "1",
//								rating = 4.3,
//								comment = "Great trip! The attractions were amazing, and the staff were friendly.",
//								name = "Abdo Mohamed",
//								date = "25/5/2003",
//								imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
//
//							)
//							// Add more reviews as needed
//						),
//						title = "Giza Zoo",
//						description = "One of the oldest zoos in Africa",
//						listOfImages = listOf(
//							"https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6",
//							"https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6",
//							"https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6"
//						),
//						timeToOpen =  "9:00 AM - 5:00 PM",
//						location = "Giza, Egypt",
//						price = 150.0
//					),
//					description = "The capital and largest city of Egypt",
//					imageUrl = "https://th.bing.com/th/id/OIP.H_-Hzd_d9FgZwH8fFpkEdgHaE6",
//					quantity = 1
//				),
//			)
//		),
//	)
	val hotels2 = listOf(
		Hotels2(
			id = "2",
			name = "Hilton Garden Inn",
			ratings = "100",
			imageUrl = "https://th.bing.com/th/id/R.fe205ac4bb2c1bb85f44fb21c8a6a5e2?rik=jc688P%2bQAknnYQ&riu=http%3a%2f%2fa.mktgcdn.com%2fp%2fwQDGIWO4Uf9ySCJGGh9zNdVgGD1rIEbSo1EK5j-FQqw%2f3839x2475.jpg&ehk=f2NMN6gaoJ37%2fPWYoFX58e152zETyTInQ9EZLLwjnlc%3d&risl=&pid=ImgRaw&r=0",
			description = "this is one of the highest quality hotels ",
			price = 25.0,
			location = "Sharm ElSheikh",
			numberOfReviews = 100,
			imagesOfHotelImages = listOf(
				HotelImages(
					imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
				),
				HotelImages(
					imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
				),
				HotelImages(
					imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
				)
			)
		),
		Hotels2(
			id ="1",
			name = "Hilton MeshGarden Inn",
			ratings = "100",
			imageUrl = "",
			description = "this is one of the highest quality hotels ",
			price = 150.0,
			location = "Atwab",
			numberOfReviews = 50,
			imagesOfHotelImages = listOf(
				HotelImages(
					imageUrl = "https://th.bing.com/th/id/OIP.uaqGCBE4FYVxt0fZWzz46QHaE8?w=254&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"
				),
				HotelImages(
					imageUrl = "https://th.bing.com/th/id/OIP.uaqGCBE4FYVxt0fZWzz46QHaE8?w=254&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7"
				),
				HotelImages(
					imageUrl = "https://images.unsplash.com/photo-1572252009286-268acec5ca0a",
				)
			)

		)
	)

}
