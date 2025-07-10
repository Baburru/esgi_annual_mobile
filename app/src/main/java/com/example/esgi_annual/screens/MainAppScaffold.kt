package com.example.esgi_annual.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.esgi_annual.R
import com.example.esgi_annual.model.Projet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(
    projet: Projet,
    onBack: () -> Unit,
    livrablesScreen: @Composable () -> Unit,
    rapportScreen: @Composable () -> Unit,
    notationScreen: @Composable () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Color(0xFF114B5F),
        selectedTextColor = Color(0xFF114B5F),
        indicatorColor = Color(0xFFC0DFE3),
        unselectedIconColor = Color(0xFF114B5F),
        unselectedTextColor = Color(0xFF114B5F)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(projet.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFC0DFE3)
            ) {
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_home), contentDescription = "Livrables") },
                    label = { Text("Livrables") },
                    selected = currentRoute == "livrables",
                    onClick = {
                        navController.navigate("livrables") {
                            popUpTo(navController.graph.startDestinationRoute ?: "livrables") {
                                inclusive = true
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    colors = navigationBarItemColors
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_document), contentDescription = "Rapport") },
                    label = { Text("Rapport d'activité") },
                    selected = currentRoute == "rapport",
                    onClick = {
                        navController.navigate("rapport") {
                            popUpTo(navController.graph.startDestinationRoute ?: "livrables") {
                                inclusive = true
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    colors = navigationBarItemColors
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.ic_grid), contentDescription = "Notation") },
                    label = { Text("Notation") },
                    selected = currentRoute == "notation",
                    onClick = {
                        navController.navigate("notation") {
                            popUpTo(navController.graph.startDestinationRoute ?: "livrables") {
                                inclusive = true
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    colors = navigationBarItemColors
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "livrables",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("livrables") {
                livrablesScreen()
            }
            composable("rapport") {
                rapportScreen()
            }
            composable("notation") {
                notationScreen()
            }
        }
    }
} 