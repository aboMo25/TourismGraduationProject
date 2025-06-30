// repositoryModule.kt
package com.data.di

import com.data.repository.*
import com.domain.repository.*
import org.koin.dsl.module

val repositoryModule = module {


    single <CartRepository3>{CartRepository3Impl(get())}


    single <TripRepository>{TripRepositoryImpl(get())}
    single <HotelsRepository>{AllHotelsRepositoryImpl()}
    single <AllCategoriesRepository>{AllCategoriesRepositoryImpl()}


    single <MainPlacesRepository>{MainPlacesRepositoryImpl(get())}
    single <SplashDataRepository>{SplashDataRepositoryImpl()}
    single <CustomizedRepository>{CustomizedRepositoryImpl()}

    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRemoteDataSource> { AuthenticationRemoteDataSourceImpl() }

    single<CheckoutRepository> { CheckoutRepositoryImpl(get()) }

}
