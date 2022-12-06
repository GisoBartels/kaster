package app.kaster.common.domainentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DomainEntryScreen(domain: String?) {
    val viewModel = remember { DomainEntryViewModel(domain) }
    val viewState by viewModel.viewState.collectAsState(DomainEntryViewState(""))
    DomainEntryContent(viewState)
}

@Composable
fun DomainEntryContent(viewState: DomainEntryViewState) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        OutlinedTextField(
            value = viewState.domain,
            onValueChange = { TODO() },
            label = { Text("Domain") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}