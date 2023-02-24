package app.passwordkaster.common.domainlist

import app.passwordkaster.common.domainentry.DomainEntryPersistence
import app.passwordkaster.common.domainlist.DomainListInput.*
import app.passwordkaster.common.domainlist.DomainListViewState.SearchState.HideSearch
import app.passwordkaster.common.domainlist.DomainListViewState.SearchState.ShowSearch
import app.passwordkaster.common.login.LoginInteractor
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class DomainListViewModel(
    private val onEditDomainEntry: (String?) -> Unit,
    private val loginInteractor: LoginInteractor,
    domainEntryPersistence: DomainEntryPersistence
) {
    private val searchTerm = MutableStateFlow<String?>(null)

    val viewState = combine(domainEntryPersistence.entries, searchTerm) { entries, searchTerm ->
        DomainListViewState(
            domainList = entries
                .map { it.domain }
                .filter { domain -> searchTerm.isNullOrBlank() || domain.contains(searchTerm, ignoreCase = true) }
                .sortedWith(String.CASE_INSENSITIVE_ORDER)
                .toImmutableList(),
            searchState = searchTerm?.let { ShowSearch(it) } ?: HideSearch
        )
    }

    fun onInput(input: DomainListInput) {
        when (input) {
            AddDomain -> onEditDomainEntry(null)
            is EditDomain -> onEditDomainEntry(input.domain)
            StartSearch -> searchTerm.value = ""
            StopSearch -> searchTerm.value = null
            is Search -> searchTerm.value = input.contains
            Logout -> loginInteractor.clear()
        }
    }
}