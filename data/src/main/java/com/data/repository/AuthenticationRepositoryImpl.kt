//package com.data.repository
//
//import com.data.model.request.ApiService
//import com.domain.model.response.AuthResponse
//import com.domain.network.safeApiCall
//import com.domain.network.toDataResult
//import com.domain.repository.AuthenticationRepository
//import com.domain.resources.DataResult
//import kotlinx.coroutines.Dispatchers
//
//class AuthenticationRepositoryImpl(private val apiService: ApiService) : AuthenticationRepository {
//    override suspend fun authenticateWithBackend(googleToken: String): DataResult<AuthResponse> {
//        return safeApiCall(Dispatchers.IO) {
//            apiService.authenticateWithGoogle(googleToken)
//        }.toDataResult() // <--- CALL THE EXTENSION FUNCTION HERE
//        // Removed .checkResultAndReturn because toDataResult already gives a DataResult
//        // The error handling logic for DataResult.Error will now be handled upstream
//        // by the ViewModel where it observes the DataResult.
//    }
//}
