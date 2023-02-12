package app.kaster.common.login

import app.kaster.common.login.LoginPersistence.LoginState
import app.kaster.common.login.LoginPersistence.LoginState.Locked
import app.kaster.common.login.LoginPersistence.LoginState.LoggedIn
import app.kaster.common.login.LoginPersistence.LoginState.LoggedOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginPersistenceInMemory(private val initialCredentials: LoginPersistence.Credentials? = null) : LoginPersistence {

    constructor(username: String, masterPassword: String) : this(LoginPersistence.Credentials(username, masterPassword))

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(Locked)
    override val loginState: StateFlow<LoginState> = _loginState

    override fun save(credentials: LoginPersistence.Credentials, requireUserAuth: Boolean) {
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