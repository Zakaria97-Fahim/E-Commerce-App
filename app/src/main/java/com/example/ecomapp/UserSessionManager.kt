package com.example.ecomapp

import android.content.Context

class UserSessionManager(val context: Context) {

    // remember login of the customer
    private val pref   = context.getSharedPreferences(PREF_NAME, 0)
    private val editor = pref.edit()

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "UserSessionPref"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
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
