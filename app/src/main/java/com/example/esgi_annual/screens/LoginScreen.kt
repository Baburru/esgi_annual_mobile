package com.example.esgi_annual.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.esgi_annual.RetrofitInstance
import com.example.esgi_annual.ApiLoginService
import com.example.esgi_annual.LoginRequest
import com.example.esgi_annual.LoginResponse
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.util.Log
import androidx.compose.material3.CardDefaults

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("mathis@mail.fr") }
    var password by remember { mutableStateOf("azerty123") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    fun handleLogin() {
        coroutineScope.launch {
            Log.d("LoginScreen", "Attempting login for: $email")
            error = null
            loading = true
            try {
                val response = RetrofitInstance.apiLogin.login(LoginRequest(email, password))
                Log.d("LoginScreen", "Login response received: $response")
                if (response.id != null) {
                    Log.d("LoginScreen", "Login successful")
                    // Stockage du token (id)
                    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    prefs.edit().putString("token", response.id).apply()
                    onLoginSuccess()
                } else {
                    Log.e("LoginScreen", "Invalid response from API")
                    error = "Email ou mot de passe invalide. Veuillez réessayer."
                }
            } catch (e: Exception) {
                Log.e("LoginScreen", "Login error", e)
                error = "Email ou mot de passe invalide. Veuillez réessayer."
            } finally {
                loading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(min = 300.dp, max = 400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Bienvenue à votre", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = Color.Black)
                Text("Portail de projets pédagogiques", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(24.dp))
                if (error != null) {
                    Text(error!!, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
                }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { handleLogin() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Connexion")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("ou continuer avec", fontSize = 12.sp, color = Color.Gray)
                // TODO: Ajouter GoogleSignInButton si besoin
            }
        }
    }
} 