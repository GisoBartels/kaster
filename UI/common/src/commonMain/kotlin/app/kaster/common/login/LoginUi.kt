package app.kaster.common.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Login
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        TextInput("Username", viewState.username) { input(LoginInput.Username(it)) }
        TextInput("Password", viewState.password) { input(LoginInput.MasterPassword(it)) }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        ExtendedFloatingActionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = { Text("Login") },
            icon = { Icon(Icons.Outlined.Login, "Login") },
            onClick = { TODO() }
        )
    }
}

@Composable
fun TextInput(label: String, input: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    OutlinedTextField(input, onValueChange, label = { Text(label) }, modifier = modifier.fillMaxWidth())
}
