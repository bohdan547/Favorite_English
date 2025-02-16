package com.panevrn.favorite_english.retrofit


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<UserToken>

    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<UserToken>
}