package com.domain.resources

sealed class CustomError(open val message: String?) {
	data class ApiError(val code: Int, override val message: String?) : CustomError(message)
	data class NetworkError(override val message: String? = "Network error, please check your connection") : CustomError(message)
	data class UnknownError(override val message: String? = "Something went wrong") : CustomError(message)
}
