import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.kaster.common.KasterRoot
import app.kaster.common.KasterTheme
import app.kaster.common.RootViewModel
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.login.Biometrics
import app.kaster.common.login.LoginInteractorBiometrics
import app.kaster.common.login.LoginPersistenceNop

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
