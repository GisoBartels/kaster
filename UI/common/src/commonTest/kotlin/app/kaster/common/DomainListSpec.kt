package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainlist.DomainEntry
import app.kaster.common.domainlist.DomainListPersistenceInMemory
import app.kaster.common.domainlist.DomainListViewModel
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainListSpec {

    @Test
    fun `A logged in user sees the list of previously added domains`() = runTest {
        val domainFixtures = listOf("d1", "d2", "d3")
        val persistence = DomainListPersistenceInMemory(domainFixtures)
        val viewModel = DomainListViewModel(persistence)

        viewModel.viewState.test {
            expectMostRecentItem().domainList.shouldContainExactly(domainFixtures.map { DomainEntry(it) })
        }
    }

    @Test
    fun `A logged in user can add new domain entries`() {
        TODO()
    }

    @Test
    fun `A logged in user can remove domain entries`() {
        TODO()
    }

    @Test
    fun `A logged in user can modify domain entries`() {
        TODO()
    }

    @Test
    fun `A domain entry can be added via share intent`() {
        TODO()
    }

    @Test
    fun `When a shared domain was already added, the entry is opened instead`() {
        TODO()
    }

    @Test
    fun `Entries are sorted by domain name`() {
        TODO()
    }

    @Test
    fun `The list is filtered for matching domain names, when a search term is entered`() {
        TODO()
    }

}
