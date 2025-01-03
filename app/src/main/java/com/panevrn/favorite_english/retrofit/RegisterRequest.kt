package com.panevrn.favorite_english.retrofit

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
)
