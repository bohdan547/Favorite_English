package com.panevrn.favorite_english

import android.content.Context

class UserPreferences (context: Context) {
    private val sharedPref = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    // Зберігаємо статус авторизації
    fun saveUserData(isLoggedIn: Boolean) {
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    // Перевіряємо, чи користувач авторизований
    fun isUserLoggedIn(): Boolean {
        return sharedPref.getBoolean("isLoggedIn", false)
    }
}