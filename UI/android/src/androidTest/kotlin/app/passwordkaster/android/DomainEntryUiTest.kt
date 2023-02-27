package app.passwordkaster.android

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.text.AnnotatedString
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.common.domainentry.DomainEntry
import app.passwordkaster.common.domainentry.DomainEntryContent
import app.passwordkaster.common.domainentry.DomainEntryInput
import app.passwordkaster.common.domainentry.DomainEntryInput.*
import app.passwordkaster.common.domainentry.DomainEntryViewState
import app.passwordkaster.common.domainentry.DomainEntryViewState.GeneratedPassword
import app.passwordkaster.core.Kaster
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class DomainEntryUiTest {

    private val inputMock = mockk<(DomainEntryInput) -> Unit>(relaxed = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun viewStateIsDisplayed() {
        val expectedDomainEntry = DomainEntry(
            domain = "www.example.org",
            scope = Kaster.Scope.Recovery,
            counter = 9001,
            type = Kaster.PasswordType.Short
        )
        val expectedPassword = "secret"
        givenDomainEntry(DomainEntryViewState(expectedDomainEntry, GeneratedPassword.Result(expectedPassword)))

        composeTestRule.onNodeWithTag("domain").assertTextContains(expectedDomainEntry.domain)
        composeTestRule.onNodeWithTag("scope").onChildren().assertAny(hasText(expectedDomainEntry.scope.name))
        composeTestRule.onNodeWithTag("counter").assertTextContains(expectedDomainEntry.counter.toString())
        composeTestRule.onNodeWithTag("type").onChildren().assertAny(hasText(expectedDomainEntry.type.name))
        composeTestRule.onNodeWithTag("password").assertTextContains(expectedPassword)
    }

    @Test
    fun notEnoughDataHintIsDisplayed() {
        givenDomainEntry(DomainEntryViewState(DomainEntry(), GeneratedPassword.NotEnoughData))

        composeTestRule.onNodeWithTag("password").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("noData", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun progressWhileGeneratingIsDisplayed() {
        givenDomainEntry(DomainEntryViewState(DomainEntry("www.example.org"), GeneratedPassword.Generating))

        composeTestRule.onNodeWithTag("password").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("generating").assertIsDisplayed()
    }

    @Test
    fun testInputDomain() {
        val expectedDomain = "new.com"
        givenDomainEntry(DomainEntryViewState(DomainEntry(domain = "old.com")))

        composeTestRule.onNodeWithTag("domain").performTextReplacement(expectedDomain)

        verify { inputMock(Domain(expectedDomain)) }
    }

    @Test
    fun testInputScopeSelection() {
        val expectedScope = Kaster.Scope.Recovery
        givenDomainEntry(DomainEntryViewState(DomainEntry(scope = Kaster.Scope.Authentication)))

        composeTestRule.onNodeWithTag("scope").performClick()
        composeTestRule.onNodeWithText(expectedScope.name).performClick()

        verify { inputMock(Scope(expectedScope)) }
    }

    @Test
    fun testInputCounter() {
        val expectedCount = 101
        givenDomainEntry(DomainEntryViewState(DomainEntry(counter = 1)))

        composeTestRule.onNodeWithTag("counter").performTextReplacement(expectedCount.toString())

        verify { inputMock(Counter(expectedCount)) }
    }

    @Test
    fun testInputCounterDecrease() {
        givenDomainEntry(DomainEntryViewState(DomainEntry(counter = 0)))

        composeTestRule.onNodeWithContentDescription("Decrease").performClick()

        verify { inputMock(DecreaseCounter) }
    }

    @Test
    fun testInputCounterIncrease() {
        givenDomainEntry(DomainEntryViewState())

        composeTestRule.onNodeWithContentDescription("Increase").performClick()

        verify { inputMock(IncreaseCounter) }
    }

    @Test
    fun testInputPasswordTypeSelection() {
        val expectedType = Kaster.PasswordType.Short
        givenDomainEntry(DomainEntryViewState(DomainEntry(type = Kaster.PasswordType.Maximum)))

        composeTestRule.onNodeWithTag("type").performClick()
        composeTestRule.onNodeWithText(expectedType.name).performClick()

        verify { inputMock(Type(expectedType)) }
    }

    @Test
    fun testInputSaveWhenEnabled() {
        givenDomainEntry(DomainEntryViewState(saveEnabled = true))

        composeTestRule.onNodeWithTag("save").performClick()

        composeTestRule.onNodeWithTag("save").assertIsEnabled()
        verify { inputMock(Save) }
    }

    @Test
    fun testNoInputSaveWhenDisabled() {
        givenDomainEntry(DomainEntryViewState(saveEnabled = false))

        composeTestRule.onNodeWithTag("save").performClick()

        composeTestRule.onNodeWithTag("save").assertIsNotEnabled()
        verify(exactly = 0) { inputMock(Save) }
    }

    @Test
    fun testInputDismiss() {
        givenDomainEntry(DomainEntryViewState())

        composeTestRule.onNodeWithTag("dismiss").performClick()

        verify { inputMock(Dismiss) }
    }

    @Test
    fun testInputDelete() {
        givenDomainEntry(DomainEntryViewState())

        composeTestRule.onNodeWithTag("delete").performClick()

        verify { inputMock(Delete) }
    }

    @Test
    fun testCopyPasswordToClipboard() {
        val clipboardMock = mockk<ClipboardManager>(relaxUnitFun = true)
        val expectedPassword = "subba secret"
        composeTestRule.setContent {
            KasterTheme {
                CompositionLocalProvider(LocalClipboardManager provides clipboardMock) {
                    DomainEntryContent(
                        DomainEntryViewState(generatedPassword = GeneratedPassword.Result(expectedPassword)),
                        inputMock
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("password").performClick()

        verify { clipboardMock.setText(AnnotatedString(expectedPassword)) }
    }

    private fun givenDomainEntry(viewState: DomainEntryViewState) {
        composeTestRule.setContent {
            KasterTheme {
                DomainEntryContent(viewState, inputMock)
            }
        }
    }

}