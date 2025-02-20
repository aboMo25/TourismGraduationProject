package com.ui.feature.account.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.UserDomainModel
import com.domain.network.ResultWrapper
import com.domain.resources.DataResult
import com.domain.usecase.AuthenticateWithBackendUseCase
import com.domain.usecase.RegisterUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ui.feature.account.login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
	private val registerUseCase: RegisterUseCase,
	private val tourismSession: TourismSession,
	private val authenticateWithBackendUseCase: AuthenticateWithBackendUseCase,
	private val googleSignInClient: GoogleSignInClient // Add this
) : ViewModel() {
	private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
	val registerState = _registerState

	fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

	fun register(email: String, password: String, name: String) {
		_registerState.value = RegisterState.Loading
		viewModelScope.launch {
            // Use case for registering a user
			when (val response = registerUseCase.execute(email, password, name)) {
				is ResultWrapper.Success -> {
					tourismSession.storeUser(response.value)
					_registerState.value = RegisterState.Success()
				}

				is ResultWrapper.Failure -> {
					_registerState.value = RegisterState.Error(
						response.exception.message
							?: "Something went wrong!"
					)
				}
			}
		}
	}

	fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
		if (account == null) {
			_registerState.value = RegisterState.Error("Google sign in failed")
			return
		}
		_registerState.value = RegisterState.Loading
		viewModelScope.launch {
			account.idToken?.let { token ->
				when (val result = authenticateWithBackendUseCase(token)) {
					is DataResult.Success -> {
						// Create a UserDomainModel from Google account info
						val user = UserDomainModel(
							id = 0, // This will be updated from your backend
							username = account.email ?: "",
							email = account.email ?: "",
							name = account.displayName ?: ""
						)
						tourismSession.storeGoogleUser(user, token)
						tourismSession.logStoredUser() // Debugging line
						_registerState.value = RegisterState.Success()
					}
					is DataResult.Error -> {
						_registerState.value = RegisterState.Error("Failed to authenticate with backend")
					}
				}
			} ?: run {
				_registerState.value = RegisterState.Error("Google token is null")
			}
		}
	}

}

sealed class RegisterState {
	object Idle : RegisterState()
	object Loading : RegisterState()
	class Success : RegisterState()
	data class Error(val message: String) : RegisterState()
}