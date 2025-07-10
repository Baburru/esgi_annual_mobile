package com.example.esgi_annual.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esgi_annual.R
import com.example.esgi_annual.model.Projet
import com.example.esgi_annual.model.ProjetDetails
import com.example.esgi_annual.model.GroupeDetails
import com.example.esgi_annual.model.DeliverableSubmission
import com.example.esgi_annual.model.Student
import com.example.esgi_annual.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LivrablesScreen(projet: Projet) {
    var projetDetails by remember { mutableStateOf<ProjetDetails?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val expandedGroup = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch project details (groups/deliverables) when component is created
    LaunchedEffect(projet.id) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                Log.d("LivrablesScreen", "Fetching project details with ID: ${projet.id}")
                val response = RetrofitInstance.api.getProjet(projet.id, true)
                projetDetails = response
                Log.d("LivrablesScreen", "Project details fetched successfully: ${response.name}")
            } catch (e: Exception) {
                Log.e("LivrablesScreen", "Error fetching project details", e)
                error = "Erreur lors du chargement du projet : ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(error!!, color = Color.Red)
            }
        }
        projetDetails == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Projet non trouvé", color = Color.Red)
            }
        }
        else -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("GESTION DES LIVRABLES", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF6C5CE7))
                Spacer(modifier = Modifier.height(16.dp))

                projetDetails!!.groups.forEach { group ->
                    val isExpanded = expandedGroup.value == group.name
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expandedGroup.value = if (isExpanded) null else group.name }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_home),
                                    contentDescription = null,
                                    tint = Color(0xFF636E72)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${group.name}", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(16.dp))
                                // Statut badge
                                val status = "En attente"
                                val statusColor = if (status == "Livré") Color(0xFF00B894) else Color(0xFF636E72)
                                Box(
                                    modifier = Modifier
                                        .background(statusColor, shape = RoundedCornerShape(12.dp))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(status, color = Color.White, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                val progress = 0.7f
                                val progressColor = when {
                                    progress >= 1f -> Color(0xFF00B894)
                                    progress > 0f -> Color(0xFFFFC300)
                                    else -> Color(0xFF636E72)
                                }
                                val statusText = if (progress >= 1f) "Livré" else "En attente"
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "${(progress * 100).toInt()}%",
                                        color = progressColor,
                                        fontSize = 12.sp,
                                        modifier = Modifier.width(32.dp)
                                    )
                                    LinearProgressIndicator(
                                        progress = progress,
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(6.dp),
                                        color = progressColor,
                                        trackColor = Color(0xFFE0E0E0)
                                    )
                                }
                            }
                            if (isExpanded) {
                                Surface(
                                    color = Color(0xFFF3F0FF),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("ÉLÈVES :", fontWeight = FontWeight.Bold, color = Color(0xFF6C5CE7))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            group.students.forEach { studentId ->
                                                val student: Student? = projet.students.find { it.id == studentId }
                                                Surface(
                                                    color = Color(0xFFE0E0E0),
                                                    shape = RoundedCornerShape(16.dp),
                                                    tonalElevation = 2.dp
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            "${student?.firstName ?: ""} ${student?.lastName ?: ""}",
                                                            color = Color(0xFF114B5F),
                                                            fontSize = 14.sp
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        // Tableau des livrables
                                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Nom du livrable :", fontWeight = FontWeight.Bold)
                                            Text("Date limite", fontWeight = FontWeight.Bold)
                                            Text("Statut", fontWeight = FontWeight.Bold)
                                            Text("Fichier soumis", fontWeight = FontWeight.Bold)
                                            Text("Vérifications", fontWeight = FontWeight.Bold)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        if (group.deliverables.isNotEmpty()) {
                                            group.deliverables.forEach { deliverable ->
                                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Icon(painter = painterResource(id = R.drawable.ic_document), contentDescription = null)
                                                    Text(formatDate(deliverable.date), fontSize = 12.sp)
                                                    Text(deliverable.status, color = getStatusColor(deliverable.status), fontSize = 12.sp)
                                                    Text(if (deliverable.file.isNotEmpty()) "Oui" else "Non", color = Color.Gray, fontSize = 12.sp)
                                                    Text("${deliverable.checksResult.folder + deliverable.checksResult.files + deliverable.checksResult.textSearch}/300", color = Color.Gray, fontSize = 12.sp)
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }
                                        } else {
                                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                                Text("Aucun livrable", color = Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yy à HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

private fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "finished" -> Color(0xFF00B894)
        "pending" -> Color(0xFFFFC300)
        "failed" -> Color(0xFFFF4136)
        else -> Color.Gray
    }
} 