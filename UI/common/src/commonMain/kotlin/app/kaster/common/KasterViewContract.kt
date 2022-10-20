package app.kaster.common

import app.kaster.core.Kaster
import app.kaster.core.Kaster.PasswordType

data class KasterViewState(
    val username: String = "",
    val password: String = "",
    val domain: String = "",
    val scope: Kaster.Scope = Kaster.Scope.Authentication,
    val counter: Int = 1,
    val type: PasswordType = PasswordType.Maximum,
    val result: String = ""
)

sealed interface KasterInput {
    data class Username(val value: String) : KasterInput
    data class Password(val value: String) : KasterInput
    data class Domain(val value: String) : KasterInput
    data class Scope(val scope: Kaster.Scope) : KasterInput
    object CounterIncrease : KasterInput
    object CounterDecrease : KasterInput
    data class Type(val value: PasswordType) : KasterInput
}