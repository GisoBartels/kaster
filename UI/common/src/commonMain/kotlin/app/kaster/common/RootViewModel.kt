package app.kaster.common

import app.kaster.common.login.LoginPersistence
import app.kaster.common.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class RootViewModel(private val onCloseApp: () -> Unit, loginPersistence: LoginPersistence) {

    private val openedDomainEntry = MutableStateFlow<String?>(null)

    val viewState: Flow<RootViewState> =
        combine(loginPersistence.credentials, openedDomainEntry) { credentials, openedDomain ->
            when {
                credentials == null -> Screen.Login
                openedDomain == null -> Screen.DomainList
                else -> Screen.DomainEntry(openedDomain)
            }
        }.map { RootViewState(it) }

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