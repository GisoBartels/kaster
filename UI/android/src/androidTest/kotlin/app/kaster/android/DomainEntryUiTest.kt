package app.kaster.android

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import app.kaster.common.KasterTheme
import app.kaster.common.domainentry.DomainEntryContent
import app.kaster.common.domainentry.DomainEntryViewState
import org.junit.Rule
import org.junit.Test

class DomainEntryUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun domainIsDisplayed() {
        val domainFixture = "www.example.org"
        givenDomainEntry(DomainEntryViewState(domainFixture))

        composeTestRule.onNodeWithTag("domain").assertTextContains(domainFixture)
    }

    private fun givenDomainEntry(viewState: DomainEntryViewState) {
        composeTestRule.setContent {
            KasterTheme {
                DomainEntryContent(viewState)
            }
        }
    }

}