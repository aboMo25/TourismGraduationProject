package com.data.network

import com.data.model.request.ApiService
import com.data.model.request.LoginRequestAction
import com.data.model.request.RegisterRequestAction
import com.data.model.request.AddHotelToCartRequest
import com.data.model.request.AddPlaceToCartRequest
import com.data.model.response.GenericSuccessResponse
import com.domain.model.response.AuthResponse
import com.domain.model.response.CartItemResponse
import io.ktor.client.HttpClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
	private const val BASE_URL = "https://traveleg.onrender.com/" // !!! IMPORTANT: Replace with your actual backend URL !!!

	private val loggingInterceptor = HttpLoggingInterceptor().apply {
		setLevel(HttpLoggingInterceptor.Level.BODY)
	}

	private val okHttpClient = OkHttpClient.Builder()
		.addInterceptor(loggingInterceptor)
		.build()

	val apiService: ApiService by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create()) // Use Gson to convert JSON to Kotlin objects
			.build()
			.create(ApiService::class.java)
	}
}