// repositoryModule.kt
package com.data.di

import com.data.repository.*
import com.domain.repository.*
import com.data.repository.AuthenticationRemoteDataSourceImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get()) }
    single<AuthenticationRemoteDataSource> { AuthenticationRemoteDataSourceImpl() }

}
