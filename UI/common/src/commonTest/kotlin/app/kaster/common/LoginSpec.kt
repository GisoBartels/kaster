package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.LoginInput.*
import app.kaster.common.login.LoginPersistence
import app.kaster.common.login.LoginPersistenceInMemory
import app.kaster.common.login.LoginViewModel
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginSpec {

    @Test
    fun `User can log in with name and master password`() = testHarness {
        inputCredentials()

        viewModel.viewState.test {
            expectMostRecentItem().loginEnabled shouldBe true
        }
    }

    @Test
    fun `Master password is obscured by default`() = testHarness("someUser", "somePassword") {
        viewModel.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe true
        }
    }

    @Test
    fun `Master password is revealed on user request`() = testHarness("someUser", "somePassword") {
        viewModel.onInput(UnmaskPassword)

        viewModel.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe false
        }
    }

    @Test
    fun `Master password is be hidden on user request`() = testHarness("someUser", "somePassword") {
        viewModel.onInput(UnmaskPassword)

        viewModel.onInput(MaskPassword)

        viewModel.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe true
        }
    }

    @Test
    fun `Username and password are restored from persistence if enabled`() = testHarness("someUser", "somePassword") {
        viewModel.viewState.test {
            expectMostRecentItem() should { viewState ->
                viewState.username shouldBe "someUser"
                viewState.password shouldBe "somePassword"
            }
        }
    }

    @Test
    fun `Username and password are persisted when user enabled persistence`() = testHarness {
        inputCredentials()

        viewModel.onInput(Login)

        loginPersistence.credentials.value shouldBe LoginPersistence.Credentials("Bender", "BiteMyShinyMetalAss!")
    }

    @Test
    fun `User is logged out after 5 minutes`() {
        TODO()
    }

    @Test
    fun `User can login via biometric authentication`() {
        TODO()
    }


    private class TestHarness(
        credentials: LoginPersistence.Credentials?,
        testCoroutineScheduler: TestCoroutineScheduler
    ) {
        val loginPersistence = LoginPersistenceInMemory(credentials)
        val viewModel = LoginViewModel(loginPersistence)

        fun inputCredentials() {
            viewModel.onInput(Username("Bender"))
            viewModel.onInput(MasterPassword("BiteMyShinyMetalAss!"))
        }
    }

    private fun testHarness(
        storedUsername: String,
        storedPassword: String,
        block: suspend TestHarness.() -> Unit
    ) = testHarness(LoginPersistence.Credentials(storedUsername, storedPassword), block)

    private fun testHarness(
        credentials: LoginPersistence.Credentials? = null,
        block: suspend TestHarness.() -> Unit
    ) = runTest {
        block(TestHarness(credentials, testScheduler))
    }

}
