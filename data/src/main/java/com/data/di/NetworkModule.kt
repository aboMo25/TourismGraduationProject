package com.data.di

import android.util.Log
import com.data.model.request.ApiService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
	single {
		HttpClient(CIO) {
			install(ContentNegotiation) {
				json(Json {
					prettyPrint = true
					isLenient = true
					ignoreUnknownKeys = true
				})
			}
			install(Logging) {
				level = LogLevel.ALL
				logger = object : Logger {
					override fun log(message: String) {
						Log.d("BackEndHandler", message)
					}
				}
			}
			install(Logging) {
				level = LogLevel.ALL
			}
		}
	}
	single {
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken("659100255594-mqbvql0t1itnn7drp4efbb2c88khp0vd.apps.googleusercontent.com") // <<< This is your Web Client ID
			.requestEmail()
			.requestProfile()
			.build()
		GoogleSignIn.getClient(androidContext(), gso)
	}

//	single {
//		RetrofitClient.apiService
//	}
	single {
		Retrofit.Builder()
			.baseUrl("https://traveleg.onrender.com/") // !!! IMPORTANT: Replace with your actual backend URL !!!
			.client(OkHttpClient.Builder()
				.addInterceptor(HttpLoggingInterceptor().apply {
					level = HttpLoggingInterceptor.Level.BODY
				}).build())
			.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
			.build()
			.create(ApiService::class.java)
	}
}
