package com

import android.annotation.SuppressLint
import android.content.Context
import com.domain.model.UserDomainModel // Make sure this is imported

class TourismSession(private val context: Context) {

	companion object {
		private const val PREF_NAME = "tourism_auth_session"
		private const val KEY_AUTH_TOKEN = "auth_token"
		private const val KEY_AUTH_TYPE = "auth_type"
		private const val KEY_GOOGLE_TOKEN = "google_token"

		// New keys for user specific data
		private const val KEY_USER_ID = "user_id"
		private const val KEY_USER_EMAIL = "user_email"
		private const val KEY_USER_NAME = "user_name"
	}

	private val sharedPref by lazy {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
	}

	@SuppressLint("UseKtx")
	fun storeAuthToken(token: String) { // Expects a non-null String
		with(sharedPref.edit()) {
			putString(KEY_AUTH_TOKEN, token)
			apply()
		}
	}

	fun getAuthToken(): String? {
		return sharedPref.getString(KEY_AUTH_TOKEN, null)
	}

	@SuppressLint("UseKtx")
	fun storeAuthType(authType: String) {
		with(sharedPref.edit()) {
			putString(KEY_AUTH_TYPE, authType)
			apply()
		}
	}

	fun getAuthType(): String? {
		return sharedPref.getString(KEY_AUTH_TYPE, null)
	}

	@SuppressLint("UseKtx")
	fun storeGoogleToken(googleToken: String) {
		with(sharedPref.edit()) {
			putString(KEY_GOOGLE_TOKEN, googleToken)
			apply()
		}
	}

	fun getGoogleToken(): String? {
		return sharedPref.getString(KEY_GOOGLE_TOKEN, null)
	}

	@SuppressLint("UseKtx")
	fun storeUser(user: UserDomainModel) {
		with(sharedPref.edit()) {
			user.id?.let { putInt(KEY_USER_ID, it) } ?: remove(KEY_USER_ID) // Safely store ID, or remove if null
			putString(KEY_USER_EMAIL, user.email)
			putString(KEY_USER_NAME, user.name)
			apply()
		}
	}

	fun getUserId(): Int? {
		val id = sharedPref.getInt(KEY_USER_ID, -1)
		return if (id != -1) id else null
	}

	fun getUserEmail(): String? {
		return sharedPref.getString(KEY_USER_EMAIL, null)
	}

	fun getUserName(): String? {
		return sharedPref.getString(KEY_USER_NAME, null)
	}

	fun isLoggedIn(): Boolean {
		return getAuthToken() != null && getUserId() != null
	}

	@SuppressLint("UseKtx")
	fun clearSession() {
		sharedPref.edit().clear().apply()
	}

	fun logStoredToken() {
		println("Stored Auth Token: ${getAuthToken()?.let { if (it.length > 10) it.substring(0, 10) + "..." else it } }")
		println("Auth Type: ${getAuthType()}")
		println("Google Token: ${getGoogleToken()?.let { if (it.length > 10) it.substring(0, 10) + "..." else it } }")
		println("Stored User ID: ${getUserId()}")
		println("Stored User Email: ${getUserEmail()}")
		println("Stored User Name: ${getUserName()}")
	}
}