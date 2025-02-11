package com.example.senceparking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.senceparking.core.GoogleAuthUiClient
import com.example.senceparking.ui.login.LoginScreen
import com.example.senceparking.ui.navigation.AppNavigation
import com.example.senceparking.ui.theme.SenceParkingTheme
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            googleAuthUiClient = GoogleAuthUiClient(this) // ✅ 한 번만 생성

            if (googleAuthUiClient.isUserLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return
            }

            setContent {
                val navController = rememberNavController() // ✅ NavController 생성

                SenceParkingTheme {
                    AppNavigation(navController, googleAuthUiClient) // ✅ `LoginScreen`이 아니라 `AppNavigation` 사용
                }
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "GoogleAuthUiClient 초기화 오류", e)
            Toast.makeText(this, "로그인 시스템 오류", Toast.LENGTH_SHORT).show()
        }
    }
}