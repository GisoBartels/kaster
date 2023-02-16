package app.kaster.common.login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginInteractorBiometrics(
    private val loginPersistence: LoginPersistence,
    private val biometrics: Biometrics,
    private val coroutineScope: CoroutineScope
) : LoginInteractor {
    private val _loginState: MutableStateFlow<LoginInteractor.LoginState> = MutableStateFlow(LoginInteractor.LoginState.Locked)
    override val loginState: StateFlow<LoginInteractor.LoginState> = _loginState

    override val biometricsSupported: Boolean
        get() = biometrics.isSupported

    override fun save(credentials: LoginInteractor.Credentials, requireUserAuth: Boolean) {
        coroutineScope.launch {
            if (!requireUserAuth || biometrics.promptUserAuth() == Biometrics.AuthResult.Success) {
                loginPersistence.userAuthenticationRequired = requireUserAuth
                loginPersistence.saveCredentials(credentials)
                _loginState.value = LoginInteractor.LoginState.LoggedIn(credentials)
            }
        }
    }

    override fun unlock() {
        coroutineScope.launch {
            if (loginPersistence.userAuthenticationRequired && biometrics.promptUserAuth() != Biometrics.AuthResult.Success) {
                _loginState.value = LoginInteractor.LoginState.UnlockFailed
            } else {
                _loginState.value = when (val credentials = loginPersistence.loadCredentials()) {
                    null -> LoginInteractor.LoginState.LoggedOut
                    else -> LoginInteractor.LoginState.LoggedIn(credentials)
                }
            }
        }
    }

    override fun clear() {
        loginPersistence.clear()
        _loginState.value = LoginInteractor.LoginState.LoggedOut
    }
}