package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.LoginInteractor
import app.kaster.common.login.LoginInteractor.LoginState
import app.kaster.common.login.LoginInteractorInMemory
import app.kaster.common.navigation.Screen
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RootSpec {

    @Test
    fun `try to unlock credentials on start`() = runTest {
        val loginPersistence = LoginInteractorInMemory()
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.Login
        }
    }

    @Test
    fun `exit app when credentials unlock fails`() = runTest {
        val closeAppMock = mockk<() -> Unit>(relaxed = true)
        val loginInteractor = mockk<LoginInteractor> {
            every { loginState } returns MutableStateFlow(LoginState.UnlockFailed)
        }
        val viewModel = RootViewModel(closeAppMock, loginInteractor)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.Empty
        }
        verify { closeAppMock() }
    }

    @Test
    fun `login is shown when no credentials are available`() = runTest {
        val loginPersistence = LoginInteractorInMemory()
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.Login
        }
    }

    @Test
    fun `domain list is shown when credentials are available`() = runTest {
        val loginPersistence = LoginInteractorInMemory("someUser", "somePassword")
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

    @Test
    fun `domain entry is shown when requested`() = runTest {
        val expectedDomain = "example.com"
        val loginPersistence = LoginInteractorInMemory("someUser", "somePassword")
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.onInput(RootInput.ShowDomainEntry(expectedDomain))

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainEntry(expectedDomain)
        }
    }

    @Test
    fun `domain list is shown when domain entry is closed`() = runTest {
        val loginPersistence = LoginInteractorInMemory("someUser", "somePassword")
        val viewModel = RootViewModel({}, loginPersistence)
        viewModel.onInput(RootInput.ShowDomainEntry("example.com"))

        viewModel.onInput(RootInput.CloseDomainEntry)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

    @Test
    fun `close app on backpress on domain list`() = runTest {
        val closeAppMock = mockk<() -> Unit>(relaxed = true)
        val loginPersistence = LoginInteractorInMemory("someUser", "somePassword")
        val viewModel = RootViewModel(closeAppMock, loginPersistence)
        viewModel.onInput(RootInput.BackPressed)

        verify { closeAppMock() }
    }

    @Test
    fun `go back from domain entry to list on backpress`() = runTest {
        val closeAppMock = mockk<() -> Unit>()
        val loginPersistence = LoginInteractorInMemory("someUser", "somePassword")
        val viewModel = RootViewModel(closeAppMock, loginPersistence)
        viewModel.onInput(RootInput.ShowDomainEntry("example.org"))

        viewModel.onInput(RootInput.BackPressed)

        verify(exactly = 0) { closeAppMock() }
        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

}