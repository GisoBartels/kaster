package app.kaster.common.login

import app.kaster.common.login.LoginInput.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class LoginViewModel {

    private val usernameState = MutableStateFlow("")
    private val masterPasswordState = MutableStateFlow("")

    val viewState: Flow<LoginViewState> = combine(usernameState, masterPasswordState) { username, password ->
        LoginViewState(
            username = username,
            password = password,
            loginEnabled = username.isNotEmpty() && password.isNotEmpty()
        )
    }

    fun onInput(input: LoginInput) {
        when (input){
            is Username -> usernameState.value = input.value
            is MasterPassword -> masterPasswordState.value = input.value
            Login -> TODO()
        }
    }

}