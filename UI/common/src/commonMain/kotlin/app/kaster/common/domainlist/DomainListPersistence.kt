package app.kaster.common.domainlist

interface DomainListPersistence {

    fun saveDomainList(list: List<String>)

    fun loadDomainList(): List<String>

}