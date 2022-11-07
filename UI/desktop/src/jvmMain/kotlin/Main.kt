import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.kaster.common.KasterContent
import app.kaster.common.KasterRoot
import app.kaster.common.KasterTheme
import app.kaster.common.KasterViewState
import app.kaster.common.domainlist.DomainListPersistenceInMemory
import app.kaster.common.login.LoginPersistenceInMemory

fun main() = application {
    // TODO implement real persistence
    val loginPersistence = remember { LoginPersistenceInMemory() }
    val domainListPersistence = remember { DomainListPersistenceInMemory(listOf("dummy")) }

    KasterTheme {
        Window(title = "Password Kaster", onCloseRequest = ::exitApplication) {
            Surface(modifier = Modifier.fillMaxSize()) {
                KasterRoot(loginPersistence, domainListPersistence)
            }
        }
    }
}

@Preview
@Composable
fun KasterPreview() {
    KasterContent(
        KasterViewState(
            username = "Bender",
            password = "secret",
            domain = "example.com",
            counter = 9000,
            result = "sitePassword"
        )
    ) {}
}
