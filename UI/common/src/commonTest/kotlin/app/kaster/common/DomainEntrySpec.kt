package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntryViewModel
import app.kaster.common.domainentry.DomainEntryViewState
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainEntrySpec {

    @Test
    fun `New domain entry displays empty values`() = runTest {
        val viewModel = DomainEntryViewModel(null)

        viewModel.viewState.test {
            expectMostRecentItem() shouldBe DomainEntryViewState("")
        }
    }

    @Test
    fun `Existing domain entry displays persisted values`() = runTest {
        val expectedDomain = "www.example.com"
        val viewModel = DomainEntryViewModel(expectedDomain)

        viewModel.viewState.test {
            expectMostRecentItem() shouldBe DomainEntryViewState(expectedDomain)
        }
    }

    @Test
    fun `Changes to domain entry can be saved`() {
        TODO()
    }

    @Test
    fun `Changes to domain entry can be dismissed`() {
        TODO()
    }

    @Test
    fun `Password is generated when domain is entered`() {
        TODO()
    }

    @Test
    fun `Correct password is generated for non-default values for domain, scope, counter and type`() {
        TODO()
    }

    @Test
    fun `User can copy generated password to clipboard`() {
        TODO()
    }

}