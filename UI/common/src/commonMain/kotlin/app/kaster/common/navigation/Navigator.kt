package app.kaster.common.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object Navigator {

    private val backstack = ArrayDeque<Screen>(listOf(Screen.Login))

    private val _currentScreen: MutableStateFlow<Screen> = MutableStateFlow(Screen.Login)
    val currentScreen = _currentScreen.asStateFlow()

    fun navTo(screen: Screen) {
        backstack.addLast(screen)
        _currentScreen.value = screen
    }

    fun goBack(): Boolean {
        if (backstack.size <= 1) return false
        backstack.removeLast()
        _currentScreen.value = backstack.last()
        return true
    }

}