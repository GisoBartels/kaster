package app.kaster.common.domainentry

import kotlinx.collections.immutable.PersistentSet
import kotlinx.coroutines.flow.MutableStateFlow

interface DomainEntryPersistence {

    val entries: MutableStateFlow<PersistentSet<DomainEntry>>

}