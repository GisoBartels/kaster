package app.kaster.common.domainlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.kaster.common.navigation.Navigator
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DomainListScreen(persistence: DomainListPersistence) {
    val viewModel = remember { DomainListViewModel(persistence) }
    val viewState by viewModel.viewState.collectAsState(DomainListViewState(persistentListOf()))
    DomainListContent(viewState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DomainListContent(viewState: DomainListViewState) {
    Box {
        LazyColumn {
            items(viewState.domainList.size) { i ->
                ListItem(text = { Text(viewState.domainList[i].domain) })
            }
        }
        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.BottomStart),
            text = { Text("Log out") },
            icon = { Icon(Icons.Outlined.Logout, "Log out") },
            onClick = { Navigator.goBack() }
        )
    }
}