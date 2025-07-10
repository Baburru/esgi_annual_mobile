package com.example.esgi_annual.model

data class ProjetDetails(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val status: String = "",
    val students: List<String> = emptyList(), // for details endpoint
    val groups: List<GroupeDetails> = emptyList(),
    val deliverablesList: List<Deliverable> = emptyList(),
    val startDate: String = "",
    val endDate: String = "",
    val options: ProjetOptionsDetails = ProjetOptionsDetails(),
    val metadata: ProjetMetadata = ProjetMetadata()
)

data class GroupeDetails(
    val name: String = "",
    val students: List<String> = emptyList(),
    val deliverables: List<DeliverableSubmission> = emptyList(),
    val passageTime: String = "",
    val report: String? = null
)

data class DeliverableSubmission(
    val id: String = "",
    val file: String = "",
    val deliverablesChecks: String = "",
    val status: String = "",
    val checksResult: ChecksResult = ChecksResult(),
    val date: String = ""
)

data class ChecksResult(
    val folder: Int = 0,
    val files: Int = 0,
    val textSearch: Int = 0
)

data class Deliverable(
    val id: String = "",
    val deadline: String = "",
    val name: String = "",
    val description: String = "",
    val allowOverDeadline: Boolean = true,
    val deadlineOptions: DeadlineOptions = DeadlineOptions(),
    val maxSize: Int = 0,
    val checks: Checks = Checks()
)

data class DeadlineOptions(
    val malus: Double = 0.0,
    val per: Int = 0
)

data class Checks(
    val directories: List<Directory> = emptyList(),
    val files: List<FileCheck> = emptyList(),
    val name: String = ""
)

data class Directory(
    val name: String = "",
    val files: List<FileCheck> = emptyList(),
    val directories: List<Directory> = emptyList()
)

data class FileCheck(
    val name: String = "",
    val caseSensitive: Boolean = true,
    val shouldContains: List<RegexCheck> = emptyList(),
    val bePresent: Boolean = true
)

data class RegexCheck(
    val regex: String = ""
)

data class ProjetOptionsDetails(
    val groupsEndDate: String = "",
    val groupsMaxStudents: Int = 0,
    val groupsMode: String = "",
    val reportTemplate: List<ReportTemplate> = emptyList()
)

data class ReportTemplate(
    val name: String = "",
    val description: String = ""
)

data class ProjetMetadata(
    val studentsMailSend: List<String> = emptyList()
) 