package com.domain.network

// Remove: import android.net.http.HttpException // <--- REMOVE THIS
// Remove: import android.os.Build
// Remove: import android.os.ext.SdkExtensions

import com.domain.resources.CustomError
import com.domain.resources.DataResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

// Existing ResultWrapper (from your code) - No change here
sealed class ResultWrapper<out T> {
	data class Success<out T>(val value: T) : ResultWrapper<T>()
	data class Failure(val exception: Throwable, val code: Int? = null) : ResultWrapper<Nothing>()
}

// Utility function to safely make API calls
suspend fun <T> safeApiCall(
	dispatcher: CoroutineDispatcher,
	apiCall: suspend () -> T
): ResultWrapper<T> {
	return withContext(dispatcher) {
		try {
			ResultWrapper.Success(apiCall.invoke())
		} catch (throwable: Throwable) {
			when (throwable) {
				is IOException -> ResultWrapper.Failure(throwable, null) // Network error, no specific HTTP code
				is HttpException -> { // This is now guaranteed to be retrofit2.HttpException
					val code = throwable.code()
					// The convertErrorBody should return CustomError, not null usually
					// You might want to pass the Retrofit's errorBody() to convertErrorBody
					val customError = convertErrorBody(throwable) // Parse error body if needed

					// Decide if you want to use the CustomError in Failure or just the Throwable
					ResultWrapper.Failure(customError?.let { CustomErrorWrapper(it) } ?: throwable, code) // HTTP error
				}
				else -> ResultWrapper.Failure(throwable, null) // Generic error
			}
		}
	}
}

// Helper data class to wrap CustomError inside a Throwable if needed for ResultWrapper.Failure
data class CustomErrorWrapper(val customError: CustomError) : Exception(customError.message)


private fun convertErrorBody(throwable: HttpException): CustomError? {
	return try {
		val errorBodyString = throwable.response()?.errorBody()?.string()
		if (!errorBodyString.isNullOrBlank()) {
			// TODO: Parse 'errorBodyString' into a meaningful CustomError from your backend's error structure
			// Example: If your backend sends {"message": "Invalid credentials"}, parse that.
			CustomError.ApiError(throwable.code(), errorBodyString) // Placeholder for actual parsing
		} else {
			CustomError.UnknownError("API Error: ${throwable.message()}")
		}
	} catch (e: Exception) {
		CustomError.UnknownError("Failed to parse error body: ${e.message}")
	}
}
fun <T> ResultWrapper<T>.toDataResult(): DataResult<T> {
	return when (this) {
		is ResultWrapper.Success -> DataResult.Success(this.value)
		is ResultWrapper.Failure -> {
			val customError = if (this.exception is CustomErrorWrapper) {
				this.exception.customError
			} else {
				// Default error mapping if not a CustomErrorWrapper
				CustomError.UnknownError(this.exception.message ?: "An unexpected error occurred")
			}
			DataResult.Error(customError)
		}
	}
}