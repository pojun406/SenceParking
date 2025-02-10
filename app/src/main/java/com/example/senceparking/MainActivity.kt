package com.example.senceparking

import GoogleAuthUiClient
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.senceparking.ui.home.HomeScreen
import com.example.senceparking.ui.login.LoginScreen
//import com.example.senceparking.ui.navigation.AppNavigation
import com.example.senceparking.ui.theme.SenceParkingTheme
import com.example.senceparking.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            SenceParkingTheme {
                val navController = rememberNavController()
                AppNavigation(navController) { // ✅ onSignIn 추가
                    lifecycleScope.launch {
                        startGoogleSignIn()
                    }
                }
            }
        }
    }

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            loginViewModel.handleSignInResult(result.data) { success ->
                if (success) {
                    Log.d("MainActivity", "로그인 성공!")
                } else {
                    Log.e("MainActivity", "로그인 실패!")
                }
            }
        }

    suspend fun startGoogleSignIn() {
        val signInIntent = loginViewModel.getSignInIntent()
        if (signInIntent == null) {
            Log.e("MainActivity", "Google Sign-In Intent is null! Check GoogleAuthUiClient.")
            return
        }
        signInIntent.let { signInLauncher.launch(it) }
    }

    @Composable
    fun AppNavigation(
        navController: NavHostController,
        onSignIn: () -> Unit // ✅ 로그인 실행을 위한 함수 전달
    ) {
        NavHost(navController, startDestination = "login") {
            composable(route = "login") {
                LoginScreen(navController, onSignIn) // ✅ LoginScreen에 전달
            }
            composable(route = "home") {
                HomeScreen(navController)
            }
        }
    }
}