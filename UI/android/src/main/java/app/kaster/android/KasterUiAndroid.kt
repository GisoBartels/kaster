package app.kaster.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import app.kaster.common.KasterRoot
import app.kaster.common.KasterTheme
import app.kaster.common.RootViewModel
import app.kaster.common.domainentry.DomainEntryPersistence
import app.kaster.common.login.Biometrics
import app.kaster.common.login.LoginPersistence
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun KasterAndroidUi(
    loginPersistence: LoginPersistence,
    domainListPersistence: DomainEntryPersistence,
    rootViewModel: RootViewModel,
    biometrics: Biometrics
) {
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
            KasterRoot(rootViewModel, loginPersistence, domainListPersistence, biometrics)
        }
    }
}
