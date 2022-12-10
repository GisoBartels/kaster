package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntryInput
import app.kaster.common.domainentry.DomainEntryViewModel
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.domainlist.DomainListPersistenceInMemory
import app.kaster.common.login.LoginPersistenceInMemory
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainEntrySpec {

    @Test
    fun `New domain entry displays empty values`() = runTest {
        val viewModel = DomainEntryViewModel(null, DomainListPersistenceInMemory(), LoginPersistenceInMemory())

        viewModel.viewState.test {
            expectMostRecentItem() shouldBe DomainEntryViewState("", GeneratedPassword.NotEnoughData)
        }
    }

    @Test
    fun `Existing domain entry displays persisted values`() = runTest {
        val expectedDomain = "www.example.com"
        val viewModel = DomainEntryViewModel(
            expectedDomain,
            DomainListPersistenceInMemory(listOf(expectedDomain)),
            LoginPersistenceInMemory()
        )

        viewModel.viewState.test {
            expectMostRecentItem().domain shouldBe expectedDomain
        }
    }

    @Test
    fun `Changes to domain entry can be saved`() {
        val originalDomain = "original.com"
        val changedDomain = "new.com"
        val persistence = DomainListPersistenceInMemory(listOf(originalDomain))
        val viewModel = DomainEntryViewModel(originalDomain, persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Save)

        persistence.domainList.value.shouldContainExactly(changedDomain)
    }

    @Test
    fun `Changes to domain entry can be dismissed`() {
        val originalDomain = "original.com"
        val changedDomain = "new.com"
        val persistence = DomainListPersistenceInMemory(listOf(originalDomain))
        val viewModel = DomainEntryViewModel(originalDomain, persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Dismiss)

        persistence.domainList.value.shouldContainExactly(originalDomain)
    }

    @Test
    fun `Password is generated when domain is entered`() = runTest {
        val viewModel = DomainEntryViewModel(
            null,
            DomainListPersistenceInMemory(),
            LoginPersistenceInMemory("Bender", "BiteMyShinyMetalAss!")
        )

        viewModel.onInput(DomainEntryInput.Domain("benderbrau.robot"))

        viewModel.viewState.test {
            skipItems(1)
            awaitItem().generatedPassword shouldBe GeneratedPassword.Result("X@CPl2wOm0RgIWu#lC7/")
        }
    }

    @Test
    fun `Show empty state when domain is empty`() = runTest {
        val viewModel = DomainEntryViewModel(
            "",
            DomainListPersistenceInMemory(),
            LoginPersistenceInMemory("username", "masterPassword")
        )

        viewModel.viewState.test {
            expectMostRecentItem().generatedPassword shouldBe GeneratedPassword.NotEnoughData
        }
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