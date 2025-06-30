package com.domain.repository

import com.domain.model.Ticket
import com.domain.model.request.CreateOrderRequest
import com.domain.network.ResultWrapper

interface CheckoutRepository {
	suspend fun createOrder(request: CreateOrderRequest): ResultWrapper<Ticket>
	suspend fun getTicketById(bookingId: String): ResultWrapper<Ticket> // New method
}