package app.kaster.common.login

import kotlinx.coroutines.flow.StateFlow

interface LoginPersistence {

    val loginState: StateFlow<LoginState>

    fun save(credentials: Credentials, requireUserAuth: Boolean)

    fun unlock()

    fun clear()

    data class Credentials(val username: String, val password: String)

    sealed interface LoginState {
        object Locked : LoginState
        object UnlockFailed : LoginState
        data class LoggedIn(val credentials: Credentials) : LoginState
        object LoggedOut : LoginState
    }

}