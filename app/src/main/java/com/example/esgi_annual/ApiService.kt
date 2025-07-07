package com.example.esgi_annual

import com.example.esgi_annual.model.Projet
import com.example.esgi_annual.model.ProjectsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @GET("/teacher/projects/list")
    suspend fun getProjets(
        @Query("page") page: Int,
        @Query("resolveStudents") resolveStudents: Boolean = true
    ): ProjectsResponse
}

data class LoginRequest(val email: String, val password: String)

data class LoginResponse(
    val id: String?,      // This is the token
    val userId: String?,
    val type: String?,
    val ttl: String?
)

interface ApiLoginService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}