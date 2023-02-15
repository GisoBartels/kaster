package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.Biometrics
import app.kaster.common.login.LoginInput.*
import app.kaster.common.login.LoginInteractor.Credentials
import app.kaster.common.login.LoginInteractor.LoginState
import app.kaster.common.login.LoginInteractorBiometrics
import app.kaster.common.login.LoginPersistenceNop
import app.kaster.common.login.LoginViewModel
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
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
    fun `Master password is obscured by default`() = testHarness {
        viewModel.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe true
        }
    }

    @Test
    fun `Master password is revealed on user request`() = testHarness {
        viewModel.onInput(UnmaskPassword)

        viewModel.viewState.test {
            expectMostRecentItem().passwordMasked shouldBe false
        }
    }

    @Test
    fun `Master password should be hidden on user request`() = testHarness {
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
        testScope.advanceUntilIdle()

        loginPersistence.loginState.value shouldBe LoginState.LoggedIn(Credentials("Bender", "BiteMyShinyMetalAss!"))
    }

    private class TestHarness(val testScope: TestScope) {
        val loginPersistence = LoginInteractorBiometrics(LoginPersistenceNop, Biometrics.Unsupported, testScope)
        val viewModel = LoginViewModel(loginPersistence)

        fun inputCredentials() {
            viewModel.onInput(Username("Bender"))
            viewModel.onInput(MasterPassword("BiteMyShinyMetalAss!"))
        }
    }

    private fun testHarness(block: suspend TestHarness.() -> Unit) = runTest {
        block(TestHarness(this))
    }

}
