package app.kaster.common.domainlist

import app.kaster.common.domainentry.DomainEntryPersistence
import app.kaster.common.domainlist.DomainListInput.AddDomain
import app.kaster.common.domainlist.DomainListInput.EditDomain
import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map

class DomainListViewModel(persistence: DomainEntryPersistence) {

    val viewState = persistence.entries.map { entries ->
        DomainListViewState(entries.map { it.domain }.sortedWith(String.CASE_INSENSITIVE_ORDER).toImmutableList())
    }

    fun onInput(input: DomainListInput) {
        when (input) {
            is AddDomain -> Navigator.navTo(Screen.DomainEntry(null))
            is EditDomain -> Navigator.navTo(Screen.DomainEntry(input.domain))
        }
    }
}