package app.kaster.common.domainlist

import app.kaster.common.domainentry.DomainEntryPersistence
import app.kaster.common.domainlist.DomainListInput.*
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map

class DomainListViewModel(
    private val onEditDomainEntry: (String?) -> Unit,
    persistence: DomainEntryPersistence
) {

    val viewState = persistence.entries.map { entries ->
        DomainListViewState(entries.map { it.domain }.sortedWith(String.CASE_INSENSITIVE_ORDER).toImmutableList())
    }

    fun onInput(input: DomainListInput) {
        when (input) {
            AddDomain -> onEditDomainEntry(null)
            is EditDomain -> onEditDomainEntry(input.domain)
            Logout -> TODO()
        }
    }
}