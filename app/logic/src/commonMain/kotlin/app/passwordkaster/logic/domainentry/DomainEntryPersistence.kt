package app.passwordkaster.logic.domainentry

import kotlinx.collections.immutable.PersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface DomainEntryPersistence {

    val entries: MutableStateFlow<PersistentSet<DomainEntry>>

    fun remove(domain: String) {
        entries.update { it.removeAll { entry -> entry.domain == domain } }
    }

}