package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntryInput
import app.kaster.common.domainentry.DomainEntryViewModel
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainlist.DomainListPersistenceInMemory
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainEntrySpec {

    @Test
    fun `New domain entry displays empty values`() = runTest {
        val viewModel = DomainEntryViewModel(null, DomainListPersistenceInMemory())

        viewModel.viewState.test {
            expectMostRecentItem() shouldBe DomainEntryViewState("")
        }
    }

    @Test
    fun `Existing domain entry displays persisted values`() = runTest {
        val expectedDomain = "www.example.com"
        val viewModel = DomainEntryViewModel(expectedDomain, DomainListPersistenceInMemory(listOf(expectedDomain)))

        viewModel.viewState.test {
            expectMostRecentItem() shouldBe DomainEntryViewState(expectedDomain)
        }
    }

    @Test
    fun `Changes to domain entry can be saved`() {
        val originalDomain = "original.com"
        val changedDomain = "new.com"
        val persistence = DomainListPersistenceInMemory(listOf(originalDomain))
        val viewModel = DomainEntryViewModel(originalDomain, persistence)

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Save)

        persistence.domainList.value.shouldContainExactly(changedDomain)
    }

    @Test
    fun `Changes to domain entry can be dismissed`() {
        val originalDomain = "original.com"
        val changedDomain = "new.com"
        val persistence = DomainListPersistenceInMemory(listOf(originalDomain))
        val viewModel = DomainEntryViewModel(originalDomain, persistence)

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Dismiss)

        persistence.domainList.value.shouldContainExactly(originalDomain)
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