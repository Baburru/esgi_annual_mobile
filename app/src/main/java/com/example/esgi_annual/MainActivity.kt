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
import com.example.esgi_annual.screens.LoginScreen
import com.example.esgi_annual.ui.theme.ESGI_ANNUALTheme
import kotlinx.coroutines.launch
import com.example.esgi_annual.RetrofitInstance
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.esgi_annual.model.ProjetDetails

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the application context for RetrofitInstance
        RetrofitInstance.appContext = applicationContext
        // Always log out the user on app start
        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit().remove("token").apply()
        setContent {
            ESGI_ANNUALTheme {
                MainActivityContent()
            }
        }
    }
}

@Composable
fun MainActivityContent() {
    val context = LocalContext.current
    var isLoggedIn by remember { mutableStateOf(false) }

    // Vérifie si un token est stocké (SharedPreferences)
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        isLoggedIn = prefs.getString("token", null) != null
    }

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = {
            isLoggedIn = true
        })
    } else {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    var projetSelectionne by remember { mutableStateOf<Projet?>(null) }
    var projets by remember { mutableStateOf<List<Projet>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                error = null
                val response = RetrofitInstance.api.getProjets(0, true) // page 1 par défaut, resolveStudents
                projets = response.results
            } catch (e: Exception) {
                error = "Erreur lors du chargement des projets : ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    if (projetSelectionne == null) {
        when {
            isLoading -> {
                androidx.compose.material3.CircularProgressIndicator()
            }
            error != null -> {
                androidx.compose.material3.Text(error!!, color = androidx.compose.ui.graphics.Color.Red)
            }
            else -> {
                ListeProjetsScreen(projets) { projet ->
                    projetSelectionne = projet
                }
            }
        }
    } else {
        BackHandler {
            projetSelectionne = null
        }
        MainAppScaffold(
            projet = projetSelectionne!!,
            onBack = { projetSelectionne = null },
            livrablesScreen = { LivrablesScreen(projet = projetSelectionne!!) },
            rapportScreen = { RapportScreen(projet = projetSelectionne!!) },
            notationScreen = { NotationScreen() }
        )
    }
}