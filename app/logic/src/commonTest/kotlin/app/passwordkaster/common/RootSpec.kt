package app.passwordkaster.common

import app.cash.turbine.test
import app.passwordkaster.common.RootSpec.InitialState.LoggedIn
import app.passwordkaster.common.RootSpec.InitialState.LoggedInRequiringUserAuth
import app.passwordkaster.common.RootSpec.InitialState.LoggedOut
import app.passwordkaster.logic.RootInput
import app.passwordkaster.logic.RootViewModel
import app.passwordkaster.logic.login.Biometrics
import app.passwordkaster.logic.login.LoginInteractor
import app.passwordkaster.logic.login.LoginInteractorBiometrics
import app.passwordkaster.logic.navigation.Screen
import io.kotest.matchers.shouldBe
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.given
import io.mockative.mock
import io.mockative.thenDoNothing
import io.mockative.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class RootSpec {

    @Test
    fun `try to auth user on start when credentials are protected`() = testHarness(LoggedInRequiringUserAuth) {
        viewModel.viewState.test {
            testScope.runCurrent()
            cancelAndIgnoreRemainingEvents()
        }

        verify(biometricsMock).suspendFunction(biometricsMock::promptUserAuth).wasInvoked()
    }

    @Test
    fun `exit app when credentials unlock fails`() = testHarness(LoggedInRequiringUserAuth) {
        given(biometricsMock).suspendFunction(biometricsMock::promptUserAuth)
            .whenInvoked().thenReturn(Biometrics.AuthResult.Failed)

        viewModel.viewState.test {
            testScope.runCurrent()
            expectMostRecentItem().screen shouldBe Screen.Empty
        }
        verify(closeAppMock).function(closeAppMock::invoke).wasInvoked()
    }

    @Test
    fun `login is shown when no credentials are available`() = testHarness(LoggedOut) {
        viewModel.viewState.test {
            testScope.runCurrent()
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

        verify(closeAppMock).function(closeAppMock::invoke).wasInvoked()
    }

    @Test
    fun `go back from domain entry to list on backpress`() = testHarness(LoggedIn) {
        viewModel.viewState.test {
            viewModel.onInput(RootInput.ShowDomainEntry("example.org"))

            viewModel.onInput(RootInput.BackPressed)

            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
        verify(closeAppMock).function(closeAppMock::invoke).wasNotInvoked()
    }

    @Test
    fun `require user re-auth after 5 min when using biometric auth`() = testHarness(LoggedInRequiringUserAuth) {
        viewModel.viewState.test {
            testScope.advanceTimeBy(5.minutes.inWholeMilliseconds)
            testScope.runCurrent()

            verify(biometricsMock).suspendFunction(biometricsMock::promptUserAuth).wasInvoked()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `log out user after 5 min when not using biometric auth`() = testHarness(LoggedIn) {
        viewModel.viewState.test {
            testScope.advanceTimeBy(5.minutes.inWholeMilliseconds)
            testScope.runCurrent()
            expectMostRecentItem().screen shouldBe Screen.Login
        }
    }

    private class TestHarness(initialState: InitialState, val testScope: TestScope) {
        @Mock
        val biometricsMock = mock(classOf<Biometrics>()).apply {
            given(this).suspendFunction(::promptUserAuth).whenInvoked().thenReturn(Biometrics.AuthResult.Success)
            given(this).invocation { isSupported }.thenReturn(true)
        }

        @Mock
        val closeAppMock = mock(classOf<() -> Unit>()).apply {
            given(this).function(this::invoke).whenInvoked().thenDoNothing()
        }

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
            testScope.runCurrent()
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
