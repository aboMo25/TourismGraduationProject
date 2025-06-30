package com.ui.feature.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.network.ResultWrapper
import com.domain.usecase.LoginUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tourismSession: TourismSession,
//    private val authenticateWithBackendUseCase: AuthenticateWithBackendUseCase,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState

    val isLoggedIn = MutableStateFlow(tourismSession.isLoggedIn()) // Expose login state

    fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            when (val response = loginUseCase.execute(email, password)) {
                is ResultWrapper.Success -> {
                    // Store the authentication token received from the API
                    tourismSession.storeAuthToken(response.value.token)
                    // !!! NEW LINE: Store the user details received from the successful login response
                    tourismSession.storeUser(response.value.user) // <--- THIS IS THE KEY ADDITION
                    tourismSession.storeAuthType("email") // Indicate auth type
                    tourismSession.logStoredToken() // For debugging
                    _loginState.value = LoginState.Success()
                    isLoggedIn.value = true
                }
                is ResultWrapper.Failure -> {
                    _loginState.value = LoginState.Error(
                        response.exception.message ?: "Login failed!" // More specific error message
                    )
                }
            }
        }
    }

//    fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
//        if (account == null) {
//            _loginState.value = LoginState.Error("Google sign in failed or cancelled")
//            return
//        }
//        _loginState.value = LoginState.Loading
//        viewModelScope.launch {
//            account.idToken?.let { token ->
//                when (val result = authenticateWithBackendUseCase(token)) {
//                    is DataResult.Success -> {
//                        // Store the authentication token received from the backend
//                        tourismSession.storeAuthToken(result.data.token) // Access the token from AuthResponse
//                        // !!! NEW LINE: Store the user details received from Google authentication response
//                        tourismSession.storeUser(result.data.user) // <--- THIS IS THE KEY ADDITION
//                        tourismSession.storeGoogleToken(token) // Keep the Google ID token if needed for refresh/backend
//                        tourismSession.storeAuthType("google") // Indicate auth type
//                        tourismSession.logStoredToken() // For debugging
//                        _loginState.value = LoginState.Success()
//                        isLoggedIn.value = true // Update login state
//                    }
//                    is DataResult.Error -> {
//                        _loginState.value = LoginState.Error(result.error.message ?: "Failed to authenticate with backend using Google")
//                    }
//                }
//            } ?: run {
//                _loginState.value = LoginState.Error("Google ID token is null")
//            }
//        }
//    }
    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    class Success : LoginState()
    data class Error(val message: String) : LoginState()
}