package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.LoginInput.*
import app.kaster.common.login.LoginViewModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginSpec {

    @Test
    fun `User can log in with name and master password`() = runTest {
        val vm = LoginViewModel()

        vm.inputCredentials()

        vm.viewState.test {
            expectMostRecentItem().loginEnabled shouldBe true
        }
    }

    @Test
    fun `Master password is obscured by default`() = runTest {
        val vm = LoginViewModel().apply { inputCredentials() }

        vm.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe true
        }
    }

    @Test
    fun `Master password is be revealed on user request`() {
        TODO()
    }

    @Test
    fun `Username and password are persisted when user enabled persistence`() {
        TODO()
    }

    @Test
    fun `Username and password are cleared after 5 min, when user disabled persistence`() {
        TODO()
    }

    @Test
    fun `Username and password are cleared, when user logs out`() {
        TODO()
    }

    @Test
    fun `User can login via biometric authentication`() {
        TODO()
    }

    @Test
    fun `App navigates to domain list on successful login`() {
        TODO()
    }

    private fun LoginViewModel.inputCredentials() {
        onInput(Username("Bender"))
        onInput(MasterPassword("BiteMyShinyMetalAss!"))
    }

}
