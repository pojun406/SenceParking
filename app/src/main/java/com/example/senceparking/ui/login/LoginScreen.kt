package com.example.senceparking.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.senceparking.core.GoogleAuthUiClient
import com.example.senceparking.ui.components.GoogleSignInButton

@Composable
fun LoginScreen(
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Google 로그인", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        // ✅ `GoogleSignInButton`만 남기고 불필요한 버튼 제거
        GoogleSignInButton(navController, googleAuthUiClient)
    }
}