package app.kaster.common.domainentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import app.kaster.common.domainentry.DomainEntryInput.*
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.domainlist.DomainListPersistence
import app.kaster.common.login.LoginPersistence

@Composable
fun DomainEntryScreen(
    domain: String?,
    domainListPersistence: DomainListPersistence,
    loginPersistence: LoginPersistence
) {
    val viewModel = remember { DomainEntryViewModel(domain, domainListPersistence, loginPersistence) }
    val viewState by viewModel.viewState.collectAsState(DomainEntryViewState())
    DomainEntryContent(viewState, viewModel::onInput)
}

@Composable
fun DomainEntryContent(viewState: DomainEntryViewState, input: (DomainEntryInput) -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Password Kaster") },
            backgroundColor = MaterialTheme.colors.primary,
            navigationIcon = {
                IconButton(onClick = { input(Dismiss) }, modifier = Modifier.testTag("dismiss")) {
                    Icon(Icons.Outlined.Close, "Dismiss")
                }
            },
            actions = {
                IconButton(onClick = { input(Save) }, modifier = Modifier.testTag("save")) {
                    Icon(Icons.Outlined.Save, "Save")
                }
            }
        )
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            OutlinedTextField(
                value = viewState.domain,
                onValueChange = { input(Domain(it)) },
                label = { Text("Domain") },
                modifier = Modifier.fillMaxWidth().testTag("domain")
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { TODO() },
                modifier = Modifier.fillMaxWidth().height(56.dp).testTag("password"),
                enabled = viewState.generatedPassword is GeneratedPassword.Result
            ) {
                when (viewState.generatedPassword) {
                    GeneratedPassword.Generating -> LinearProgressIndicator(Modifier.testTag("generating"))
                    GeneratedPassword.NotEnoughData -> Text("""¯\_(ツ)_/¯""", modifier = Modifier.testTag("noData"))
                    is GeneratedPassword.Result -> {
                        Icon(Icons.Outlined.ContentCopy, "Copy password", modifier = Modifier.padding(end = 8.dp))
                        Text(viewState.generatedPassword.password)
                    }
                }
            }
        }
    }
}
