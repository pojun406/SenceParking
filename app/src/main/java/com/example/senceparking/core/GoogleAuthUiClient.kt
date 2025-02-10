import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.senceparking.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(private val context: Context) {
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * ✅ Google 로그인 UI 실행을 위한 인텐트 생성
     */
    suspend fun getSignInIntent(): PendingIntent? {
        return try {
            val signInRequest = BeginSignInRequest.Builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(context.getString(R.string.OAuth)) // ✅ OAuth 클라이언트 ID 가져오기
                        .setFilterByAuthorizedAccounts(false) // 모든 계정 허용
                        .build()
                )
                .setAutoSelectEnabled(true) // 자동 선택 가능
                .build()

            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            signInResult.pendingIntent
        } catch (e: Exception) {
            Log.e("GoogleAuthUiClient", "Error getting Google Sign-In Intent", e)
            null
        }
    }

    /**
     * ✅ 로그인 결과 처리 및 Firebase 인증 연동
     */
    suspend fun handleSignInResult(data: Intent?): Boolean {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken

            if (idToken != null) {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential).await()
                Log.d("GoogleAuthUiClient", "Firebase 로그인 성공: ${auth.currentUser?.displayName}")
                return true
            }
            false
        } catch (e: ApiException) {
            Log.e("GoogleAuthUiClient", "Google 로그인 실패", e)
            false
        }
    }

    /**
     * ✅ 로그아웃 처리
     */
    fun signOut() {
        auth.signOut()
        oneTapClient.signOut()
        Log.d("GoogleAuthUiClient", "로그아웃 완료")
    }
}
