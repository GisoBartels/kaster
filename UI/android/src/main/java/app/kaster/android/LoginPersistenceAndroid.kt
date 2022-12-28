package app.kaster.android

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme
import androidx.security.crypto.MasterKey
import app.kaster.common.login.LoginPersistence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class LoginPersistenceAndroid(context: Context, coroutineScope: CoroutineScope) : LoginPersistence {

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

    override val credentials: MutableStateFlow<LoginPersistence.Credentials?> = MutableStateFlow(loadCredentials())

    init {
        coroutineScope.launch {
            credentials.filterNotNull().collectLatest {
                prefs.edit {
                    putString("username", it.username)
                    putString("password", it.password)
                }
            }
        }
    }

    private fun loadCredentials(): LoginPersistence.Credentials? {
        val username = prefs.getString("username", null)
        val password = prefs.getString("password", null)
        return when {
            username == null || password == null -> null
            else -> LoginPersistence.Credentials(username, password)
        }
    }

}