package com.example.senceparking.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(private val activity: Activity) { // ✅ Activity를 받는 하나의 클래스만 유지
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val signInClient: SignInClient = Identity.getSignInClient(activity)
    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(
        activity, GoogleSignInOptions.DEFAULT_SIGN_IN
    )

    // ✅ Google 로그인 인텐트 반환 함수 추가
    fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    suspend fun launchGoogleSignIn(): Boolean {
        return try {
            val signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(activity.getString(com.example.senceparking.R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
                )
                .build()

            val result = signInClient.beginSignIn(signInRequest).await()

            result?.pendingIntent?.let { pendingIntent ->
                activity.startIntentSenderForResult(
                    pendingIntent.intentSender,
                    1001,  // 요청 코드
                    null,
                    0, 0, 0
                )
            } ?: throw Exception("PendingIntent is null")

            firebaseAuth.currentUser != null
            true
        } catch (e: ApiException) {
            Log.e("GoogleAuthUiClient", "Google Sign-In API 오류 발생", e)
            Toast.makeText(activity, "Google 계정이 등록되지 않았습니다.", Toast.LENGTH_SHORT).show()
            false
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Google Sign-In 요청 실패", e)
            Toast.makeText(activity, "Google 로그인 요청 실패", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        signInClient.signOut()
    }

    fun handleSignInResult(data: Intent?, onResult: (Boolean) -> Unit) {
        try {
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                firebaseAuthWithGoogle(idToken, onResult)
            } else {
                Toast.makeText(activity, "Google 로그인 실패", Toast.LENGTH_SHORT).show()
                onResult(false)
            }
        } catch (e: ApiException) {
            Log.e("GoogleAuthUiClient", "Google 로그인 실패", e)
            onResult(false)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(activity, "환영합니다, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
                    onResult(true)
                } else {
                    Toast.makeText(activity, "Firebase 인증 실패", Toast.LENGTH_SHORT).show()
                    onResult(false)
                }
            }
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}