package app.kaster.common.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal actual fun <T> DropDown(
    values: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    valueLabel: (T) -> String,
    modifier: Modifier
) {
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
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
