package app.kaster.common.domainentry

import app.kaster.core.Kaster

data class DomainEntryViewState(
    val domainEntry: DomainEntry = DomainEntry(),
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
    data class Scope(val value: Kaster.Scope) : DomainEntryInput
    data class Counter(val value: Int) : DomainEntryInput
    object IncreaseCounter : DomainEntryInput
    object DecreaseCounter : DomainEntryInput
    data class Type(val value: Kaster.PasswordType) : DomainEntryInput

    object Save : DomainEntryInput
    object Dismiss : DomainEntryInput
}