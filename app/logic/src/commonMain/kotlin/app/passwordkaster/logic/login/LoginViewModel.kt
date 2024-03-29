package app.passwordkaster.logic.login

import app.passwordkaster.logic.login.LoginInput.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginViewModel(
    private val loginInteractor: LoginInteractor,
    private val ossLicenses: OSSLicenses
) {
    private val usernameState = MutableStateFlow("")
    private val masterPasswordState = MutableStateFlow("")
    private val maskPasswordState = MutableStateFlow(true)

    val viewState: Flow<LoginViewState> = combine(
        usernameState,
        masterPasswordState,
        maskPasswordState,
    ) { username, password, maskPassword ->
        LoginViewState(
            username = username,
            password = password,
            passwordMasked = maskPassword,
            loginEnabled = username.isNotEmpty() && password.isNotEmpty(),
            biometricLoginEnabled = loginInteractor.biometricsSupported
        )
    }

    fun onInput(input: LoginInput) {
        when (input) {
            is Username -> usernameState.value = input.value
            is MasterPassword -> masterPasswordState.value = input.value
            MaskPassword -> maskPasswordState.value = true
            UnmaskPassword -> maskPasswordState.value = false
            Login -> login(usernameState.value, masterPasswordState.value, false)
            LoginWithBiometrics -> login(usernameState.value, masterPasswordState.value, true)
            ShowOSSLicenses -> ossLicenses.show()
        }
    }

    private fun login(username: String, password: String, requireUserAuth: Boolean) {
        loginInteractor.save(LoginInteractor.Credentials(username, password), requireUserAuth)
    }

}