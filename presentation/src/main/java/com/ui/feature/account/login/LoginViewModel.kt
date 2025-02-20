// LoginViewModel.kt
package com.ui.feature.account.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TourismSession
import com.domain.model.UserDomainModel
import com.domain.network.ResultWrapper
import com.domain.resources.DataResult
import com.domain.usecase.AuthenticateWithBackendUseCase
import com.domain.usecase.LoginUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tourismSession: TourismSession,
    private val authenticateWithBackendUseCase: AuthenticateWithBackendUseCase,
    private val googleSignInClient: GoogleSignInClient // Add this

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
                    tourismSession.storeUser(response.value)
                    _loginState.value = LoginState.Success()
                    isLoggedIn.value = true
                }
                is ResultWrapper.Failure -> {
                    _loginState.value = LoginState.Error(
                        response.exception.message ?: "Something went wrong!"
                    )
                }
            }
        }
    }

    fun handleGoogleSignInResult(account: GoogleSignInAccount?) {
        if (account == null) {
            _loginState.value = LoginState.Error("Google sign in failed")
            return
        }
        _loginState.value = LoginState.Loading
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
                        _loginState.value = LoginState.Success()
                        isLoggedIn.value = true // Update login state

                    }
                    is DataResult.Error -> {
                        _loginState.value = LoginState.Error("Failed to authenticate with backend")
                    }
                }
            } ?: run {
                _loginState.value = LoginState.Error("Google token is null")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    class Success : LoginState()
    data class Error(val message: String) : LoginState()
}
