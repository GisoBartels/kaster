package app.passwordkaster.common.login

data class LoginViewState(
    val username: String = "",
    val password: String = "",
    val passwordMasked: Boolean = true,
    val loginEnabled: Boolean = false,
    val biometricLoginEnabled: Boolean = false
)

sealed interface LoginInput {
    data class Username(val value: String) : LoginInput
    data class MasterPassword(val value: String) : LoginInput

    object MaskPassword : LoginInput
    object UnmaskPassword : LoginInput
    object Login : LoginInput
    object LoginWithBiometrics : LoginInput
}