package app.kaster.android.showkase

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.kaster.common.KasterTheme
import app.kaster.common.domainentry.DomainEntry
import app.kaster.common.domainentry.DomainEntryContent
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainlist.DomainListContent
import app.kaster.common.domainlist.DomainListViewState
import app.kaster.common.login.LoginContent
import app.kaster.common.login.LoginViewState
import kotlinx.collections.immutable.persistentListOf

@Composable
private fun Preview(content: @Composable () -> Unit) {
    KasterTheme {
        Surface(
            color = MaterialTheme.colors.background,
            elevation = 8.dp
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
