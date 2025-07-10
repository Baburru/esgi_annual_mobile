package com.example.esgi_annual

import com.example.esgi_annual.model.Projet
import com.example.esgi_annual.model.ProjectsResponse
import com.example.esgi_annual.model.ProjetDetails // import the details model
import com.example.esgi_annual.model.Rapport
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/teacher/projects/list")
    suspend fun getProjets(
        @Query("page") page: Int,
        @Query("resolveStudents") resolveStudents: Boolean = true
    ): ProjectsResponse

    @GET("/teacher/projects/{projectId}")
    suspend fun getProjet(
        @Path("projectId") projectId: String,
        @Query("resolveDeliverables") resolveDeliverables: Boolean = true
    ): ProjetDetails // use ProjetDetails here

    @GET("/teacher/reports/{reportId}")
    suspend fun getRapport(
        @Path("reportId") reportId: String
    ): Rapport
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