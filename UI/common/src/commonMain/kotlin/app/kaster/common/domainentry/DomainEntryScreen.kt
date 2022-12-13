package app.kaster.common.domainentry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.kaster.common.domainentry.DomainEntryInput.*
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.login.LoginPersistence
import app.kaster.common.util.DropDown
import app.kaster.core.Kaster

@Composable
fun DomainEntryScreen(
    domain: String?,
    domainEntryPersistence: DomainEntryPersistence,
    loginPersistence: LoginPersistence
) {
    val viewModel = remember { DomainEntryViewModel(domain, domainEntryPersistence, loginPersistence) }
    val viewState by viewModel.viewState.collectAsState(DomainEntryViewState())
    DomainEntryContent(viewState, viewModel::onInput)
}

@Composable
fun DomainEntryContent(viewState: DomainEntryViewState, input: (DomainEntryInput) -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Password Kaster") },
            backgroundColor = MaterialTheme.colors.primary,
            navigationIcon = {
                IconButton(onClick = { input(Dismiss) }, modifier = Modifier.testTag("dismiss")) {
                    Icon(Icons.Outlined.Close, "Dismiss")
                }
            },
            actions = {
                IconButton(onClick = { input(Save) }, modifier = Modifier.testTag("save")) {
                    Icon(Icons.Outlined.Save, "Save")
                }
            }
        )
        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            DomainInput(value = viewState.domainEntry.domain, onValueChange = { input(Domain(it)) })
            ScopeInput(value = viewState.domainEntry.scope, onValueChange = { input(Scope(it)) })
            CounterInput(value = viewState.domainEntry.counter, input = input)
            PasswordTypeInput(value = viewState.domainEntry.type, onValueChange = { input(Type(it)) })

            Spacer(Modifier.weight(1f))

            PasswordButton(viewState.generatedPassword)
        }
    }
}

@Composable
private fun DomainInput(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Domain") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth().testTag("domain")
    )
}

@Composable
private fun ScopeInput(value: Kaster.Scope, onValueChange: (Kaster.Scope) -> Unit) {
    Label("Scope") {
        DropDown(
            values = Kaster.Scope.values().toList(),
            selected = value,
            onSelected = onValueChange,
            modifier = Modifier.testTag("scope")
        )
    }
}

@Composable
private fun CounterInput(value: Int, input: (DomainEntryInput) -> Unit) {
    OutlinedTextField(
        label = { Text("Counter") },
        value = value.toString(),
        onValueChange = { text -> text.toIntOrNull()?.let { num -> input(Counter(num)) } },
        leadingIcon = { IconButton({ input(DecreaseCounter) }) { Icon(Icons.Default.Remove, "Decrease") } },
        trailingIcon = { IconButton({ input(IncreaseCounter) }) { Icon(Icons.Default.Add, "Increase") } },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.width(140.dp).testTag("counter")
    )
}

@Composable
private fun PasswordTypeInput(value: Kaster.PasswordType, onValueChange: (Kaster.PasswordType) -> Unit) {
    Label("Password Type") {
        DropDown(
            values = Kaster.PasswordType.values().toList(),
            selected = value,
            onSelected = onValueChange,
            modifier = Modifier.testTag("type")
        )
    }
}

@Composable
private fun Label(text: String, content: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, Modifier.padding(end = 16.dp))
        content()
    }
}

@Composable
private fun PasswordButton(generatedPassword: GeneratedPassword) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Button(
        onClick = {
            if (generatedPassword is GeneratedPassword.Result)
                clipboardManager.setText(AnnotatedString(generatedPassword.password))
        },
        modifier = Modifier.fillMaxWidth().height(56.dp).testTag("password"),
        enabled = generatedPassword is GeneratedPassword.Result
    ) {
        when (generatedPassword) {
            GeneratedPassword.Generating -> LinearProgressIndicator(Modifier.testTag("generating"))
            GeneratedPassword.NotEnoughData -> Text("""¯\_(ツ)_/¯""", modifier = Modifier.testTag("noData"))
            is GeneratedPassword.Result -> {
                Icon(Icons.Outlined.ContentCopy, "Copy password", modifier = Modifier.padding(end = 8.dp))
                Text(generatedPassword.password)
            }
        }
    }
}
