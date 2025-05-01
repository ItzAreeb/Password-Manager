package com.example.passwordmanager.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object EncryptionHelper {
    private const val KEYSTORE_ALIAS = "PasswordManager_AES_Key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val FILE_NAME = "encrypted_accounts.json"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_LENGTH = 12 // 12 bytes for GCM

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }

        if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )

            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEYSTORE_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build()
            )

            return keyGenerator.generateKey()
        }

        return keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
    }

    fun saveAccounts(context: Context, accounts: List<Account>) {
        val secretKey = getOrCreateSecretKey()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Get IV for later decryption
        val iv = cipher.iv

        // Convert accounts to JSON
        val jsonString = Gson().toJson(accounts)
        val encryptedBytes = cipher.doFinal(jsonString.toByteArray(StandardCharsets.UTF_8))

        // Combine IV and encrypted data
        val outputStream = ByteArrayOutputStream()
        outputStream.write(iv)
        outputStream.write(encryptedBytes)

        // Save to encrypted file
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val encryptedFile = EncryptedFile.Builder(
            context,
            File(context.filesDir, FILE_NAME),
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileOutput().use { output ->
            output.write(outputStream.toByteArray())
        }
    }

    fun loadAccounts(context: Context): List<Account> {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val encryptedFile = EncryptedFile.Builder(
                context,
                File(context.filesDir, FILE_NAME),
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val inputStream = encryptedFile.openFileInput()
            val bytes = inputStream.readBytes()

            if (bytes.isEmpty()) return emptyList()

            // Extract IV (first 12 bytes)
            val iv = bytes.copyOfRange(0, IV_LENGTH)
            val encryptedData = bytes.copyOfRange(IV_LENGTH, bytes.size)

            // Decrypt data
            val secretKey = getOrCreateSecretKey()
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedBytes = cipher.doFinal(encryptedData)
            val jsonString = String(decryptedBytes, StandardCharsets.UTF_8)

            val type = object : TypeToken<List<Account>>() {}.type
            return Gson().fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            // File doesn't exist or corrupted, return empty list
            return emptyList()
        }
    }
}