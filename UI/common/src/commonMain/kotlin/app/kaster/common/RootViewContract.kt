package app.kaster.common

import app.kaster.common.navigation.Screen

data class RootViewState(
    val screen: Screen
)

sealed interface RootInput {
    data class ShowDomainEntry(val domain: String?) : RootInput
    object CloseDomainEntry: RootInput
    object BackPressed: RootInput
}
