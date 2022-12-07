package app.kaster.common.domainlist

import app.kaster.common.domainlist.DomainListInput.AddDomain
import app.kaster.common.domainlist.DomainListInput.EditDomain
import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map

class DomainListViewModel(persistence: DomainListPersistence) {

    val viewState = persistence.domainList.map { DomainListViewState(it.map { DomainEntry(it) }.toImmutableList()) }

    fun onInput(input: DomainListInput) {
        when (input) {
            is AddDomain -> Navigator.navTo(Screen.DomainEntry(null))
            is EditDomain -> Navigator.navTo(Screen.DomainEntry(input.domain))
        }
    }
}