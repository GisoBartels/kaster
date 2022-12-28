package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.LoginInput.*
import app.kaster.common.login.LoginPersistence
import app.kaster.common.login.LoginPersistenceInMemory
import app.kaster.common.login.LoginViewModel
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginSpec {

    @Test
    fun `User can log in with name and master password`() = runTest {
        val vm = LoginViewModel(LoginPersistenceInMemory())

        vm.inputCredentials()

        vm.viewState.test {
            expectMostRecentItem().loginEnabled shouldBe true
        }
    }

    @Test
    fun `Master password is obscured by default`() = runTest {
        val vm = LoginViewModel(LoginPersistenceInMemory()).apply { inputCredentials() }

        vm.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe true
        }
    }

    @Test
    fun `Master password is revealed on user request`() = runTest {
        val vm = LoginViewModel(LoginPersistenceInMemory()).apply { inputCredentials() }

        vm.onInput(UnmaskPassword)

        vm.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe false
        }
    }

    @Test
    fun `Master password is be hidden on user request`() = runTest {
        val vm = LoginViewModel(LoginPersistenceInMemory()).apply { inputCredentials() }
        vm.onInput(UnmaskPassword)

        vm.onInput(MaskPassword)

        vm.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe true
        }
    }

    @Test
    fun `Username and password are restored from persistence if enabled`() = runTest {
        val storedUsername = "storedUsername"
        val storedPassword = "storedPassword"
        val vm = LoginViewModel(LoginPersistenceInMemory(storedUsername, storedPassword))

        vm.viewState.test {
            expectMostRecentItem() should { viewState ->
                viewState.username shouldBe storedUsername
                viewState.password shouldBe storedPassword
            }
        }
    }

    @Test
    fun `Username and password are persisted when user enabled persistence`() = runTest {
        val persistence = LoginPersistenceInMemory()
        val vm = LoginViewModel(persistence)

        vm.inputCredentials()

        vm.onInput(Login)

        persistence.credentials.value shouldBe LoginPersistence.Credentials("Bender", "BiteMyShinyMetalAss!")
    }

    @Test
    fun `User is logged out after 5 minutes`() {
        TODO()
    }

    @Test
    fun `User can login via biometric authentication`() {
        TODO()
    }


    private fun LoginViewModel.inputCredentials() {
        onInput(Username("Bender"))
        onInput(MasterPassword("BiteMyShinyMetalAss!"))
    }

}
