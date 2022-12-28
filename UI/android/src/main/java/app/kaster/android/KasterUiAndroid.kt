package app.kaster.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.kaster.common.KasterRoot
import app.kaster.common.KasterTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun KasterAndroidUi() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val loginPersistence = remember { LoginPersistenceAndroid(context, coroutineScope) }
    val domainListPersistence = remember { DomainEntryPersistenceAndroid(context, coroutineScope) }

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
        Surface(modifier = Modifier.fillMaxSize()) {
            KasterRoot(loginPersistence, domainListPersistence)
        }
    }
}
