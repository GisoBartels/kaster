package app.kaster.common.login

import app.kaster.common.login.Biometrics.AuthResult.Canceled
import app.kaster.common.login.Biometrics.AuthResult.Failed
import app.kaster.common.login.Biometrics.AuthResult.Success
import app.kaster.common.login.LoginInput.Login
import app.kaster.common.login.LoginInput.LoginWithBiometrics
import app.kaster.common.login.LoginInput.MaskPassword
import app.kaster.common.login.LoginInput.MasterPassword
import app.kaster.common.login.LoginInput.UnmaskPassword
import app.kaster.common.login.LoginInput.Username
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LoginViewModel(
    private val persistence: LoginPersistence,
    private val biometrics: Biometrics,
    private val coroutineScope: CoroutineScope = MainScope()
) {
    private val usernameState = MutableStateFlow(persistence.credentials.value?.username ?: "")
    private val masterPasswordState = MutableStateFlow(persistence.credentials.value?.password ?: "")
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
            Login -> login(usernameState.value, masterPasswordState.value)
            LoginWithBiometrics -> loginWithBiometrics(usernameState.value, masterPasswordState.value)
        }
    }

    private fun login(username: String, password: String) {
        persistence.credentials.value = LoginPersistence.Credentials(username, password)
    }

    private fun loginWithBiometrics(username: String, password: String) {
        coroutineScope.launch {
            when (biometrics.promptUserAuth()) {
                Success -> login(username, password)
                Failed -> TODO()
                Canceled -> TODO()
            }
        }
    }

}