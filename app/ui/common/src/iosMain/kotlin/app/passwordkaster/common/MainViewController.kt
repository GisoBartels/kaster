package app.passwordkaster.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import app.passwordkaster.logic.RootViewModel
import app.passwordkaster.logic.domainentry.DomainEntryPersistenceInMemory
import app.passwordkaster.logic.login.Biometrics
import app.passwordkaster.logic.login.LoginInteractorBiometrics
import app.passwordkaster.logic.login.LoginPersistenceNop

fun MainViewController() = ComposeUIViewController {

    val coroutineScope = rememberCoroutineScope()
    val loginInteractor = remember { LoginInteractorBiometrics(LoginPersistenceNop, Biometrics.Unsupported, coroutineScope) }
    val domainListPersistence = remember { DomainEntryPersistenceInMemory() } // TODO real persistence
    val viewModel = remember { RootViewModel({ TODO() }, loginInteractor) }

    KasterTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            KasterRoot(viewModel, loginInteractor, domainListPersistence)
        }
    }
}
