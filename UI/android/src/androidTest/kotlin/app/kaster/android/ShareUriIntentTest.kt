package app.kaster.android

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen
import io.kotest.matchers.shouldBe
import org.junit.Test


class ShareUriIntentTest {

    @Test
    fun navigateToDomainEntryWhenUriIntentIsReceived() {
        val sendUriIntent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
            .putExtra(Intent.EXTRA_TEXT, "https://user:host@www.example.org/path?query")

        ActivityScenario.launch<MainActivity>(sendUriIntent)

        Navigator.currentScreen.value shouldBe Screen.DomainEntry("example.org")
    }

}