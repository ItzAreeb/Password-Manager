package com.example.passwordmanager.ui.security

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class SecurityManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_security", Context.MODE_PRIVATE)
    private val executor: Executor = ContextCompat.getMainExecutor(context)

    fun isSecurityEnabled(): Boolean = prefs.getBoolean("security_enabled", false)

    fun setSecurityEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("security_enabled", enabled).apply()
    }

    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!isSecurityEnabled()) {
            onSuccess()
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Password Manager")
            .setSubtitle("Authenticate to access your passwords")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    fun setupSecurity(activity: Activity, onComplete: () -> Unit) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Set up app security")
            .setSubtitle("Register your fingerprint, PIN, or pattern")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(activity as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    setSecurityEnabled(true)
                    prefs.edit().putBoolean("security_set", true).apply()
                    onComplete()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }
}