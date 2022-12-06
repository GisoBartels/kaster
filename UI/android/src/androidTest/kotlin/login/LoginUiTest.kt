package login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.kaster.common.KasterTheme
import app.kaster.common.login.LoginContent
import app.kaster.common.login.LoginInput
import app.kaster.common.login.LoginViewState
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class LoginUiTest {

    private val inputMock = mockk<(LoginInput) -> Unit>(relaxed = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginInputReceivedWhenLoginEnabled() {
        givenLoginUiWithState(
            LoginViewState(
                loginEnabled = true
            )
        )

        composeTestRule.onNodeWithText("Login").performClick()

        verify { inputMock(LoginInput.Login) }
    }

    @Test
    fun noInputWhenLoginDisabled() {
        givenLoginUiWithState(
            LoginViewState(
                loginEnabled = false
            )
        )

        composeTestRule.onNodeWithText("Login").performClick()

        verify(exactly = 0) { inputMock(LoginInput.Login) }
    }

    @Test
    fun usernameAndPasswordAreCorrectlyDisplayedWhenUnmasked() {
        val expectedUsername = "someUser"
        val expectedPassword = "12345"

        givenLoginUiWithState(
            LoginViewState(
                username = expectedUsername,
                password = expectedPassword,
                passwordMasked = false,
                loginEnabled = false
            )
        )

        composeTestRule.onNodeWithText(expectedUsername).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedPassword).assertIsDisplayed()
    }

    @Test
    fun passwordIsMasked() {
        val password = "12345"

        givenLoginUiWithState(
            LoginViewState(
                username = "username",
                password = password,
                passwordMasked = true,
                loginEnabled = false
            )
        )

        composeTestRule.onNodeWithText(password).assertDoesNotExist()
    }

    @Test
    fun unmaskInputReceivedWhenPasswordIsMasked() {
        givenLoginUiWithState(
            LoginViewState(
                passwordMasked = true
            )
        )

        composeTestRule.onNodeWithContentDescription("Hide password").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Show password").performClick()

        verify { inputMock(LoginInput.UnmaskPassword) }
    }

    @Test
    fun maskInputReceivedWhenPasswordIsUnmasked() {
        givenLoginUiWithState(
            LoginViewState(
                passwordMasked = false
            )
        )

        composeTestRule.onNodeWithContentDescription("Show password").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Hide password").performClick()

        verify { inputMock(LoginInput.MaskPassword) }
    }

    private fun givenLoginUiWithState(viewState: LoginViewState) {
        composeTestRule.setContent {
            KasterTheme {
                LoginContent(viewState, inputMock)
            }
        }
    }

}