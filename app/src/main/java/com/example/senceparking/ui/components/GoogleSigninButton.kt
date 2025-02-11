package com.example.senceparking.ui.components

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.senceparking.R
import com.example.senceparking.core.GoogleAuthUiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInButton(navController: NavController, googleAuthUiClient: GoogleAuthUiClient) {
    val context = LocalContext.current // @Composable 내부에서 context 가져오기

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                val activity = context as? Activity
                activity?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        val success = googleAuthUiClient.launchGoogleSignIn() // Boolean 반환
                        if (success) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true } // 로그인 화면 제거
                            }
                        }
                    }
                }
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.mipmap.google_logo),
            contentDescription = "Google Sign In",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Google 로그인", fontSize = 16.sp, color = Color.Black)
    }
}
