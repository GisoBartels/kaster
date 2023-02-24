package app.passwordkaster.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import app.passwordkaster.common.login.LoginContent
import app.passwordkaster.common.login.LoginViewState

@Preview
@Composable
fun LoginPreview() {
    KasterTheme {
        Surface(color = MaterialTheme.colors.surface) {
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