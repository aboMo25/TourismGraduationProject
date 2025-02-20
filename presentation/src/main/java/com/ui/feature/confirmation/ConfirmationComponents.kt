package com.ui.feature.confirmation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	isError: Boolean = false,
	errorMessage: String? = null
) {
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		label = { Text(label) },
		modifier = Modifier.fillMaxWidth(),
		singleLine = true,
		isError = isError,
		colors = OutlinedTextFieldDefaults.colors(
			focusedBorderColor = Color(0xFFE65100),
			focusedLabelColor = Color(0xFFE65100)
		),
		leadingIcon = {
			Icon(
				Icons.Default.Person,
				contentDescription = null,
				tint = Color(0xFFE65100)
			)
		}
	)
		if (isError && errorMessage != null) {
			Text(
				text = errorMessage,
				color = MaterialTheme.colorScheme.error,
				style = MaterialTheme.typography.bodySmall,
				modifier = Modifier.padding(start = 16.dp, top = 4.dp)
			)
		}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberInput(
	value: String,
	label: String,
	onValueChange: (String) -> Unit,
) {
	var isError by remember { mutableStateOf(false) }

	OutlinedTextField(
		value = value,
		onValueChange = {
			if (it.length <= 11 && it.all { char -> char.isDigit() }) {
				onValueChange(it)
				isError = it.isNotEmpty() && it.length != 11
			}
		},
		label = { Text(label) },
		keyboardOptions = KeyboardOptions(
			keyboardType = KeyboardType.Phone,
			imeAction = ImeAction.Next
		),
		colors = OutlinedTextFieldDefaults.colors(
			focusedBorderColor =Color(0xFFE65100),
			focusedLabelColor = Color(0xFFE65100)
		),
		leadingIcon = {
			Icon(
				Icons.Default.Phone,
				contentDescription = null,
				tint = Color(0xFFE65100)
			)
		},
		modifier = Modifier.fillMaxWidth(),
		singleLine = true,
		isError = isError,
		supportingText = {
			if (isError) {
				Text(
					text = "Phone number must be 11 digits",
					color = MaterialTheme.colorScheme.error
				)
			}
		}
	)
}