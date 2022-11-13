package app.kaster.common.domainlist

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow

class DomainListPersistenceInMemory(initialDomainList: List<String> = emptyList()) : DomainListPersistence {

    override val domainList = MutableStateFlow(initialDomainList.toPersistentList())

}