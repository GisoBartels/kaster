package app.kaster.common.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Login
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginUi() {
    val viewModel = remember { LoginViewModel() }
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
            onPasswordChange = { input(LoginInput.MasterPassword(it)) }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LoginButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { TODO() }
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
fun PasswordField(modifier: Modifier = Modifier, password: String, onPasswordChange: (String) -> Unit) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = modifier,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
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