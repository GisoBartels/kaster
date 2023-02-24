package app.passwordkaster.common.domainentry

import app.passwordkaster.core.Kaster
import kotlinx.serialization.Serializable

@Serializable
data class DomainEntry(
    val domain: String = "",
    val scope: Kaster.Scope = Kaster.Scope.Authentication,
    val counter: Int = 1,
    val type: Kaster.PasswordType = Kaster.PasswordType.Maximum
)
