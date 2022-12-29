package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.login.LoginPersistenceInMemory
import app.kaster.common.navigation.Screen
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RootSpec {

    @Test
    fun `login is shown when no credentials are available`() = runTest {
        val loginPersistence = LoginPersistenceInMemory()
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.Login
        }
    }

    @Test
    fun `domain list is shown when credentials are available`() = runTest {
        val loginPersistence = LoginPersistenceInMemory("someUser", "somePassword")
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

    @Test
    fun `domain entry is shown when requested`() = runTest {
        val expectedDomain = "example.com"
        val loginPersistence = LoginPersistenceInMemory("someUser", "somePassword")
        val viewModel = RootViewModel({}, loginPersistence)

        viewModel.onInput(RootInput.ShowDomainEntry(expectedDomain))

        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainEntry(expectedDomain)
        }
    }

    @Test
    fun `domain list is shown when domain entry is closed`() = runTest {
        val loginPersistence = LoginPersistenceInMemory("someUser", "somePassword")
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
        val loginPersistence = LoginPersistenceInMemory("someUser", "somePassword")
        val viewModel = RootViewModel(closeAppMock, loginPersistence)
        viewModel.onInput(RootInput.BackPressed)

        verify { closeAppMock() }
    }

    @Test
    fun `go back from domain entry to list on backpress`() = runTest {
        val closeAppMock = mockk<() -> Unit>()
        val loginPersistence = LoginPersistenceInMemory("someUser", "somePassword")
        val viewModel = RootViewModel(closeAppMock, loginPersistence)
        viewModel.onInput(RootInput.ShowDomainEntry("example.org"))

        viewModel.onInput(RootInput.BackPressed)

        verify(exactly = 0) { closeAppMock() }
        viewModel.viewState.test {
            expectMostRecentItem().screen shouldBe Screen.DomainList
        }
    }

}