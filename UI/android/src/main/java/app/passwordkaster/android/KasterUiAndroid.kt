package app.passwordkaster.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import app.passwordkaster.common.KasterRoot
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.common.RootViewModel
import app.passwordkaster.common.domainentry.DomainEntryPersistence
import app.passwordkaster.common.login.LoginInteractor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun KasterAndroidUi(
    loginInteractor: LoginInteractor,
    domainListPersistence: DomainEntryPersistence,
    rootViewModel: RootViewModel,
) {
    KasterTheme {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val systemBarColor = MaterialTheme.colorScheme.primaryContainer

        DisposableEffect(systemUiController, systemBarColor) {
            systemUiController.setSystemBarsColor(
                color = systemBarColor,
                darkIcons = useDarkIcons
            )
            onDispose {}
        }
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            KasterRoot(rootViewModel, loginInteractor, domainListPersistence)
        }
    }
}
