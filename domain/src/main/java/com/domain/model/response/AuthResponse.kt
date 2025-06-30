package com.domain.model.response

import com.domain.model.UserDomainModel

data class AuthResponse (
	val user: UserDomainModel,
	val token: String
)