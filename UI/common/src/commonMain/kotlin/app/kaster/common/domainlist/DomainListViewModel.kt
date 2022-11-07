package app.kaster.common.domainlist

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class DomainListViewModel(persistence: DomainListPersistence) {

    private val domainList: StateFlow<ImmutableList<DomainEntry>> =
        MutableStateFlow(persistence.loadDomainList().map { DomainEntry(it) }.toImmutableList())

    val viewState = domainList.map { DomainListViewState(it) }

}