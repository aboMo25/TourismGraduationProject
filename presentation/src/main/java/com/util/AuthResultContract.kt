package com.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log // Keep your Log import
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task

class AuthResultContract(private val googleSignInClient: GoogleSignInClient) :
	ActivityResultContract<Int, Task<GoogleSignInAccount>?>() {

	override fun createIntent(context: Context, input: Int): Intent {
		// The 'input' here (which is 0 in your Composables) is just a placeholder.
		// We only need the signInIntent from the GoogleSignInClient.
		return googleSignInClient.signInIntent
	}

	override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
		// Log the result code for debugging purposes
		Log.d("AuthResultContract", "parseResult: resultCode=$resultCode")

		// Only proceed if the result was successful (Activity.RESULT_OK)
		return when (resultCode) {
			Activity.RESULT_OK -> GoogleSignIn.getSignedInAccountFromIntent(intent)
			else -> {
				// For other result codes (e.g., CANCELED), return null
				Log.w("AuthResultContract", "Google Sign-In result was not OK. Result Code: $resultCode")
				null
			}
		}
	}
}