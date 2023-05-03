package app.passwordkaster.logic.login

import app.passwordkaster.logic.login.LoginInteractor.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

class LoginInteractorBiometrics(
    private val loginPersistence: LoginPersistence,
    private val biometrics: Biometrics,
    private val coroutineScope: CoroutineScope
) : LoginInteractor {
    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Locked)
    override val loginState: StateFlow<LoginState> = _loginState

    override val biometricsSupported: Boolean
        get() = biometrics.isSupported

    override fun save(credentials: LoginInteractor.Credentials, requireUserAuth: Boolean) {
        coroutineScope.launch {
            if (!requireUserAuth || biometrics.promptUserAuth() == Biometrics.AuthResult.Success) {
                loginPersistence.userAuthenticationRequired = requireUserAuth
                loginPersistence.saveCredentials(credentials)
                _loginState.value = LoginState.LoggedIn(credentials)
                startLogoutTimer()
            }
        }
    }

    override fun unlock() {
        coroutineScope.launch {
            if (loginPersistence.userAuthenticationRequired && biometrics.promptUserAuth() != Biometrics.AuthResult.Success) {
                _loginState.value = LoginState.UnlockFailed
            } else {
                _loginState.value = when (val credentials = loginPersistence.loadCredentials()) {
                    null -> LoginState.LoggedOut
                    else -> LoginState.LoggedIn(credentials).also { startLogoutTimer() }
                }
            }
        }
    }

    private var logoutTimerJob: Job? = null
    private fun startLogoutTimer() {
        logoutTimerJob?.cancel()
        logoutTimerJob = coroutineScope.launch {
            delay(5.minutes)
            if (!loginPersistence.userAuthenticationRequired) loginPersistence.clear()
            _loginState.value = LoginState.Locked
        }
    }

    override fun clear() {
        loginPersistence.clear()
        _loginState.value = LoginState.LoggedOut
    }
}