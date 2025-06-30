package com.domain.di

//import com.domain.usecase.AddHotelToCartUseCase2
//import com.domain.usecase.AddItemToCartUseCase
//import com.domain.usecase.GetCartItemsUseCase2
import com.domain.usecase.AddHotelToCartUseCase
import com.domain.usecase.AddPlaceToCartUseCase
import com.domain.usecase.AddTripToCartUseCase
import com.domain.usecase.ClearCartUseCase
import com.domain.usecase.CreateOrderUseCase
import com.domain.usecase.GetAllCategoriesUseCase
import com.domain.usecase.GetAllHotelsUseCase
import com.domain.usecase.GetAllTripsUseCase
import com.domain.usecase.GetCartItemsUseCase
import com.domain.usecase.GetCustomizedTripUseCase
import com.domain.usecase.GetMainPlacesUseCase
import com.domain.usecase.GetSplashDataUseCase
import com.domain.usecase.GetTicketByIdUseCase
import com.domain.usecase.HandleSplashNavigationUseCase
import com.domain.usecase.LoginUseCase
import com.domain.usecase.RegisterUseCase
import com.domain.usecase.RemoveItemFromCartUseCase2
import com.domain.usecase.UpdateItemQuantityUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAllTripsUseCase(get()) }
    factory { GetAllHotelsUseCase(get()) }
    factory { GetAllCategoriesUseCase(get()) }

    factory { GetSplashDataUseCase(get()) }
    factory { GetCustomizedTripUseCase(get()) }
    factory { GetMainPlacesUseCase(get()) }
    factory { GetCartItemsUseCase(get()) }
//    factory { AddPlaceToCartUseCase2(get()) }
    factory { AddPlaceToCartUseCase(get()) }
    factory { AddHotelToCartUseCase(get()) }
//    factory { AddHotelToCartUseCase2(get()) }
//    factory { RemoveItemFromCartUseCase(get()) }
// cart 2
//    factory { AddItemToCartUseCase(get()) }
    factory { RemoveItemFromCartUseCase2(get()) }
//    factory { GetCartItemsUseCase2(get()) }
    factory { ClearCartUseCase(get()) }
    factory { UpdateItemQuantityUseCase(get()) }


    factory { HandleSplashNavigationUseCase(get()) }

    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
//    factory {AuthenticateWithBackendUseCase(get()) }

    // Use cases for Checkout
    factory { CreateOrderUseCase(get()) }
    factory { GetTicketByIdUseCase(get()) }
    factory { AddTripToCartUseCase(get()) }
}