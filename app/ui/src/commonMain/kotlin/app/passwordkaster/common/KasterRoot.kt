package app.passwordkaster.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.passwordkaster.common.domainentry.DomainEntryScreen
import app.passwordkaster.common.domainlist.DomainListScreen
import app.passwordkaster.common.login.LoginScreen
import app.passwordkaster.logic.RootInput
import app.passwordkaster.logic.RootViewModel
import app.passwordkaster.logic.RootViewState
import app.passwordkaster.logic.domainentry.DomainEntryPersistence
import app.passwordkaster.logic.login.LoginInteractor
import app.passwordkaster.logic.login.OSSLicenses
import app.passwordkaster.logic.navigation.Screen

@Composable
fun KasterRoot(
    viewModel: RootViewModel,
    loginInteractor: LoginInteractor,
    domainEntryPersistence: DomainEntryPersistence,
    ossLicenses: OSSLicenses,
) {
    val viewState by viewModel.viewState.collectAsState(RootViewState(Screen.Empty))
    when (val screen = viewState.screen) {
        Screen.Empty -> Unit
        Screen.Login -> LoginScreen(loginInteractor, ossLicenses)
        Screen.DomainList -> DomainListScreen(
            { viewModel.onInput(RootInput.ShowDomainEntry(it)) },
            loginInteractor,
            domainEntryPersistence
        )

        is Screen.DomainEntry -> DomainEntryScreen(
            screen.domain,
            { viewModel.onInput(RootInput.CloseDomainEntry) },
            loginInteractor,
            domainEntryPersistence
        )
    }
}