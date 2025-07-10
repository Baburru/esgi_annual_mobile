package com.example.esgi_annual.model

data class Rapport(
    val id: String,
    val parts: List<RapportPart>,
    val date: String
)

data class RapportPart(
    val content: RapportContent,
    val modified: String,
    val date: String
)

data class RapportContent(
    val type: String, // "title", "subtitle", "text"
    val content: String
) 