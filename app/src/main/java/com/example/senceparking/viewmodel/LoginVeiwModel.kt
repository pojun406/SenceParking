package com.example.senceparking.viewmodel

import GoogleAuthUiClient
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val googleAuthUiClient = GoogleAuthUiClient(application.applicationContext)

    /**
     * ✅ Google 로그인 UI를 실행하기 위한 Intent 반환
     */
    suspend fun getSignInIntent(): Intent? {
        return googleAuthUiClient.getSignInIntent()
    }

    /**
     * ✅ Google 로그인 결과 처리 및 Firebase 인증
     */
    fun handleSignInResult(data: Intent?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = googleAuthUiClient.handleSignInResult(data)
            onResult(success) // 로그인 성공 여부를 콜백으로 반환
        }
    }
}
