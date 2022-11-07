package app.kaster.common.domainlist

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import app.kaster.common.navigation.Navigator

@Composable
fun DomainListScreen() {
    Box(contentAlignment = Alignment.Center) {
        ExtendedFloatingActionButton(
            text = { Text("Log out") },
            icon = { Icon(Icons.Outlined.Logout, "Log out") },
            onClick = { Navigator.goBack() }
        )
    }
}