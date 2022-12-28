import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.kaster.common.KasterRoot
import app.kaster.common.KasterTheme
import app.kaster.common.RootViewModel
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.login.LoginPersistenceInMemory

fun main() = application {
    val loginPersistence = remember { LoginPersistenceInMemory() } // TODO real persistence
    val domainListPersistence = remember { DomainEntryPersistenceInMemory() } // TODO real persistence
    val viewModel = remember { RootViewModel(loginPersistence) }

    KasterTheme {
        Window(title = "Password Kaster", onCloseRequest = ::exitApplication) {
            Surface(modifier = Modifier.fillMaxSize()) {
                KasterRoot(viewModel, loginPersistence, domainListPersistence)
            }
        }
    }
}
