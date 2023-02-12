package app.kaster.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.kaster.common.domainentry.DomainEntryPersistence
import app.kaster.common.domainentry.DomainEntryScreen
import app.kaster.common.domainlist.DomainListScreen
import app.kaster.common.login.LoginInteractor
import app.kaster.common.login.LoginScreen
import app.kaster.common.navigation.Screen

@Composable
fun KasterRoot(
    viewModel: RootViewModel,
    loginInteractor: LoginInteractor,
    domainEntryPersistence: DomainEntryPersistence,
) {
    val viewState by viewModel.viewState.collectAsState(RootViewState(Screen.Empty))
    when (val screen = viewState.screen) {
        Screen.Empty -> Unit
        Screen.Login -> LoginScreen(loginInteractor)
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