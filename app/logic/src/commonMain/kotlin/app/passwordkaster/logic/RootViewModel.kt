package app.passwordkaster.logic

import app.passwordkaster.logic.login.LoginInteractor
import app.passwordkaster.logic.login.LoginInteractor.LoginState.Locked
import app.passwordkaster.logic.login.LoginInteractor.LoginState.LoggedIn
import app.passwordkaster.logic.login.LoginInteractor.LoginState.LoggedOut
import app.passwordkaster.logic.login.LoginInteractor.LoginState.UnlockFailed
import app.passwordkaster.logic.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class RootViewModel(private val onCloseApp: () -> Unit, loginInteractor: LoginInteractor) {

    private val openedDomainEntry = MutableStateFlow<String?>(null)

    val viewState: Flow<RootViewState> = loginInteractor.loginState
        .onEach {
            when (it) {
                Locked -> loginInteractor.unlock()
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