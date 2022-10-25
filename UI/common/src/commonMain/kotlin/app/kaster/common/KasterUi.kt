package app.kaster.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.kaster.common.KasterInput.*
import app.kaster.core.Kaster
import app.kaster.core.Kaster.Scope

@Composable
fun KasterUi() {
    val viewModel = remember { KasterViewModel() }
    val viewState by viewModel.viewState.collectAsState(KasterViewState())
    KasterContent(viewState, viewModel::onInput)
}

@Composable
fun KasterContent(viewState: KasterViewState, input: (KasterInput) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextInput("Username", viewState.username) { input(Username(it)) }

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            value = viewState.password,
            onValueChange = { input(Password(it)) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
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

        TextInput("Domain", viewState.domain) { input(Domain(it)) }
        DropDown(
            label = "Scope",
            values = Scope.values().toList(),
            selected = viewState.scope,
            onSelected = { input(Scope(it)) }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Counter", modifier = Modifier.padding(end = 16.dp))
            OutlinedButton(onClick = { input(CounterDecrease) }, modifier = Modifier.width(48.dp)) {
                Text(
                    "-",
                    style = MaterialTheme.typography.h6
                )
            }
            Text(
                viewState.counter.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            OutlinedButton(onClick = { input(CounterIncrease) }, modifier = Modifier.width(48.dp)) {
                Text(
                    "+",
                    style = MaterialTheme.typography.h6
                )
            }
        }

        DropDown(
            label = "Password Type",
            values = Kaster.PasswordType.values().toList(),
            selected = viewState.type,
            onSelected = { input(Type(it)) }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        Button(
            onClick = { clipboardManager.setText(AnnotatedString((viewState.result))) },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewState.result.isNotEmpty()
        ) {
            Icon(Icons.Outlined.ContentCopy, "Copy password", modifier = Modifier.padding(end = 8.dp))
            Text(viewState.result.ifEmpty { "Missing data" })
        }
    }
}

@Composable
fun TextInput(label: String, input: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    OutlinedTextField(input, onValueChange, label = { Text(label) }, modifier = modifier.fillMaxWidth())
}

@Composable
fun <T> DropDown(
    label: String,
    values: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    valueLabel: (T) -> String = { it.toString() }
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.padding(end = 16.dp))
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
            var expanded by remember { mutableStateOf(false) }
            OutlinedButton(onClick = { expanded = true }) {
                Text(valueLabel(selected))
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                values.forEach { value ->
                    DropdownMenuItem(onClick = {
                        onSelected(value)
                        expanded = false
                    }) {
                        Text(valueLabel(value))
                    }
                }
            }
        }
    }
}