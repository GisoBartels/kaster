package app.passwordkaster.logic.login

import app.passwordkaster.logic.login.LoginInput.Login
import app.passwordkaster.logic.login.LoginInput.LoginWithBiometrics
import app.passwordkaster.logic.login.LoginInput.MaskPassword
import app.passwordkaster.logic.login.LoginInput.MasterPassword
import app.passwordkaster.logic.login.LoginInput.UnmaskPassword
import app.passwordkaster.logic.login.LoginInput.Username
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginViewModel(private val loginInteractor: LoginInteractor) {
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
        }
    }

    private fun login(username: String, password: String, requireUserAuth: Boolean) {
        loginInteractor.save(LoginInteractor.Credentials(username, password), requireUserAuth)
    }

}