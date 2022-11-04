package app.kaster.android

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme
import androidx.security.crypto.MasterKey
import app.kaster.common.login.LoginPersistence

class LoginPersistenceAndroid(context: Context) : LoginPersistence {

    private val masterKey = MasterKey.Builder(context, "Kaster")
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "credentials",
        masterKey,
        PrefKeyEncryptionScheme.AES256_SIV,
        PrefValueEncryptionScheme.AES256_GCM
    )

    override fun storeCredentials(username: String, password: String) {
        prefs.edit {
            putString("username", username)
            putString("password", password)
        }
    }

    override fun loadUsername(): String = prefs.getString("username", null) ?: ""

    override fun loadMasterPassword(): String = prefs.getString("password", null) ?: ""

}