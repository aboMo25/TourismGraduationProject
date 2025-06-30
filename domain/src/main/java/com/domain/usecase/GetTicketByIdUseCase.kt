package com.domain.usecase

import com.domain.model.Ticket
import com.domain.network.ResultWrapper
import com.domain.repository.CheckoutRepository // Import your CheckoutRepository

class GetTicketByIdUseCase(
	private val checkoutRepository: CheckoutRepository
) {
	suspend operator fun invoke(bookingId: String): ResultWrapper<Ticket> {
		return checkoutRepository.getTicketById(bookingId)
	}
}