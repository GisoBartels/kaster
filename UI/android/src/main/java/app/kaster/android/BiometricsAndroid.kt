package app.kaster.android

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import app.kaster.common.login.Biometrics
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BiometricsAndroid(private val fragmentActivity: FragmentActivity) : Biometrics {

    private val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    override suspend fun promptUserAuth(): Biometrics.AuthResult = suspendCancellableCoroutine { continuation ->
        val biometricPrompt = BiometricPrompt(fragmentActivity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    continuation.resume(Biometrics.AuthResult.Failed)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    continuation.resume(Biometrics.AuthResult.Success)
                }

                override fun onAuthenticationFailed() {
                    continuation.resume(Biometrics.AuthResult.Failed)
                }
            })

        biometricPrompt.authenticate(promptInfo)

        continuation.invokeOnCancellation {
            biometricPrompt.cancelAuthentication()
        }
    }
}