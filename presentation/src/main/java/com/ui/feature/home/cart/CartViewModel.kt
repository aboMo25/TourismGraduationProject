//package com.ui.feature.home.cart
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.domain.model.CartItem
//import com.domain.usecase.GetCartItemsUseCase
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//
//class CartViewModel(
//	private val getCartItemsUseCase: GetCartItemsUseCase,
//) : ViewModel() {
//	private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Loading)
//	val uiState = _uiState.asStateFlow()
//	init {
//		loadCartItems()
//	}
//
//	private fun loadCartItems() {
//		viewModelScope.launch {
//			try {
//				val items = getCartItemsUseCase()
//				_uiState.value = CartUiState.Success(items)
//			} catch (e: Exception) {
//				_uiState.value = CartUiState.Error(e.localizedMessage ?: "Unknown Error")
//			}
//		}
//	}
//}
//
//sealed class CartUiState {
//	object Loading : CartUiState()
//	data class Success(val items: List<CartItem>) : CartUiState()
//	data class Error(val message: String) : CartUiState()
//}