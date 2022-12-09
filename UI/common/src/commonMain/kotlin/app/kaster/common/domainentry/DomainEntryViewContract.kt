package app.kaster.common.domainentry

data class DomainEntryViewState(
    val domain: String
)

sealed interface DomainEntryInput {
    data class Domain(val value: String) : DomainEntryInput
    object Save : DomainEntryInput
    object Dismiss : DomainEntryInput
}