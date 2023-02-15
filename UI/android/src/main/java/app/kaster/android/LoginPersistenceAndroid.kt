package app.kaster.android

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme
import androidx.security.crypto.MasterKey
import app.kaster.common.login.LoginInteractor
import app.kaster.common.login.LoginPersistence
import java.security.KeyStore

class LoginPersistenceAndroid(private val context: Context) : LoginPersistence {

    override fun saveCredentials(credentials: LoginInteractor.Credentials) {
        credentialsPrefs().edit {
            putString("username", credentials.username)
            putString("password", credentials.password)
        }
    }

    override fun loadCredentials(): LoginInteractor.Credentials? {
        val prefs = credentialsPrefs()
        val username = prefs.getString("username", null)
        val password = prefs.getString("password", null)
        return when {
            username == null || password == null -> null
            else -> LoginInteractor.Credentials(username, password)
        }
    }

    private val configPrefs = context.getSharedPreferences("loginConfig", Context.MODE_PRIVATE)

    override var userAuthenticationRequired: Boolean
        get() = configPrefs.getBoolean(KEY_USER_AUTH, false)
        set(value) = configPrefs.edit { putBoolean(KEY_USER_AUTH, value) }

    override fun clear() {
        deleteCredentialsPrefs()
        deleteMasterKey()
        userAuthenticationRequired = false
    }

    private fun deleteCredentialsPrefs() {
        context.deleteSharedPreferences(CRED_PREFS_FILENAME)
    }

    private fun deleteMasterKey() {
        KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
            deleteEntry(MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        }
    }

    private val masterKey: MasterKey
        get() = MasterKey.Builder(context, "Kaster")
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .setUserAuthenticationRequired(userAuthenticationRequired)
            .build()

    private fun credentialsPrefs(): SharedPreferences =
        EncryptedSharedPreferences.create(
            context,
            CRED_PREFS_FILENAME,
            masterKey,
            PrefKeyEncryptionScheme.AES256_SIV,
            PrefValueEncryptionScheme.AES256_GCM
        )

    companion object {
        private const val KEY_USER_AUTH = "userAuth"
        private const val CRED_PREFS_FILENAME = "credentials"
    }

}