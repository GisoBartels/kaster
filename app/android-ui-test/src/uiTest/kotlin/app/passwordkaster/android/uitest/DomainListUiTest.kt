package app.passwordkaster.android.uitest

import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.common.domainlist.DomainListContent
import app.passwordkaster.logic.domainlist.DomainListInput
import app.passwordkaster.logic.domainlist.DomainListViewState
import app.passwordkaster.logic.domainlist.DomainListViewState.SearchState.ShowSearch
import dev.mokkery.MockMode.autofill
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DomainListUiTest {

    private val inputMock = mock<(DomainListInput) -> Unit>(autofill)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun selectingDomainSendsEditEvent() {
        val domainFixture = "www.example.org"
        givenDomainList(domainFixture)

        composeTestRule.onNodeWithText(domainFixture).performClick()

        verify { inputMock(DomainListInput.EditDomain(domainFixture)) }
    }

    @Test
    fun logout() {
        givenDomainList()

        composeTestRule.onNodeWithContentDescription("Log out").performClick()

        verify { inputMock(DomainListInput.Logout) }
    }

    @Test
    fun startSearch() {
        givenDomainList()

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        verify { inputMock(DomainListInput.StartSearch) }
    }

    @Test
    fun stopSearch() {
        givenDomainListUiWithState(
            DomainListViewState(searchState = ShowSearch(""))
        )

        composeTestRule.onNodeWithContentDescription("Stop search").performClick()

        verify { inputMock(DomainListInput.StopSearch) }
    }

    @Test
    fun searchIsFocused() {
        givenDomainListUiWithState(
            DomainListViewState(searchState = ShowSearch(""))
        )

        composeTestRule.onNodeWithTag("search").assertIsFocused()
    }

    private fun givenDomainList(vararg domains: String) = givenDomainListUiWithState(
        DomainListViewState(domains.toList().toImmutableList())
    )

    private fun givenDomainListUiWithState(viewState: DomainListViewState) {
        composeTestRule.setContent {
            KasterTheme {
                DomainListContent(viewState, inputMock)
            }
        }
    }

}