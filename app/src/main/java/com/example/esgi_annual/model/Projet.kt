package com.example.esgi_annual.model

data class Student(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val status: String = "",
    val email: String = "",
    val metadata: StudentMetadata = StudentMetadata(),
    val type: String = ""
)

data class StudentMetadata(
    val createdBy: String = "",
    val creationDate: String = ""
)

data class Projet(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: String = "",
    val students: List<Student> = emptyList(), // for list endpoint
    val groups: List<Groupe> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val options: ProjetOptions = ProjetOptions()
)

data class Groupe(
    val name: String = "",
    val students: List<Student> = emptyList(),
    val deliverables: List<String> = emptyList(),
    val passageTime: String = "",
    val report: String? = null
)

data class ProjetOptions(
    val groupsEndDate: String = "",
    val groupsMaxStudents: Int = 0,
    val groupsMode: String = ""
)

data class ProjectsResponse(
    val results: List<Projet>,
    val totalCount: Int,
    val resultsPerPage: Int
) 