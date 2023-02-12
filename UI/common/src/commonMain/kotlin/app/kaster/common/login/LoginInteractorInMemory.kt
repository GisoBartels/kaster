package app.kaster.common.login

import app.kaster.common.login.LoginInteractor.LoginState
import app.kaster.common.login.LoginInteractor.LoginState.Locked
import app.kaster.common.login.LoginInteractor.LoginState.LoggedIn
import app.kaster.common.login.LoginInteractor.LoginState.LoggedOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginInteractorInMemory(private val initialCredentials: LoginInteractor.Credentials? = null) : LoginInteractor {

    constructor(username: String, masterPassword: String) : this(LoginInteractor.Credentials(username, masterPassword))

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(Locked)
    override val loginState: StateFlow<LoginState> = _loginState

    override fun save(credentials: LoginInteractor.Credentials, requireUserAuth: Boolean) {
        _loginState.value = LoggedIn(credentials)
    }

    override fun unlock() {
        _loginState.value = when (initialCredentials) {
            null -> LoggedOut
            else -> LoggedIn(initialCredentials)
        }
    }

    override fun clear() {
        _loginState.value = LoggedOut
    }

}