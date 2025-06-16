package com.example.esgi_annual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import com.example.esgi_annual.model.Projet
import com.example.esgi_annual.model.Etudiant
import com.example.esgi_annual.screens.ListeProjetsScreen
import com.example.esgi_annual.screens.MainAppScaffold
import com.example.esgi_annual.screens.LivrablesScreen
import com.example.esgi_annual.screens.RapportScreen
import com.example.esgi_annual.screens.NotationScreen
import com.example.esgi_annual.ui.theme.ESGI_ANNUALTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ESGI_ANNUALTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var projetSelectionne by remember { mutableStateOf<Projet?>(null) }
    val projets = remember {
        listOf(
            Projet(
                1,
                "Projet Annuel anne Ingenierie du Web",
                "Active",
                "14/01/2025",
                "22/02/2025",
                8,
                3,
                etudiants = listOf(
                    Etudiant("Alice Dupont", 1),
                    Etudiant("Bob Martin", 1),
                    Etudiant("Charlie Durand", 1),
                    Etudiant("David Bernard", 2),
                    Etudiant("Emma Leroy", 2),
                    Etudiant("Fanny Petit", 2),
                    Etudiant("Gabriel Moreau", 3),
                    Etudiant("Hugo Blanc", 3)
                )
            ),
            Projet(
                2,
                "Projet Annuel anne Ingenierie du Web",
                "Clôturée",
                "14/01/2025",
                "22/02/2025",
                8,
                3,
                etudiants = listOf(
                    Etudiant("Isabelle Roux", 1),
                    Etudiant("Julien Fabre", 1),
                    Etudiant("Kevin Simon", 2),
                    Etudiant("Laura Girard", 2),
                    Etudiant("Manon Lefevre", 3)
                )
            ),
            Projet(
                3,
                "Projet Annuel anne Ingenierie du Web",
                "Programmée",
                "14/01/2025",
                "22/02/2025",
                8,
                3,
                etudiants = listOf(
                    Etudiant("Nina Perrin", 1),
                    Etudiant("Olivier Marchand", 1),
                    Etudiant("Pauline Robert", 2),
                    Etudiant("Quentin Dubois", 2),
                    Etudiant("Romain Gauthier", 3)
                )
            )
        )
    }
    if (projetSelectionne == null) {
        ListeProjetsScreen(projets) { projet ->
            projetSelectionne = projet
        }
    } else {
        BackHandler {
            projetSelectionne = null
        }
        MainAppScaffold(
            projet = projetSelectionne!!,
            onBack = { projetSelectionne = null },
            livrablesScreen = { LivrablesScreen() },
            rapportScreen = { RapportScreen() },
            notationScreen = { NotationScreen() }
        )
    }
}