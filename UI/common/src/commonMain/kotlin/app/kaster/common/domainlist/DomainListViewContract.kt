package app.kaster.common.domainlist

import app.kaster.common.domainlist.DomainListViewState.SearchState.HideSearch
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DomainListViewState(
    val domainList: ImmutableList<String> = persistentListOf(),
    val searchState: SearchState = HideSearch
) {
    sealed interface SearchState {
        data class ShowSearch(val searchTerm: String) : SearchState
        object HideSearch : SearchState
    }
}

sealed interface DomainListInput {
    object AddDomain : DomainListInput
    data class EditDomain(val domain: String) : DomainListInput
    object StartSearch : DomainListInput
    object StopSearch : DomainListInput
    data class Search(val contains: String) : DomainListInput
    object Logout : DomainListInput
}