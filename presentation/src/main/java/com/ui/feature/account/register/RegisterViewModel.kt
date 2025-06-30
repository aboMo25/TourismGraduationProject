package com.ui.feature.account.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.network.ResultWrapper
import com.domain.usecase.RegisterUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
	private val registerUseCase: RegisterUseCase,
	private val tourismSession: TourismSession,
//	private val authenticateWithBackendUseCase: AuthenticateWithBackendUseCase,
	@Suppress("DEPRECATION") private val googleSignInClient: GoogleSignInClient
) : ViewModel() {
	private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
	val registerState = _registerState

	@Suppress("DEPRECATION")
	fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

	fun register(email: String, password: String, name: String) {
		_registerState.value = RegisterState.Loading
		viewModelScope.launch {
			when (val response = registerUseCase.execute(email, password, name)) {
				is ResultWrapper.Success -> {
					// --- CRITICAL CHANGE HERE ---
					val token = response.value.token // Get the token
					val user = response.value.user // Get the user

					// Ensure both token and user are not null
					tourismSession.storeAuthToken(token)
					tourismSession.storeUser(user)
					tourismSession.storeAuthType("email")
					tourismSession.logStoredToken()
					_registerState.value = RegisterState.Success()
					// --- END CRITICAL CHANGE ---
				}
				is ResultWrapper.Failure -> {
					_registerState.value = RegisterState.Error(
						response.exception.message ?: "Registration failed!"
					)
				}
			}
		}
	}

//	fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
//		if (account == null) {
//			_registerState.value = RegisterState.Error("Google sign in failed or cancelled")
//			return
//		}
//		_registerState.value = RegisterState.Loading
//		viewModelScope.launch {
//			account.idToken?.let { googleIdToken -> // Renamed `token` to `googleIdToken` for clarity
//				when (val result = authenticateWithBackendUseCase(googleIdToken)) {
//					is DataResult.Success -> {
//						// --- CRITICAL CHANGE HERE (similar logic) ---
//						val backendAuthToken = result.data.token // Token from your backend
//						val user = result.data.user // User from your backend
//
//						if (backendAuthToken != null && user != null) {
//							tourismSession.storeAuthToken(backendAuthToken)
//							tourismSession.storeUser(user)
//							tourismSession.storeGoogleToken(googleIdToken) // Store the Google ID token
//							tourismSession.storeAuthType("google")
//							tourismSession.logStoredToken()
//							_registerState.value = RegisterState.Success()
//						} else {
//							_registerState.value = RegisterState.Error(
//								"Authentication successful, but received null token or user data from backend."
//							)
//						}
//						// --- END CRITICAL CHANGE ---
//					}
//					is DataResult.Error -> {
//						_registerState.value = RegisterState.Error(result.error.message ?: "Failed to authenticate with backend using Google")
//					}
//				}
//			} ?: run {
//				_registerState.value = RegisterState.Error("Google ID token is null after sign-in. Check GSO configuration.")
//			}
//		}
//	}
	fun resetRegisterState() {
		_registerState.value = RegisterState.Idle
	}
}
sealed class RegisterState {
	object Idle : RegisterState()
	object Loading : RegisterState()
	class Success : RegisterState()
	data class Error(val message: String) : RegisterState()
}