package app.passwordkaster.common.domainlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import app.passwordkaster.common.kasterTopAppBarColors
import app.passwordkaster.logic.domainentry.DomainEntryPersistence
import app.passwordkaster.logic.domainlist.DomainListInput
import app.passwordkaster.logic.domainlist.DomainListViewModel
import app.passwordkaster.logic.domainlist.DomainListViewState
import app.passwordkaster.logic.domainlist.DomainListViewState.SearchState.HideSearch
import app.passwordkaster.logic.domainlist.DomainListViewState.SearchState.ShowSearch
import app.passwordkaster.logic.login.LoginInteractor

@Composable
fun DomainListScreen(
    onEditDomainEntry: (String?) -> Unit,
    loginInteractor: LoginInteractor,
    domainEntryPersistence: DomainEntryPersistence
) {
    val viewModel = remember { DomainListViewModel(onEditDomainEntry, loginInteractor, domainEntryPersistence) }
    val viewState by viewModel.viewState.collectAsState(DomainListViewState())
    DomainListContent(viewState, viewModel::onInput)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomainListContent(viewState: DomainListViewState, input: (DomainListInput) -> Unit) {
    Column {
        when (val searchState = viewState.searchState) {
            HideSearch -> RegularTopAppBar(input)
            is ShowSearch -> SearchTopAppBar(searchState.searchTerm, input)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(viewState.domainList) { domain ->
                    ListItem(
                        headlineContent = { Text(domain) },
                        modifier = Modifier.clickable { input(DomainListInput.EditDomain(domain)) }
                    )
                }
            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                onClick = { input(DomainListInput.AddDomain) }
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegularTopAppBar(input: (DomainListInput) -> Unit) = TopAppBar(
    title = { Text("Password Kaster") },
    colors = kasterTopAppBarColors,
    actions = {
        IconButton(onClick = { input(DomainListInput.StartSearch) }) {
            Icon(Icons.Outlined.Search, "Search")
        }
        IconButton(onClick = { input(DomainListInput.Logout) }) {
            Icon(Icons.AutoMirrored.Outlined.Logout, "Log out")
        }
    }
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopAppBar(
    searchTerm: String,
    input: (DomainListInput) -> Unit
) = TopAppBar(
    title = {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(true) { focusRequester.requestFocus() }
        TextField(
            value = searchTerm,
            onValueChange = { input(DomainListInput.Search(it)) },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).testTag("search"),
            singleLine = true
        )
    },
    colors = kasterTopAppBarColors,
    actions = {
        IconButton(onClick = { input(DomainListInput.StopSearch) }) {
            Icon(Icons.Outlined.Close, "Stop search")
        }
    }
)