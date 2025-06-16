package com.example.esgi_annual.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.esgi_annual.R
import com.example.esgi_annual.model.Projet
import com.example.esgi_annual.model.Etudiant

@Composable
fun ListeProjetsScreen(
    projets: List<Projet>,
    onProjetClick: (Projet) -> Unit
) {
    var projetParticipants by remember { mutableStateOf<Projet?>(null) }

    Column(modifier = Modifier.padding(8.dp)) {
        projets.forEach { projet ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFC0DFE3)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_home),
                                contentDescription = "Avatar projet",
                                tint = Color(0xFF114B5F),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(3f)) {
                            Text(
                                "4ème année ingénierie du web",
                                fontSize = 12.sp,
                                color = Color(0xFF114B5F),
                                lineHeight = 12.sp
                            )
                            Text(
                                projet.titre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF114B5F),
                                lineHeight = 16.sp
                            )
                        }
                        // Statut
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .weight(2f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val (statutColor, statutText, statutIcon) = when (projet.statut) {
                                    "Active" -> Triple(Color(0xFF2ECC40), "Active", R.drawable.ic_check_circle)
                                    "Clôturée" -> Triple(Color(0xFFFF4136), "Clôturée", R.drawable.ic_close)
                                    "Programmée" -> Triple(Color(0xFFFFDC00), "Programmée", R.drawable.ic_schedule)
                                    else -> Triple(Color.Gray, projet.statut, R.drawable.ic_home)
                                }
                                Text(statutText, color = statutColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    painter = painterResource(id = statutIcon),
                                    contentDescription = null,
                                    tint = statutColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Infos principales
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("• ", fontSize = 18.sp, color = Color(0xFF114B5F))
                                Text(
                                    "Date de creation : ${projet.dateCreation}",
                                    fontSize = 14.sp,
                                    color = Color(0xFF114B5F)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("• ", fontSize = 18.sp, color = Color(0xFF114B5F))
                                Text(
                                    "Date de rendu : ${projet.dateRendu}",
                                    fontSize = 14.sp,
                                    color = Color(0xFF114B5F)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${projet.participants} Participants", fontSize = 14.sp, color = Color(0xFF114B5F))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${projet.groupes} Groupes", fontSize = 14.sp, color = Color(0xFF114B5F))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Boutons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End)
                    ) {
                        OutlinedButton(
                            onClick = { projetParticipants = projet },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF114B5F))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = "Participants",
                                tint = Color(0xFF114B5F),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Participants", color = Color(0xFF114B5F), fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onProjetClick(projet) },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF114B5F))
                        ) {
                            Text("Voir plus", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Affichage du dialog participants
    projetParticipants?.let { projet ->
        AlertDialog(
            onDismissRequest = { projetParticipants = null },
            title = { Text("Participants") },
            text = {
                val groupes = projet.etudiants.groupBy { it.groupe }.toSortedMap()
                Column(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    if (projet.etudiants.isEmpty()) {
                        Text("Aucun étudiant")
                    } else {
                        groupes.forEach { (groupe, etudiants) ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Groupe $groupe :", fontWeight = FontWeight.Bold, color = Color(0xFF114B5F))
                            Spacer(modifier = Modifier.height(4.dp))
                            etudiants.forEachIndexed { idx, etu ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp, bottom = if (idx == etudiants.lastIndex) 0.dp else 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFC0DFE3)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = etu.nom.split(" ").joinToString("") { it.first().toString() }.uppercase(),
                                            color = Color(0xFF114B5F),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(etu.nom)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { projetParticipants = null }) {
                    Text("Fermer")
                }
            }
        )
    }
} 