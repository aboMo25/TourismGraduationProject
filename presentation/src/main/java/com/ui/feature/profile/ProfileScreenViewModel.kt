package com.ui.feature.profile

import androidx.lifecycle.ViewModel
import com.TourismSession


class ProfileScreenViewModel(
	private val tourismSession: TourismSession
) : ViewModel(){
	fun logoutUser() {
		tourismSession.clearSession()
	}
}