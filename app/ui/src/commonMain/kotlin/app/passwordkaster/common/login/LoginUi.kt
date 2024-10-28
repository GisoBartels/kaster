package app.passwordkaster.common.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.passwordkaster.common.kasterTopAppBarColors
import app.passwordkaster.logic.login.LoginInput
import app.passwordkaster.logic.login.LoginInput.MasterPassword
import app.passwordkaster.logic.login.LoginInput.UnmaskPassword
import app.passwordkaster.logic.login.LoginInteractor
import app.passwordkaster.logic.login.LoginViewModel
import app.passwordkaster.logic.login.LoginViewState
import app.passwordkaster.logic.login.OSSLicenses

@Composable
fun LoginScreen(loginInteractor: LoginInteractor, ossLicenses: OSSLicenses) {
    val viewModel = remember { LoginViewModel(loginInteractor, ossLicenses) }
    val viewState by viewModel.viewState.collectAsState(LoginViewState())
    LoginContent(viewState, viewModel::onInput)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(viewState: LoginViewState, input: (LoginInput) -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Password Kaster") },
            colors = kasterTopAppBarColors
        )
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            UsernameField(
                modifier = Modifier.fillMaxWidth().testTag("username"),
                username = viewState.username,
                onUsernameChange = { input(LoginInput.Username(it)) }
            )
            PasswordField(
                modifier = Modifier.fillMaxWidth().testTag("password"),
                password = viewState.password,
                maskPassword = viewState.passwordMasked,
                onInput = input
            )

            Spacer(Modifier.height(2.dp))

            LoginButton(
                modifier = Modifier.align(Alignment.CenterHorizontally).testTag("login"),
                enabled = viewState.loginEnabled,
                onClick = { input(LoginInput.Login) }
            )

            if (viewState.biometricLoginEnabled) {
                LoginWithBiometrics(
                    modifier = Modifier.align(Alignment.CenterHorizontally).testTag("loginWithBiometrics"),
                    enabled = viewState.loginEnabled,
                    onClick = { input(LoginInput.LoginWithBiometrics) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Username and master password are used to generate site passwords. " +
                        "Use a strong master password and keep it at a safe place or use " +
                        "biometric login (if available) to store it encrypted on this device",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.fillMaxWidth().weight(1f))

            Button(
                onClick = { input(LoginInput.ShowOSSLicenses) },
                modifier = Modifier.align(Alignment.CenterHorizontally).testTag("OpenSourceLicenses"),
            ) {
                Text("Open Source Licenses")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameField(modifier: Modifier = Modifier, username: String, onUsernameChange: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Username") },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    password: String,
    maskPassword: Boolean,
    onInput: (LoginInput) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = { onInput(MasterPassword(it)) },
        label = { Text("Master Password") },
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
fun LoginButton(modifier: Modifier = Modifier, enabled: Boolean, onClick: () -> Unit) = Button(
    modifier = modifier,
    enabled = enabled,
    onClick = onClick
) {
    Icon(Icons.Outlined.Timer, "Login", Modifier.padding(end = 8.dp))
    Text("Login")
}

@Composable
fun LoginWithBiometrics(modifier: Modifier = Modifier, enabled: Boolean, onClick: () -> Unit) = Button(
    modifier = modifier,
    enabled = enabled,
    onClick = onClick
) {
    Icon(Icons.Outlined.Fingerprint, "Login with biometrics", Modifier.padding(end = 8.dp))
    Text("Login")
}