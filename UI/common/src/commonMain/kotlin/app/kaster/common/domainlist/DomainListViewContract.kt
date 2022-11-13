package app.kaster.common.domainlist

import kotlinx.collections.immutable.ImmutableList

data class DomainListViewState(
    val domainList: ImmutableList<DomainEntry>
)

data class DomainEntry(
    val domain: String
)

sealed interface DomainListInput {
    object AddDomain : DomainListInput
}