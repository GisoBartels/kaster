package app.kaster.common.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.kaster.common.login.LoginInput.MasterPassword
import app.kaster.common.login.LoginInput.UnmaskPassword

@Composable
fun LoginScreen(persistence: LoginPersistence) {
    val viewModel = remember { LoginViewModel(persistence) }
    val viewState by viewModel.viewState.collectAsState(LoginViewState())
    LoginContent(viewState, viewModel::onInput)
}

@Composable
fun LoginContent(viewState: LoginViewState, input: (LoginInput) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        UsernameField(
            modifier = Modifier.fillMaxWidth(),
            username = viewState.username,
            onUsernameChange = { input(LoginInput.Username(it)) }
        )
        PasswordField(
            modifier = Modifier.fillMaxWidth(),
            password = viewState.password,
            maskPassword = viewState.passwordMasked,
            onInput = input
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LoginButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { input(LoginInput.Login) }
        )
    }
}

@Composable
fun UsernameField(modifier: Modifier = Modifier, username: String, onUsernameChange: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Username") },
        modifier = modifier
    )
}

@Composable
fun PasswordField(modifier: Modifier = Modifier, password: String, maskPassword: Boolean, onInput: (LoginInput) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = { onInput(MasterPassword(it)) },
        label = { Text("Password") },
        modifier = modifier,
        visualTransformation = if (maskPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (maskPassword) {
                IconButton(onClick = { onInput(UnmaskPassword) }) {
                    Icon(imageVector = Icons.Filled.VisibilityOff, "Show password")
                }
            } else {
                IconButton(onClick = { onInput(LoginInput.MaskPassword) }) {
                    Icon(imageVector = Icons.Filled.Visibility, "Hide password")
                }
            }
        }
    )
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, onClick: () -> Unit) = ExtendedFloatingActionButton(
    modifier = modifier,
    text = { Text("Login") },
    icon = { Icon(Icons.Outlined.Login, "Login") },
    onClick = onClick
)