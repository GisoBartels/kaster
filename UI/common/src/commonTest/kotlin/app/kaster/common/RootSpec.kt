package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.RootSpec.InitialState.LoggedIn
import app.kaster.common.RootSpec.InitialState.LoggedInRequiringUserAuth
import app.kaster.common.RootSpec.InitialState.LoggedOut
import app.kaster.common.login.Biometrics
import app.kaster.common.login.LoginInteractor
import app.kaster.common.login.LoginInteractorBiometrics
import app.kaster.common.navigation.Screen
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RootSpec {

    @Test
    fun `try to auth user on start when credentials are protected`() = testHarness(LoggedInRequiringUserAuth) {
        viewModel.viewState.test {
            testScope.advanceUntilIdle()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { biometricsMock.promptUserAuth() }
    }

    @Test
    fun `exit app when credentials unlock fails`() = testHarness(LoggedInRequiringUserAuth) {
        coEvery { biometricsMock.promptUserAuth() } returns Biometrics.AuthResult.Failed

        viewModel.viewState.test {
            testScope.advanceUntilIdle()
            expectMostRecentItem().screen shouldBe Screen.Empty
        }
        verify { closeAppMock() }
    }

    @Test
    fun `login is shown when no credentials are available`() = testHarness(LoggedOut) {
        viewModel.viewState.test {
            testScope.advanceUntilIdle()
            expectMostRecentItem().screen shouldBe Screen.Login
        }
    }

    @Test
    fun `domain list is shown when credentials are available`() = testHarness(LoggedIn) {
        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

    @Test
    fun `domain entry is shown when requested`() = testHarness(LoggedIn) {
        val expectedDomain = "example.com"

        viewModel.onInput(RootInput.ShowDomainEntry(expectedDomain))

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainEntry(expectedDomain)
        }
    }

    @Test
    fun `domain list is shown when domain entry is closed`() = testHarness(LoggedIn) {
        viewModel.onInput(RootInput.ShowDomainEntry("example.com"))

        viewModel.onInput(RootInput.CloseDomainEntry)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

    @Test
    fun `close app on backpress on domain list`() = testHarness(LoggedIn) {
        viewModel.onInput(RootInput.BackPressed)

        verify { closeAppMock() }
    }

    @Test
    fun `go back from domain entry to list on backpress`() = testHarness(LoggedIn) {
        viewModel.viewState.test {
            viewModel.onInput(RootInput.ShowDomainEntry("example.org"))

            viewModel.onInput(RootInput.BackPressed)

            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
        verify(exactly = 0) { closeAppMock() }
    }

    private class TestHarness(initialState: InitialState, val testScope: TestScope) {
        val biometricsMock = mockk<Biometrics>() {
            coEvery { promptUserAuth() } returns Biometrics.AuthResult.Success
            coEvery { isSupported } returns true
        }
        val closeAppMock = mockk<() -> Unit>(relaxed = true)

        val loginPersistence = LoginPersistenceInMemory().apply {
            if (initialState == LoggedIn || initialState == LoggedInRequiringUserAuth) {
                saveCredentials(LoginInteractor.Credentials("someUser", "somePassword"))
            }
            if (initialState == LoggedInRequiringUserAuth) {
                userAuthenticationRequired = true
            }
        }

        val loginInteractor = LoginInteractorBiometrics(loginPersistence, biometricsMock, testScope)
        val viewModel = RootViewModel(closeAppMock, loginInteractor)

        fun unlockLoginPersistence() {
            loginInteractor.unlock()
            testScope.advanceUntilIdle()
        }

        init {
            if (initialState == LoggedIn) {
                unlockLoginPersistence()
            }
        }
    }

    private fun testHarness(initialState: InitialState, block: suspend TestHarness.() -> Unit) = runTest {
        block(TestHarness(initialState, this))
    }

    private enum class InitialState {
        LoggedOut, LoggedIn, LoggedInRequiringUserAuth
    }

}