package app.kaster.common.navigation

sealed interface Screen {
    object Login : Screen
    object DomainList : Screen
}