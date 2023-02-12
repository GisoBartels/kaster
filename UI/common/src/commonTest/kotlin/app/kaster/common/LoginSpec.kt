package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.LoginInput.*
import app.kaster.common.login.LoginInteractor.Credentials
import app.kaster.common.login.LoginInteractor.LoginState
import app.kaster.common.login.LoginInteractorInMemory
import app.kaster.common.login.LoginViewModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `Username and password are persisted when user enabled persistence`() = testHarness {
        inputCredentials()

        viewModel.onInput(Login)

        loginPersistence.loginState.value shouldBe LoginState.LoggedIn(Credentials("Bender", "BiteMyShinyMetalAss!"))
    }

    private class TestHarness(
        credentials: Credentials?,
    ) {
        val loginPersistence = LoginInteractorInMemory(credentials).apply { unlock() }
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
    ) = testHarness(Credentials(storedUsername, storedPassword), block)

    private fun testHarness(
        credentials: Credentials? = null,
        block: suspend TestHarness.() -> Unit
    ) = runTest {
        block(TestHarness(credentials))
    }

}
