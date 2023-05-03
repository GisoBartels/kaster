package app.passwordkaster.logic.domainentry

import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow

class DomainEntryPersistenceInMemory(initial: Set<DomainEntry> = emptySet()) : DomainEntryPersistence {

    override val entries = MutableStateFlow(initial.toPersistentSet())

}
