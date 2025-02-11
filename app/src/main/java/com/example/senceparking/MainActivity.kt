package com.example.senceparking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.senceparking.core.GoogleAuthUiClient
import com.example.senceparking.ui.navigation.AppNavigation
import com.example.senceparking.ui.theme.SenceParkingTheme

class MainActivity : ComponentActivity() {
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleAuthUiClient = GoogleAuthUiClient(this)

        if (!googleAuthUiClient.isUserLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setContent {
            val navController = rememberNavController() // ✅ NavController 생성
            val googleAuthUiClient = GoogleAuthUiClient(this) // ✅ GoogleAuthUiClient 생성

            SenceParkingTheme {
                AppNavigation(navController, googleAuthUiClient) // ✅ 올바르게 호출
            }
        }
    }
}

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "메인 화면")
    }
}