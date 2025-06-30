package com.domain.usecase

import com.domain.model.Ticket
import com.domain.model.request.CreateOrderRequest
import com.domain.network.ResultWrapper
import com.domain.repository.CheckoutRepository

class CreateOrderUseCase(private val checkoutRepository: CheckoutRepository) {
	suspend operator fun invoke(request: CreateOrderRequest): ResultWrapper<Ticket> {
		return checkoutRepository.createOrder(request)
	}
}