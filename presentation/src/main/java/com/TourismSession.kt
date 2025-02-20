package com

import android.content.Context
import com.domain.model.UserDomainModel

class TourismSession(private val context: Context) {
	companion object {
		private const val PREF_NAME = "user"
		private const val KEY_ID = "id"
		private const val KEY_USERNAME = "username"
		private const val KEY_EMAIL = "email"
		private const val KEY_NAME = "name"
		private const val KEY_AUTH_TYPE = "auth_type"
		private const val KEY_GOOGLE_TOKEN = "google_token"
	}

	private val sharedPref by lazy {
		context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
	}

	fun storeUser(user: UserDomainModel) {
		with(sharedPref.edit()) {
			putInt(KEY_ID, user.id!!)
			putString(KEY_USERNAME, user.username)
			putString(KEY_EMAIL, user.email)
			putString(KEY_NAME, user.name)
			putString(KEY_AUTH_TYPE, "email")
			apply()
		}
	}

	fun storeGoogleUser(user: UserDomainModel, googleToken: String) {
		with(sharedPref.edit()) {
			putInt(KEY_ID, user.id!!)
			putString(KEY_USERNAME, user.username)
			putString(KEY_EMAIL, user.email)
			putString(KEY_NAME, user.name)
			putString(KEY_AUTH_TYPE, "google")
			putString(KEY_GOOGLE_TOKEN, googleToken)
			apply()
		}
	}

	fun getUser(): UserDomainModel? {
		val id = sharedPref.getInt(KEY_ID, 0)
		val username = sharedPref.getString(KEY_USERNAME, null)
		val email = sharedPref.getString(KEY_EMAIL, null)
		val name = sharedPref.getString(KEY_NAME, null)

		return if (id != 0 && username != null && email != null && name != null) {
			UserDomainModel(id, username, email, name)
		} else {
			null
		}
	}

	fun getAuthType(): String? {
		return sharedPref.getString(KEY_AUTH_TYPE, null)
	}

	fun getGoogleToken(): String? {
		return sharedPref.getString(KEY_GOOGLE_TOKEN, null)
	}

	fun isLoggedIn(): Boolean {
		return getUser() != null
	}

	fun clearUser() {
		sharedPref.edit().clear().apply()
	}

	fun updateGoogleToken(token: String) {
		sharedPref.edit().putString(KEY_GOOGLE_TOKEN, token).apply()
	}
	fun logStoredUser() {
		val user = getUser()
		println("Stored User: $user")
		println("Auth Type: ${getAuthType()}")
		println("Google Token: ${getGoogleToken()}")
	}

}
