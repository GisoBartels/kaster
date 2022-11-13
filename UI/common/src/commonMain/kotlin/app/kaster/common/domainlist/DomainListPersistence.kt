package app.kaster.common.domainlist

import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.MutableStateFlow

interface DomainListPersistence {

    val domainList: MutableStateFlow<PersistentList<String>>

}