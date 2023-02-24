package app.passwordkaster.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun <T> DropDown(
    values: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    valueLabel: (T) -> String = { it.toString() },
    modifier: Modifier = Modifier
)
