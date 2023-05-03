import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.passwordkaster.common.KasterRoot
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.logic.RootViewModel
import app.passwordkaster.logic.domainentry.DomainEntryPersistenceInMemory
import app.passwordkaster.logic.login.Biometrics
import app.passwordkaster.logic.login.LoginInteractorBiometrics
import app.passwordkaster.logic.login.LoginPersistenceNop

fun main() = application {
    val coroutineScope = rememberCoroutineScope()
    val loginInteractor = remember { LoginInteractorBiometrics(LoginPersistenceNop, Biometrics.Unsupported, coroutineScope) }
    val domainListPersistence = remember { DomainEntryPersistenceInMemory() } // TODO real persistence
    val viewModel = remember { RootViewModel(::exitApplication, loginInteractor) }

    KasterTheme {
        Window(title = "Password Kaster", onCloseRequest = ::exitApplication) {
            Surface(modifier = Modifier.fillMaxSize()) {
                KasterRoot(viewModel, loginInteractor, domainListPersistence)
            }
        }
    }
}
