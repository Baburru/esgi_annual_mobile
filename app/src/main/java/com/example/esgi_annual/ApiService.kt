package com.example.esgi_annual

import com.example.esgi_annual.model.Projet
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @GET("projects/list")
    suspend fun getProjets(@Query("page") page: Int): List<Projet>
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val id: Int?, val token: String?)

interface ApiLoginService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}