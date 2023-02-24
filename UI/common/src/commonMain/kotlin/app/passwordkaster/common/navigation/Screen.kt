package app.passwordkaster.common.navigation

sealed interface Screen {
    object Empty : Screen
    object Login : Screen
    object DomainList : Screen
    data class DomainEntry(val domain: String?) : Screen
}
