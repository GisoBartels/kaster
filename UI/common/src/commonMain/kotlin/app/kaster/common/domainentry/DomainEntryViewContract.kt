package app.kaster.common.domainentry

data class DomainEntryViewState(
    val domain: String = "",
    val generatedPassword: GeneratedPassword = GeneratedPassword.Generating
) {
    sealed interface GeneratedPassword {
        object NotEnoughData : GeneratedPassword
        object Generating : GeneratedPassword
        data class Result(val password: String) : GeneratedPassword
    }
}

sealed interface DomainEntryInput {
    data class Domain(val value: String) : DomainEntryInput
    object Save : DomainEntryInput
    object Dismiss : DomainEntryInput
}