package app.kaster.common.domainlist

import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class DomainListViewModel(persistence: DomainListPersistence) {

    private val domainList: StateFlow<ImmutableList<DomainEntry>> =
        MutableStateFlow(persistence.loadDomainList().map { DomainEntry(it) }.toImmutableList())

    val viewState = domainList.map { DomainListViewState(it) }

    fun onInput(input: DomainListInput) {
        when (input) {
            is DomainListInput.AddDomain -> Navigator.navTo(Screen.EditDomainEntry(null))
        }
    }
}