package com.di

import android.content.Context
import com.TourismSession
import com.example.tourismgraduationproject.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presentationModule= module {
    includes(viewModelModule)
    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidContext().getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    single {
        GoogleSignIn.getClient(androidContext(), get<GoogleSignInOptions>())
    }
    single { TourismSession(get()) }
    single<CoroutineDispatcher> { Dispatchers.IO } // Provide Coroutine Dispatcher


}