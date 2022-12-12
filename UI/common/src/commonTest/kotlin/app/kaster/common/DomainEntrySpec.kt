package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntry
import app.kaster.common.domainentry.DomainEntryInput
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.domainentry.DomainEntryViewModel
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.login.LoginPersistenceInMemory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainEntrySpec {

    @Test
    fun `New domain entry displays empty values`() = runTest {
        val viewModel = DomainEntryViewModel(null, DomainEntryPersistenceInMemory(), LoginPersistenceInMemory())

        viewModel.viewState.test {
            awaitItem() shouldBe DomainEntryViewState("", GeneratedPassword.NotEnoughData)
        }
    }

    @Test
    fun `Existing domain entry displays persisted values`() = runTest {
        val expectedDomain = "www.example.com"
        val viewModel = DomainEntryViewModel(
            expectedDomain,
            DomainEntryPersistenceInMemory(DomainEntry(expectedDomain)),
            LoginPersistenceInMemory()
        )

        viewModel.viewState.test {
            awaitItem().domain shouldBe expectedDomain
        }
    }

    @Test
    fun `Changes to domain entry can be saved`() {
        val originalDomain = "original.com"
        val changedDomain = "new.com"
        val persistence = DomainEntryPersistenceInMemory(DomainEntry(originalDomain))
        val viewModel = DomainEntryViewModel(originalDomain, persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Save)

        persistence.entries.value.single().domain shouldBe changedDomain
    }

    @Test
    fun `Changes to domain entry can be dismissed`() {
        val originalDomain = "original.com"
        val changedDomain = "new.com"
        val persistence = DomainEntryPersistenceInMemory(DomainEntry(originalDomain))
        val viewModel = DomainEntryViewModel(originalDomain, persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Dismiss)

        persistence.entries.value.single().domain shouldBe originalDomain
    }

    @Test
    fun `Password is generated when domain is entered`() = runTest {
        val viewModel = DomainEntryViewModel(
            null,
            DomainEntryPersistenceInMemory(),
            LoginPersistenceInMemory("Bender", "BiteMyShinyMetalAss!")
        )

        viewModel.onInput(DomainEntryInput.Domain("benderbrau.robot"))

        viewModel.viewState.test {
            awaitItem().generatedPassword shouldBe GeneratedPassword.Generating
            awaitItem().generatedPassword shouldBe GeneratedPassword.Result("X@CPl2wOm0RgIWu#lC7/")
        }
    }

    @Test
    fun `Show empty state when domain is empty`() = runTest {
        val viewModel = DomainEntryViewModel(
            "",
            DomainEntryPersistenceInMemory(),
            LoginPersistenceInMemory("username", "masterPassword")
        )

        viewModel.viewState.test {
            awaitItem().generatedPassword shouldBe GeneratedPassword.NotEnoughData
        }
    }

    @Test
    fun `Show loadings state while password is generated`() = runTest {
        val viewModel = DomainEntryViewModel(
            "",
            DomainEntryPersistenceInMemory(),
            LoginPersistenceInMemory("username", "masterPassword")
        )

        viewModel.onInput(DomainEntryInput.Domain("www.example.com"))

        viewModel.viewState.test {
            awaitItem().generatedPassword shouldBe GeneratedPassword.Generating
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