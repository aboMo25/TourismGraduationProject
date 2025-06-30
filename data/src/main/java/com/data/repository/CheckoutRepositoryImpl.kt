package com.data.repository

import com.data.model.request.ApiService
import com.domain.model.Ticket
import com.domain.model.request.CreateOrderRequest
import com.domain.network.ResultWrapper
import com.domain.network.safeApiCall
import com.domain.repository.CheckoutRepository
import kotlinx.coroutines.Dispatchers

class CheckoutRepositoryImpl(private val apiService: ApiService) : CheckoutRepository {
	override suspend fun createOrder(request: CreateOrderRequest): ResultWrapper<Ticket> {
		return safeApiCall(Dispatchers.IO) {
			apiService.createOrder(request)
		}
	}

	override suspend fun getTicketById(bookingId: String): ResultWrapper<Ticket> {
		return safeApiCall(Dispatchers.IO) {
			apiService.getTicketById(bookingId) // Assuming you'll add this to ApiService
		}
	}
}