package app.kaster.common.domainlist

import kotlinx.collections.immutable.ImmutableList

data class DomainListViewState(
    val domainList: ImmutableList<String>
)

sealed interface DomainListInput {
    object AddDomain : DomainListInput
    data class EditDomain(val domain: String) : DomainListInput
}