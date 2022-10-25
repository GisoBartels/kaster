package app.kaster.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.kaster.common.KasterContent
import app.kaster.common.KasterTheme
import app.kaster.common.KasterUi
import app.kaster.common.KasterViewState
import app.kaster.common.login.LoginUi
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun KasterAndroidUi() {
    KasterTheme {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = isSystemInDarkTheme()
        val systemBarColor = MaterialTheme.colors.primary

        DisposableEffect(systemUiController, systemBarColor) {
            systemUiController.setSystemBarsColor(
                color = systemBarColor,
                darkIcons = useDarkIcons
            )
            onDispose {}
        }
        Column {
            var showNewUi by rememberSaveable { mutableStateOf(false) }
            TopAppBar(
                title = { Text("Password Kaster") },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    IconButton(onClick = { showNewUi = !showNewUi }) {
                        Icon(Icons.Outlined.AutoAwesome, "switch UI")
                    }
                })
            Surface(modifier = Modifier.fillMaxSize()) {
                if (showNewUi) {
                    LoginUi()
                } else {
                    KasterUi()
                }
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
