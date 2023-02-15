package app.kaster.common

import app.cash.turbine.test
import app.kaster.common.domainentry.DomainEntry
import app.kaster.common.domainentry.DomainEntryInput
import app.kaster.common.domainentry.DomainEntryPersistenceInMemory
import app.kaster.common.domainentry.DomainEntryViewModel
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.kaster.common.login.Biometrics
import app.kaster.common.login.LoginInteractor
import app.kaster.common.login.LoginInteractorBiometrics
import app.kaster.common.login.LoginPersistence
import app.kaster.core.Kaster
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DomainEntrySpec {

    @Test
    fun `New domain entry displays default values`() = testHarness {
        viewModel.viewState.test {
            expectMostRecentItem() shouldBe DomainEntryViewState(DomainEntry(), GeneratedPassword.NotEnoughData)
        }
    }

    @Test
    fun `Existing domain entry displays persisted values`() = testHarness(DomainEntry("www.example.com")) {
        viewModel.viewState.test {
            expectMostRecentItem().domainEntry.domain shouldBe originalDomainEntry?.domain
        }
    }

    @Test
    fun `Changes to domain entry can be saved`() = testHarness(DomainEntry("original.com")) {
        val changedDomain = "new.com"

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Save)

        domainEntryPersistence.entries.value.single().domain shouldBe changedDomain
    }

    @Test
    fun `Changes to domain entry can be dismissed`() = testHarness(DomainEntry("original.com")) {
        val changedDomain = "new.com"

        viewModel.onInput(DomainEntryInput.Domain(changedDomain))
        viewModel.onInput(DomainEntryInput.Dismiss)

        domainEntryPersistence.entries.value.single().domain shouldBe originalDomainEntry?.domain
    }

    @Test
    fun `Changes to scope are saved`() = testHarness(DomainEntry("www.com", scope = Kaster.Scope.Authentication)) {
        val expectedScope = Kaster.Scope.Recovery

        viewModel.onInput(DomainEntryInput.Scope(expectedScope))
        viewModel.onInput(DomainEntryInput.Save)

        domainEntryPersistence.entries.value.single().scope shouldBe expectedScope
    }

    @Test
    fun `Changes to password type are saved`() = testHarness(DomainEntry("www.com", type = Kaster.PasswordType.PIN)) {
        val expectedType = Kaster.PasswordType.Short

        viewModel.onInput(DomainEntryInput.Type(expectedType))
        viewModel.onInput(DomainEntryInput.Save)

        domainEntryPersistence.entries.value.single().type shouldBe expectedType
    }

    @Test
    fun `Changes to counter are saved`() = testHarness(DomainEntry("www.com", counter = 1)) {
        val expectedCounter = 100

        viewModel.onInput(DomainEntryInput.Counter(expectedCounter))
        viewModel.onInput(DomainEntryInput.Save)

        domainEntryPersistence.entries.value.single().counter shouldBe expectedCounter
    }

    @Test
    fun `Counter can be increased`() = testHarness(DomainEntry("www.com", counter = 100)) {
        viewModel.viewState.test {
            viewModel.onInput(DomainEntryInput.IncreaseCounter)

            expectMostRecentItem().domainEntry.counter shouldBe 101
        }
    }

    @Test
    fun `Counter can be decreased`() = testHarness(DomainEntry("www.com", counter = 100)) {
        viewModel.viewState.test {
            viewModel.onInput(DomainEntryInput.DecreaseCounter)

            expectMostRecentItem().domainEntry.counter shouldBe 99
        }
    }

    @Test
    fun `Counter cannot become zero or negative`() = testHarness(DomainEntry("www.com", counter = 1)) {
        viewModel.viewState.test {
            viewModel.onInput(DomainEntryInput.Counter(0))
            viewModel.onInput(DomainEntryInput.Counter(-1))
            viewModel.onInput(DomainEntryInput.Counter(-100))
            viewModel.onInput(DomainEntryInput.DecreaseCounter)

            expectMostRecentItem().domainEntry.counter shouldBe 1
            expectNoEvents()
        }
    }

    @Test
    fun `Password is generated when domain is entered`() = testHarness {
        viewModel.onInput(DomainEntryInput.Domain("benderbrau.robot"))

        viewModel.viewState.test {
            awaitItem().generatedPassword shouldBe GeneratedPassword.Generating
            awaitItem().generatedPassword shouldBe GeneratedPassword.Result("X@CPl2wOm0RgIWu#lC7/")
        }
    }

    @Test
    fun `Show empty state when domain is empty`() = testHarness(DomainEntry("www.example.com")) {
        viewModel.onInput(DomainEntryInput.Domain(""))

        viewModel.viewState.test {
            expectMostRecentItem().generatedPassword shouldBe GeneratedPassword.NotEnoughData
        }
    }

    @Test
    fun `Show loadings state while password is generated`() = testHarness {
        viewModel.onInput(DomainEntryInput.Domain("www.example.com"))

        viewModel.viewState.test {
            expectMostRecentItem().generatedPassword shouldBe GeneratedPassword.Generating
        }
    }

    @Test
    fun `Correct password is generated for non-default values for domain, scope, counter and type`() = testHarness {
        viewModel.onInput(DomainEntryInput.Domain("benderbrau.robot"))
        viewModel.onInput(DomainEntryInput.Scope(Kaster.Scope.Recovery))
        viewModel.onInput(DomainEntryInput.Counter(9001))
        viewModel.onInput(DomainEntryInput.Type(Kaster.PasswordType.Phrase))

        viewModel.viewState.test {
            awaitItem().generatedPassword shouldBe GeneratedPassword.Generating
            awaitItem().generatedPassword shouldBe GeneratedPassword.Result("gajr jev rikbeku pah")
        }
    }

    @Test
    fun `A domain entry can be deleted`() = testHarness(DomainEntry("example.com")) {
        viewModel.onInput(DomainEntryInput.Delete)

        domainEntryPersistence.entries.value.shouldBeEmpty()
        verify { onCloseEntryMock() }
    }

    private class TestHarness(
        val originalDomainEntry: DomainEntry? = null,
        testScope: TestScope
    ) {
        val onCloseEntryMock = mockk<() -> Unit> { every { this@mockk() } just runs }
        val loginPersistenceMock = mockk<LoginPersistence> {
            every { loadCredentials() } returns LoginInteractor.Credentials("Bender", "BiteMyShinyMetalAss!")
            every { userAuthenticationRequired } returns false
        }
        val domainEntryPersistence = DomainEntryPersistenceInMemory(setOfNotNull(originalDomainEntry))
        val viewModel = DomainEntryViewModel(
            originalDomainEntry?.domain,
            onCloseEntryMock,
            LoginInteractorBiometrics(
                loginPersistenceMock,
                Biometrics.Unsupported,
                testScope
            ).apply { unlock() },
            domainEntryPersistence,
            testScope.testScheduler
        )
    }

    private fun testHarness(domainEntry: DomainEntry? = null, block: suspend TestHarness.() -> Unit) = runTest {
        block(TestHarness(domainEntry, this))
    }

}
