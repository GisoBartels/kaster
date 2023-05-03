package app.passwordkaster.logic.navigation

sealed interface Screen {
    object Empty : Screen
    object Login : Screen
    object DomainList : Screen
    data class DomainEntry(val domain: String?) : Screen
}
