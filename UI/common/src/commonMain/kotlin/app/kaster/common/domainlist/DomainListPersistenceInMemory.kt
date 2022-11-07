package app.kaster.common.domainlist

class DomainListPersistenceInMemory(initialDomainList: List<String> = emptyList()) : DomainListPersistence {

    private var domainList = initialDomainList

    override fun saveDomainList(list: List<String>) {
        domainList = list
    }

    override fun loadDomainList(): List<String> = domainList
}