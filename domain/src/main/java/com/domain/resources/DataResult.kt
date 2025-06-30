package com.domain.resources

sealed class DataResult<out T> {
	data class Success<out T>(val data: T) : DataResult<T>()
	data class Error(val error: CustomError) : DataResult<Nothing>()
}