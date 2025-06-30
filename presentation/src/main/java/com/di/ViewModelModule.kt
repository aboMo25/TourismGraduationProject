package com.di

import com.ui.feature.account.login.LoginViewModel
import com.ui.feature.account.register.RegisterViewModel
import com.ui.feature.cart.CartViewModel2
import com.ui.feature.checkout.checkout_ticket_screen.CheckoutTicketScreenViewModel
import com.ui.feature.checkout.finalScreen.FinalScreenViewModel
import com.ui.feature.checkout.passenger_details_screen.PassengerDetailsScreenViewModel
import com.ui.feature.checkout.payment_method_screen.PaymentMethodScreenViewModel
import com.ui.feature.customized.CustomizedScreenViewModel
import com.ui.feature.home.HomeViewModel
import com.ui.feature.home.detailsHomeScreen.DetailsScreenViewModel
import com.ui.feature.hotels.HotelsViewModel
import com.ui.feature.packages.PackageListViewModel
import com.ui.feature.places.detailsOfPOI.DetailsOfPlaceScreenViewModel
import com.ui.feature.places.governates.PlacesOfInterestViewModel
import com.ui.feature.places.mainPlaces.PlacesViewModel
import com.ui.feature.profile.ProfileScreenViewModel
import com.ui.feature.splash.SplashViewModel
import com.ui.feature.ticket.TicketDetailsScreenViewModel
import com.ui.feature.ticket.TicketViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
	viewModel {
		HomeViewModel(get())
	}
	viewModel{
		SplashViewModel(get(), get())
	}
	viewModel {
		LoginViewModel(
			loginUseCase = get(),
			tourismSession = get(),
			googleSignInClient = get() // Add this parameter
		)
	}
// CartViewModel2 is used for the new cart implementation
	viewModel {
		CartViewModel2(
			getCartItemsUseCase = get(),
			removeItemFromCartUseCase = get(),
			clearCartUseCase = get(),
			updateItemQuantityUseCase = get(),
			tourismSession = get(),
			addHotelToCartUseCase = get(),
			addPlaceToCartUseCase = get(),
			checkoutRepository = get(),
		)
	}

	viewModel {
		RegisterViewModel(get(),get(),get(),)
	}
	viewModel {
        ProfileScreenViewModel(get())
    }
	viewModel {
		CheckoutTicketScreenViewModel(get (), get(), get()) // Inject the new use case
    }
	viewModel {
        PassengerDetailsScreenViewModel()
    }
	viewModel {
        PaymentMethodScreenViewModel(get())
    }
	viewModel {
		PackageListViewModel(get())
	}
	viewModel {
		DetailsScreenViewModel(get(),get(), get(), get(),)
	}
	viewModel {
		PlacesOfInterestViewModel(get())
	}
	viewModel {
		CustomizedScreenViewModel(get())
	}
//	viewModel {
//		CartViewModel(get())
//	}
	viewModel {
		PlacesViewModel(get())
	}
	viewModel {
        DetailsOfPlaceScreenViewModel(get(),get(), get())
    }

	viewModel {
		HotelsViewModel(get(),get(), get(), )
	}
	viewModel {
		TicketViewModel()
	}
	viewModel {
		TicketDetailsScreenViewModel(get(),get(),get(),)
	}
	viewModel {
		FinalScreenViewModel()
	}
}