package app.kaster.common

import app.kaster.common.login.LoginPersistence
import app.kaster.common.login.LoginPersistence.LoginState.Locked
import app.kaster.common.login.LoginPersistence.LoginState.LoggedIn
import app.kaster.common.login.LoginPersistence.LoginState.LoggedOut
import app.kaster.common.login.LoginPersistence.LoginState.UnlockFailed
import app.kaster.common.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class RootViewModel(private val onCloseApp: () -> Unit, loginPersistence: LoginPersistence) {

    private val openedDomainEntry = MutableStateFlow<String?>(null)

    val viewState: Flow<RootViewState> = loginPersistence.loginState
        .onEach {
            when (it) {
                Locked -> loginPersistence.unlock()
                UnlockFailed -> onCloseApp()
                else -> {}
            }
        }
        .combine(openedDomainEntry) { loginState, openedDomain ->
            when (loginState) {
                Locked, UnlockFailed -> Screen.Empty
                LoggedOut -> Screen.Login
                is LoggedIn -> when (openedDomain) {
                    null -> Screen.DomainList
                    else -> Screen.DomainEntry(openedDomain)
                }
            }
        }
        .map { RootViewState(it) }
        .onStart { if (loginPersistence.loginState.value == Locked) loginPersistence.unlock() }

    fun onInput(input: RootInput) {
        when (input) {
            is RootInput.ShowDomainEntry -> openedDomainEntry.value = input.domain ?: ""
            RootInput.CloseDomainEntry -> openedDomainEntry.value = null
            RootInput.BackPressed -> when {
                openedDomainEntry.value != null -> openedDomainEntry.value = null
                else -> onCloseApp()
            }
        }
    }

}