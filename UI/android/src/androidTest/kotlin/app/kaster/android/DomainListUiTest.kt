package app.kaster.android

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.kaster.common.KasterTheme
import app.kaster.common.domainlist.DomainListContent
import app.kaster.common.domainlist.DomainListInput
import app.kaster.common.domainlist.DomainListViewState
import io.mockk.mockk
import io.mockk.verify
import kotlinx.collections.immutable.toImmutableList
import org.junit.Rule
import org.junit.Test

class DomainListUiTest {

    private val inputMock = mockk<(DomainListInput) -> Unit>(relaxed = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun selectingDomainSendsEditEvent() {
        val domainFixture = "www.example.org"
        givenDomainList(domainFixture)

        composeTestRule.onNodeWithText(domainFixture).performClick()

        verify { inputMock(DomainListInput.EditDomain(domainFixture)) }
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