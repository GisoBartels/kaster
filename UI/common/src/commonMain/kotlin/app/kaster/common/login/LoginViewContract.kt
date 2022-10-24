package app.kaster.common.login

data class LoginViewState(
    val username: String = "",
    val password: String = "",
    val loginEnabled: Boolean = false
)

sealed interface LoginInput {
    data class Username(val value: String) : LoginInput
    data class MasterPassword(val value: String) : LoginInput
    object Login : LoginInput
}