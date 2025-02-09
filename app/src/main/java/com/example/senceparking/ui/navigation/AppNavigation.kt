package com.example.senceparking.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.senceparking.ui.home.HomeScreen
import com.example.senceparking.ui.login.LoginScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = "login"){
        composable("login") { LoginScreen(navController) }
        composable("home"){ HomeScreen(navController) }
    }
}