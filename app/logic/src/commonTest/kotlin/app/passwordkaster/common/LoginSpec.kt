package app.passwordkaster.common

import app.cash.turbine.test
import app.passwordkaster.logic.login.Biometrics
import app.passwordkaster.logic.login.LoginInput.*
import app.passwordkaster.logic.login.LoginInteractor.Credentials
import app.passwordkaster.logic.login.LoginInteractor.LoginState
import app.passwordkaster.logic.login.LoginInteractorBiometrics
import app.passwordkaster.logic.login.LoginPersistenceNop
import app.passwordkaster.logic.login.LoginViewModel
import app.passwordkaster.logic.login.OSSLicenses
import dev.mokkery.MockMode.autoUnit
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
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
        testScope.runCurrent()

        loginPersistence.loginState.value shouldBe LoginState.LoggedIn(Credentials("Bender", "BiteMyShinyMetalAss!"))
    }

    @Test
    fun `Biometric login enabled when supported`() = testHarness(biometricsSupported = true) {
        inputCredentials()

        viewModel.viewState.test {
            expectMostRecentItem().biometricLoginEnabled shouldBe true
        }
    }

    @Test
    fun `Biometric login disabled when not supported`() = testHarness(biometricsSupported = false) {
        inputCredentials()

        viewModel.viewState.test {
            expectMostRecentItem().biometricLoginEnabled shouldBe false
        }
    }

    @Test
    fun `Show OSS licenses`() = testHarness {
        viewModel.onInput(ShowOSSLicenses)

        verify { ossLicenses.show() }
    }

    private class TestHarness(
        biometricsSupported: Boolean,
        val testScope: TestScope
    ) {
        val loginPersistence = LoginInteractorBiometrics(LoginPersistenceNop, BiometricsFake(biometricsSupported), testScope)
        val ossLicenses = mock<OSSLicenses>(autoUnit)
        val viewModel = LoginViewModel(loginPersistence, ossLicenses)

        fun inputCredentials() {
            viewModel.onInput(Username("Bender"))
            viewModel.onInput(MasterPassword("BiteMyShinyMetalAss!"))
        }
    }

    private fun testHarness(biometricsSupported: Boolean = false, block: suspend TestHarness.() -> Unit) = runTest {
        block(TestHarness(biometricsSupported, this))
    }

    private class BiometricsFake(private val supported: Boolean) : Biometrics {
        override suspend fun promptUserAuth(): Biometrics.AuthResult = when (supported) {
            true -> Biometrics.AuthResult.Success
            false -> Biometrics.AuthResult.Failed
        }

        override val isSupported: Boolean = supported
    }

}
