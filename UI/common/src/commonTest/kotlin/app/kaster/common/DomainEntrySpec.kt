package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntry
import app.kaster.common.domainentry.DomainEntryInput
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.domainentry.DomainEntryViewModel
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.login.LoginPersistenceInMemory
import app.kaster.core.Kaster
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainEntrySpec {

    @Test
    fun `New domain entry displays default values`() = runTest {
        val viewModel = DomainEntryViewModel(null, DomainEntryPersistenceInMemory(), LoginPersistenceInMemory())

        viewModel.viewState.test {
            awaitItem() shouldBe DomainEntryViewState(DomainEntry(), GeneratedPassword.NotEnoughData)
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
            awaitItem().domainEntry.domain shouldBe expectedDomain
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
    fun `Changes to scope are saved`() {
        val expectedScope = Kaster.Scope.Recovery
        val persistence = DomainEntryPersistenceInMemory(DomainEntry("www.com", scope = Kaster.Scope.Authentication))
        val viewModel = DomainEntryViewModel("www.com", persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Scope(expectedScope))
        viewModel.onInput(DomainEntryInput.Save)

        persistence.entries.value.single().scope shouldBe expectedScope
    }

    @Test
    fun `Changes to password type are saved`() {
        val expectedType = Kaster.PasswordType.Short
        val persistence = DomainEntryPersistenceInMemory(DomainEntry("www.com", type = Kaster.PasswordType.PIN))
        val viewModel = DomainEntryViewModel("www.com", persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Type(expectedType))
        viewModel.onInput(DomainEntryInput.Save)

        persistence.entries.value.single().type shouldBe expectedType
    }

    @Test
    fun `Changes to counter are saved`() {
        val expectedCounter = 100
        val persistence = DomainEntryPersistenceInMemory(DomainEntry("www.com", counter = 1))
        val viewModel = DomainEntryViewModel("www.com", persistence, LoginPersistenceInMemory())

        viewModel.onInput(DomainEntryInput.Counter(expectedCounter))
        viewModel.onInput(DomainEntryInput.Save)

        persistence.entries.value.single().counter shouldBe expectedCounter
    }

    @Test
    fun `Counter can be increased`() = runTest {
        val persistence = DomainEntryPersistenceInMemory(DomainEntry("www.com", counter = 100))
        val viewModel = DomainEntryViewModel("www.com", persistence, LoginPersistenceInMemory())

        viewModel.viewState.test {
            viewModel.onInput(DomainEntryInput.IncreaseCounter)

            awaitItem().domainEntry.counter shouldBe 101
        }
    }

    @Test
    fun `Counter can be decreased`() = runTest {
        val persistence = DomainEntryPersistenceInMemory(DomainEntry("www.com", counter = 100))
        val viewModel = DomainEntryViewModel("www.com", persistence, LoginPersistenceInMemory())

        viewModel.viewState.test {
            viewModel.onInput(DomainEntryInput.DecreaseCounter)

            awaitItem().domainEntry.counter shouldBe 99
        }
    }

    @Test
    fun `Counter cannot become zero or negative`() = runTest {
        val persistence = DomainEntryPersistenceInMemory(DomainEntry("www.com", counter = 1))
        val viewModel = DomainEntryViewModel("www.com", persistence, LoginPersistenceInMemory())

        viewModel.viewState.test {
            viewModel.onInput(DomainEntryInput.Counter(0))
            viewModel.onInput(DomainEntryInput.Counter(-1))
            viewModel.onInput(DomainEntryInput.Counter(-100))
            viewModel.onInput(DomainEntryInput.DecreaseCounter)

            awaitItem().domainEntry.counter shouldBe 1
            expectNoEvents()
        }
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