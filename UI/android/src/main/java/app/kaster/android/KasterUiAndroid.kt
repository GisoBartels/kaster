package app.kaster.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.kaster.common.KasterContent
import app.kaster.common.KasterUi
import app.kaster.common.KasterViewState

@Composable
fun KasterAndroidUi() {
    MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
        Column {
            TopAppBar(title = { Text("Password Kaster") })
            Surface(modifier = Modifier.fillMaxSize()) {
                KasterUi()
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
