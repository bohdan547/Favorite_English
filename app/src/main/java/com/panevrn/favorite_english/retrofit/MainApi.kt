package com.panevrn.favorite_english.retrofit


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): UserToken
}