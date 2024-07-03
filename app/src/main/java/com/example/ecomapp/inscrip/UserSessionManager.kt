package com.example.ecomapp.inscrip

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(private val context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, 0)
    private val editor: SharedPreferences.Editor = pref.edit()

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "UserSessionPref"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val PRIVATE_MODE = 0
    }

    // Method to save login state
    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    // Method to check login status
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Method to clear login status
    fun logoutUser() {
        editor.clear()
        editor.apply()
    }
}
