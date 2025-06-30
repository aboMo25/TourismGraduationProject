package com.domain.repository

import com.domain.model.MainPlace2

interface MainPlacesRepository{
	suspend fun getMainPlaces(): List<MainPlace2>
}
