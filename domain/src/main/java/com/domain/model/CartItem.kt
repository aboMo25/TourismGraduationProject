package com.domain.model

sealed class CartItem {
	data class PlaceItem(val place: AllPlaces) : CartItem()
	data class HotelItem(val hotel: Hotels) : CartItem()
}