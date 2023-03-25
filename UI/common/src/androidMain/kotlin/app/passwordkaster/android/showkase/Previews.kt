package app.passwordkaster.android.showkase

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.common.domainentry.DomainEntry
import app.passwordkaster.common.domainentry.DomainEntryContent
import app.passwordkaster.common.domainentry.DomainEntryViewState
import app.passwordkaster.common.domainlist.DomainListContent
import app.passwordkaster.common.domainlist.DomainListViewState
import app.passwordkaster.common.login.LoginContent
import app.passwordkaster.common.login.LoginViewState
import kotlinx.collections.immutable.persistentListOf

@Composable
private fun Preview(content: @Composable () -> Unit) {
    KasterTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 8.dp
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun LoginPreview() = Preview {
    LoginContent(LoginViewState(username = "user", password = "password", passwordMasked = true, loginEnabled = true)) {}
}

@Preview
@Composable
fun DomainListPreview() = Preview {
    DomainListContent(DomainListViewState(persistentListOf("one", "two", "three", "verylongdomainname.evenlongertld"))) {}
}

@Preview
@Composable
fun DomainEntryPreview() = Preview {
    DomainEntryContent(
        DomainEntryViewState(
            domainEntry = DomainEntry("example.org"),
            generatedPassword = DomainEntryViewState.GeneratedPassword.Result("generatedPassword")
        )
    ) {}
}
