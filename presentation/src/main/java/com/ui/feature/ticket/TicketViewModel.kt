package com.ui.feature.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ui.feature.home.HomeScreenUIEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketViewModel: ViewModel() {
	private val _uiState = MutableStateFlow<TicketUiState>(TicketUiState.Loading)
	val uiState = _uiState.asStateFlow()

	fun loadTickets() {
		viewModelScope.launch {
			_uiState.value = TicketUiState.Loading
			try {
				// Replace with your actual data loading logic
				val tickets = listOf(
					Ticket(
						id = 20242410003.toInt(),
						title = "Joy Fantasy Castle",
						description = "Joyworld Fantasy",
						price = 49.99,
						quantity = 1
					)
				)
				_uiState.value = TicketUiState.Success(tickets)
			} catch (e: Exception) {
				_uiState.value = TicketUiState.Error(e)
			}
		}
	}
}

sealed class TicketUiState {
	object Loading : TicketUiState()
	data class Success(val tickets: List<Ticket>) : TicketUiState()
	data class Error(val error: Throwable) : TicketUiState()
}

data class Ticket(
	val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val quantity: Int,
)