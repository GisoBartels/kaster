package app.kaster.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import app.kaster.common.KasterTheme
import app.kaster.common.domainentry.DomainEntryContent
import app.kaster.common.domainentry.DomainEntryInput
import app.kaster.common.domainentry.DomainEntryInput.Dismiss
import app.kaster.common.domainentry.DomainEntryInput.Save
import app.kaster.common.domainentry.DomainEntryViewState
import app.kaster.common.domainentry.DomainEntryViewState.GeneratedPassword
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
        val expectedDomain = "www.example.org"
        val expectedPassword = "secret"
        givenDomainEntry(DomainEntryViewState(expectedDomain, GeneratedPassword.Result(expectedPassword)))

        composeTestRule.onNodeWithTag("domain").assertTextContains(expectedDomain)
        composeTestRule.onNodeWithTag("password").assertTextContains(expectedPassword)
    }

    @Test
    fun notEnoughDataHintIsDisplayedIsDisplayed() {
        givenDomainEntry(DomainEntryViewState("", GeneratedPassword.NotEnoughData))

        composeTestRule.onNodeWithTag("password").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("noData", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun entrySaveRequest() {
        givenDomainEntry(DomainEntryViewState())

        composeTestRule.onNodeWithTag("save").performClick()

        verify { inputMock(Save) }
    }

    @Test
    fun dismissChangesRequest() {
        givenDomainEntry(DomainEntryViewState())

        composeTestRule.onNodeWithTag("dismiss").performClick()

        verify { inputMock(Dismiss) }
    }

    private fun givenDomainEntry(viewState: DomainEntryViewState) {
        composeTestRule.setContent {
            KasterTheme {
                DomainEntryContent(viewState, inputMock)
            }
        }
    }

}