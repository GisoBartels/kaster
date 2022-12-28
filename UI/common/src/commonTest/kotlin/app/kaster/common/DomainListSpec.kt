package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntry
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.domainlist.DomainListInput
import app.kaster.common.domainlist.DomainListInput.AddDomain
import app.kaster.common.domainlist.DomainListViewModel
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainListSpec {

    @Test
    fun `A logged in user sees the list of previously added domains`() = runTest {
        val domainFixtures = listOf("d1", "d2", "d3")
        val persistence = DomainEntryPersistenceInMemory(domainFixtures.map { DomainEntry(it) }.toSet())
        val viewModel = DomainListViewModel({}, persistence)

        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(domainFixtures)
        }
    }

    @Test
    fun `A logged in user can add new domain entries`() = runTest {
        val onEditDomainEntryMock = mockk<(String?) -> Unit> { every { this@mockk(any()) } just runs }
        val viewModel = DomainListViewModel(onEditDomainEntryMock, DomainEntryPersistenceInMemory())

        viewModel.onInput(AddDomain)

        verify { onEditDomainEntryMock(null) }
    }

    @Test
    fun `A logged in user can remove domain entries`() {
        TODO()
    }

    @Test
    fun `A logged in user can modify domain entries`() {
        val onEditDomainEntryMock = mockk<(String?) -> Unit> { every { this@mockk(any()) } just runs }
        val domainFixture = "www.example.org"
        val viewModel =
            DomainListViewModel(onEditDomainEntryMock, DomainEntryPersistenceInMemory(DomainEntry(domainFixture)))

        viewModel.onInput(DomainListInput.EditDomain(domainFixture))

        verify { onEditDomainEntryMock(domainFixture) }
    }

    @Test
    fun `Entries are sorted by domain name`() = runTest {
        val domainFixtures = listOf("z", "a", "c", "B")
        val persistence = DomainEntryPersistenceInMemory(domainFixtures.map { DomainEntry(it) }.toSet())
        val viewModel = DomainListViewModel({}, persistence)

        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(listOf("a", "B", "c", "z"))
        }
    }

    @Test
    fun `The list is filtered for matching domain names, when a search term is entered`() {
        TODO()
    }

}
