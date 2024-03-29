package app.passwordkaster.common

import app.cash.turbine.test
import app.passwordkaster.logic.domainentry.DomainEntry
import app.passwordkaster.logic.domainentry.DomainEntryPersistenceInMemory
import app.passwordkaster.logic.domainlist.DomainListInput
import app.passwordkaster.logic.domainlist.DomainListInput.AddDomain
import app.passwordkaster.logic.domainlist.DomainListViewModel
import app.passwordkaster.logic.domainlist.DomainListViewState.SearchState
import app.passwordkaster.logic.login.Biometrics
import app.passwordkaster.logic.login.LoginInteractor.LoginState
import app.passwordkaster.logic.login.LoginInteractorBiometrics
import app.passwordkaster.logic.login.LoginPersistenceNop
import dev.mokkery.MockMode.autofill
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainListSpec {

    @Test
    fun `A logged in user sees the list of previously added domains`() = testHarness("d1", "d2", "d3") {
        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(domains)
        }
    }

    @Test
    fun `A logged in user can add new domain entries`() = testHarness {
        viewModel.onInput(AddDomain)

        verify { onEditDomainEntryMock(null) }
    }

    @Test
    fun `A logged in user can modify domain entries`() = testHarness {
        val domainFixture = "www.example.org"

        viewModel.onInput(DomainListInput.EditDomain(domainFixture))

        verify { onEditDomainEntryMock(domainFixture) }
    }

    @Test
    fun `Entries are sorted by domain name`() = testHarness("z", "a", "c", "B") {
        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(listOf("a", "B", "c", "z"))
        }
    }

    @Test
    fun `Search is started upon request`() = testHarness {
        viewModel.onInput(DomainListInput.StartSearch)

        viewModel.viewState.test {
            expectMostRecentItem().searchState shouldBe SearchState.ShowSearch("")
        }
    }

    @Test
    fun `Search is stopped upon request`() = testHarness {
        viewModel.onInput(DomainListInput.StopSearch)

        viewModel.viewState.test {
            expectMostRecentItem().searchState shouldBe SearchState.HideSearch
        }
    }

    @Test
    fun `Search is cleared when restarted`() = testHarness {
        viewModel.onInput(DomainListInput.StartSearch)
        viewModel.onInput(DomainListInput.Search(contains = "foo"))
        viewModel.onInput(DomainListInput.StopSearch)
        viewModel.onInput(DomainListInput.StartSearch)

        viewModel.viewState.test {
            expectMostRecentItem().searchState shouldBe SearchState.ShowSearch("")
        }
    }

    @Test
    fun `The list is filtered for matching domain names when a search term is entered`() =
        testHarness("foo", "fo", "oof", "afoobar", "fOo") {
            viewModel.onInput(DomainListInput.StartSearch)
            viewModel.onInput(DomainListInput.Search(contains = "foo"))

            viewModel.viewState.test {
                expectMostRecentItem() should { viewState ->
                    viewState.domainList shouldContainExactlyInAnyOrder setOf("foo", "afoobar", "fOo")
                    viewState.searchState shouldBe SearchState.ShowSearch("foo")
                }
            }
        }

    @Test
    fun `User can manually log out from domain list`() = testHarness {
        viewModel.onInput(DomainListInput.Logout)

        loginPersistence.loginState.value shouldBe LoginState.LoggedOut
    }

    private class TestHarness(val domains: Set<String>, testScheduler: TestCoroutineScheduler) {
        val onEditDomainEntryMock = mock<(String?) -> Unit>(autofill)
        val loginPersistence = LoginInteractorBiometrics(LoginPersistenceNop, Biometrics.Unsupported, CoroutineScope(testScheduler))
        val domainEntryPersistence = DomainEntryPersistenceInMemory(domains.map { DomainEntry(it) }.toSet())
        val viewModel = DomainListViewModel(
            onEditDomainEntryMock,
            loginPersistence,
            domainEntryPersistence
        )
    }

    private fun testHarness(vararg domains: String, block: suspend TestHarness.() -> Unit) = runTest {
        block(TestHarness(domains.toSet(), testScheduler))
    }

}
