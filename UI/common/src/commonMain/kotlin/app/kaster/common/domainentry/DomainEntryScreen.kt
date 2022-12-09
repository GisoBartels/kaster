package app.kaster.common.domainentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import app.kaster.common.domainentry.DomainEntryInput.Domain
import app.kaster.common.domainentry.DomainEntryInput.Save
import app.kaster.common.domainlist.DomainListPersistence

@Composable
fun DomainEntryScreen(domain: String?, persistence: DomainListPersistence) {
    val viewModel = remember { DomainEntryViewModel(domain, persistence) }
    val viewState by viewModel.viewState.collectAsState(DomainEntryViewState(""))
    DomainEntryContent(viewState, viewModel::onInput)
}

@Composable
fun DomainEntryContent(viewState: DomainEntryViewState, input: (DomainEntryInput) -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Password Kaster") },
            backgroundColor = MaterialTheme.colors.primary,
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
        }
    }
}
