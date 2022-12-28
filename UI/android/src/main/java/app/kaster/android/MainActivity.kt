package app.kaster.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import app.kaster.common.navigation.Navigator
import app.kaster.common.navigation.Screen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            intent?.navToDomainEntry()
        }
        setContent {
            KasterAndroidUi()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.navToDomainEntry()
    }

    private fun Intent.navToDomainEntry() {
        val host = getStringExtra(Intent.EXTRA_TEXT)?.toUri()?.simpleHost ?: return
        Navigator.navTo(Screen.DomainEntry(host))
    }

    private val Uri.simpleHost: String?
        get() = host?.removePrefix("www.")
}