package app.kaster.common.login

import app.kaster.common.login.LoginInput.Login
import app.kaster.common.login.LoginInput.LoginWithBiometrics
import app.kaster.common.login.LoginInput.MaskPassword
import app.kaster.common.login.LoginInput.MasterPassword
import app.kaster.common.login.LoginInput.UnmaskPassword
import app.kaster.common.login.LoginInput.Username
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginViewModel(private val persistence: LoginPersistence) {
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
            loginEnabled = username.isNotEmpty() && password.isNotEmpty()
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
        persistence.save(LoginPersistence.Credentials(username, password), requireUserAuth)
    }

}