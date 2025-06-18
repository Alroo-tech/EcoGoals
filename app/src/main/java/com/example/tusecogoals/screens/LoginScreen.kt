// ui/screens/LoginScreen.kt
package com.example.tusecogoals.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tusecogoals.R
import com.example.tusecogoals.ui.theme.PrimaryColor
import com.example.tusecogoals.ui.theme.SeaFoamGreen
import com.example.tusecogoals.viewmodels.HomeViewModel
import com.example.tusecogoals.viewmodels.HomeViewModelFactory
import com.google.firebase.auth.FirebaseAuth

/**
 * File: LoginScreen.kt
 * Description: This file contains the LoginScreen composable for logging in
 *
 * Author: Alan Rooney
 * Date: 19/12/2024
 */
@Composable
fun LoginScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(application)
    )

    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Center the Tus Logo at the top of the screen
        Image(
            painter = painterResource(id = R.drawable.tuslogo),
            contentDescription = "Tus Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField with SeaFoamGreen background and white text
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = PrimaryColor,
                focusedTextColor = Color.White, // Corrected from focusedTextColor to textColor
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(autoCorrect = false)
        )
        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = PrimaryColor,
                focusedTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                cursorColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(autoCorrect = false)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            homeViewModel.fetchUser() // Refresh user data
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = task.exception?.message ?: "Authentication failed."
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text("Login", color = Color.White)
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.navigate("signup") }) {
            Text(
                "Don't have an account? Sign Up",
                color = Color.White
            )
        }
    }
}
