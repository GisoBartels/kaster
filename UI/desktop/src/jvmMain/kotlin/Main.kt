import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.kaster.common.KasterContent
import app.kaster.common.KasterTheme
import app.kaster.common.KasterUi
import app.kaster.common.KasterViewState

fun main() = application {
    KasterTheme {
        Window(title = "Password Kaster", onCloseRequest = ::exitApplication) {
            KasterUi()
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
