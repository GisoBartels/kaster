package app.passwordkaster.common.domainentry

import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow

class DomainEntryPersistenceInMemory(initial: Set<DomainEntry> = emptySet()) : DomainEntryPersistence {

    constructor(vararg entries: DomainEntry) : this(entries.toSet())

    override val entries = MutableStateFlow(initial.toPersistentSet())

}