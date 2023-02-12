package app.kaster.android

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme
import androidx.security.crypto.MasterKey
import app.kaster.common.login.Biometrics
import app.kaster.common.login.LoginInteractor
import app.kaster.common.login.LoginInteractor.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.KeyStore

class LoginInteractorAndroid(
    private val context: Context,
    private val biometrics: Biometrics,
    private val coroutineScope: CoroutineScope
) : LoginInteractor {

    private val configPrefs = context.getSharedPreferences("loginConfig", Context.MODE_PRIVATE)

    private var userAuthenticationRequired: Boolean
        get() = configPrefs.getBoolean(KEY_USER_AUTH, false)
        set(value) = configPrefs.edit { putBoolean(KEY_USER_AUTH, value) }

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Locked)

    override val loginState: StateFlow<LoginState> = _loginState

    override fun save(credentials: LoginInteractor.Credentials, requireUserAuth: Boolean) {
        coroutineScope.launch {
            clear() // TODO actually needed?
            if (!requireUserAuth || biometrics.promptUserAuth() == Biometrics.AuthResult.Success) {
                userAuthenticationRequired = requireUserAuth
                saveCredentials(credentials)
                _loginState.value = LoginState.LoggedIn(credentials)
            }
        }
    }

    override fun unlock() {
        coroutineScope.launch {
            if (userAuthenticationRequired && biometrics.promptUserAuth() != Biometrics.AuthResult.Success) {
                _loginState.value = LoginState.UnlockFailed
            } else {
                _loginState.value = when (val credentials = loadCredentials()) {
                    null -> LoginState.LoggedOut
                    else -> LoginState.LoggedIn(credentials)
                }
            }
        }
    }

    override fun clear() {
        deleteCredentialsPrefs()
        deleteMasterKey()
        userAuthenticationRequired = false
        _loginState.value = LoginState.LoggedOut
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

    private fun saveCredentials(credentials: LoginInteractor.Credentials) {
        credentialsPrefs().edit {
            putString("username", credentials.username)
            putString("password", credentials.password)
        }

    }

    private fun loadCredentials(): LoginInteractor.Credentials? {
        val prefs = credentialsPrefs()
        val username = prefs.getString("username", null)
        val password = prefs.getString("password", null)
        return when {
            username == null || password == null -> null
            else -> LoginInteractor.Credentials(username, password)
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