package app.kaster.common.login

import app.kaster.common.login.LoginInput.*
import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginViewModel(private val persistence: LoginPersistence) {

    private val usernameState = MutableStateFlow(persistence.credentials.value?.username ?: "")
    private val masterPasswordState = MutableStateFlow(persistence.credentials.value?.password ?: "")
    private val maskPasswordState = MutableStateFlow(true)

    val viewState: Flow<LoginViewState> = combine(
        usernameState,
        masterPasswordState,
        maskPasswordState
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
            Login -> login(usernameState.value, masterPasswordState.value)
            MaskPassword -> maskPasswordState.value = true
            UnmaskPassword -> maskPasswordState.value = false
        }
    }

    private fun login(username: String, password: String) {
        persistence.credentials.value = LoginPersistence.Credentials(username, password)
        Navigator.navTo(Screen.DomainList)
    }

}