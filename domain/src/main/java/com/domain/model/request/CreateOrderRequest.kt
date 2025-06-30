package com.domain.model.request

import com.domain.model.CartItem2
import com.domain.model.PassengerDetails

data class CreateOrderRequest(
	val userId: String,
	val cartItems: List<CartItem2>, // The actual items from the cart
	val passengerDetails: PassengerDetails, // Your collected passenger info
	val selectedPaymentMethod: String,
	val voucherCode: String?, // Nullable if no voucher
	val finalAmount: Double ,// The final calculated amount after discounts
	 val tripStartDate: String, // If you need dates from somewhere else
	 val tripEndDate: String,
	// val hotelDetails: Hotel?, // If a hotel was part of the cart and needs separate handling for the order
)