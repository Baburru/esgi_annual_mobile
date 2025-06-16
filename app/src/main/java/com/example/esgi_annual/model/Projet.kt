package com.example.esgi_annual.model

data class Projet(
    val id: Int,
    val titre: String,
    val statut: String,
    val dateCreation: String,
    val dateRendu: String,
    val participants: Int,
    val groupes: Int,
    val etudiants: List<Etudiant> = emptyList()
) 