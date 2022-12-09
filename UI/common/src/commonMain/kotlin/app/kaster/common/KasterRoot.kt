package app.kaster.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.kaster.common.domainentry.DomainEntryScreen
import app.kaster.common.domainlist.DomainListPersistence
import app.kaster.common.domainlist.DomainListScreen
import app.kaster.common.login.LoginPersistence
import app.kaster.common.login.LoginScreen
import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen

@Composable
fun KasterRoot(loginPersistence: LoginPersistence, domainListPersistence: DomainListPersistence) {
    val currentScreen by Navigator.currentScreen.collectAsState()
    when (currentScreen) {
        Screen.Login -> LoginScreen(loginPersistence)
        Screen.DomainList -> DomainListScreen(domainListPersistence)
        is Screen.DomainEntry -> DomainEntryScreen((currentScreen as Screen.DomainEntry).domain, domainListPersistence)
    }
}