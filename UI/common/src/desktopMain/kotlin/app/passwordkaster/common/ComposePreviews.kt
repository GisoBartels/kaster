package app.passwordkaster.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import app.passwordkaster.common.login.LoginContent
import app.passwordkaster.common.login.LoginViewState

@Preview
@Composable
fun LoginPreview() {
    KasterTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            LoginContent(
                LoginViewState(
                    username = "Bender",
                    password = "passw0rd",
                    loginEnabled = true
                )
            ) {}
        }
    }
}