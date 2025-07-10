package com.example.esgi_annual.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.esgi_annual.model.Projet
import com.example.esgi_annual.model.Groupe
import com.example.esgi_annual.model.Rapport
import com.example.esgi_annual.model.Student
import com.example.esgi_annual.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun RapportScreen(projet: Projet) {
    val groupes = projet.groups
    var selectedGroup by remember { mutableStateOf<Groupe?>(null) }
    var rapport by remember { mutableStateOf<Rapport?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(Modifier.padding(12.dp)) {
        groupes.forEach { group ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            group.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF114B5F)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            group.students.joinToString(", ") { student ->
                                listOfNotNull(student.lastName, student.firstName).joinToString(" ")
                            },
                            color = Color(0xFF114B5F),
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    if (group.report == null) {
                        Text(
                            "Pas de rapport disponible",
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF114B5F),
                            fontSize = 15.sp
                        )
                    } else {
                        Button(
                            onClick = {
                                isDialogOpen = true
                                isLoading = true
                                error = null
                                rapport = null
                                selectedGroup = group
                                coroutineScope.launch {
                                    try {
                                        val result = RetrofitInstance.api.getRapport(group.report!!)
                                        rapport = result
                                    } catch (e: Exception) {
                                        error = "Erreur lors du chargement du rapport"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = BorderStroke(0.dp, Color.Transparent),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            elevation = null
                        ) {
                            Text(
                                "Visualizer le rapport",
                                color = Color(0xFF114B5F),
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Filled.ArrowForward,
                                contentDescription = "Voir rapport",
                                tint = Color(0xFF114B5F)
                            )
                        }
                    }
                }
            }
        }
    }

    if (isDialogOpen) {
        Dialog(onDismissRequest = { isDialogOpen = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 300.dp),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp
            ) {
                Column(Modifier.padding(24.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedGroup?.name ?: "Rapport", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
                        IconButton(onClick = { isDialogOpen = false }) {
                            Icon(Icons.Filled.Close, contentDescription = "Fermer")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    when {
                        isLoading -> CircularProgressIndicator()
                        error != null -> Text(error!!, color = Color.Red)
                        rapport != null -> RapportContentView(rapport!!)
                        else -> Text("Aucun rapport")
                    }
                }
            }
        }
    }
}

@Composable
fun RapportContentView(rapport: Rapport) {
    Column {
        rapport.parts.forEach { part ->
            when (part.content.type) {
                "title" -> Text(
                    part.content.content,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                "subtitle" -> Text(
                    part.content.content,
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                "text" -> Text(
                    part.content.content,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
} 