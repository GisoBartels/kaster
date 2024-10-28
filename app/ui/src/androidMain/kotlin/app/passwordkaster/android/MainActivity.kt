package app.passwordkaster.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import app.passwordkaster.logic.RootInput
import app.passwordkaster.logic.RootViewModel
import app.passwordkaster.logic.login.LoginInteractorBiometrics

class MainActivity : AppCompatActivity() {

    private val loginPersistence by lazy { LoginPersistenceAndroid(applicationContext) }
    private val loginInteractor by lazy {
        LoginInteractorBiometrics(loginPersistence, BiometricsAndroid(this), lifecycleScope)
    }
    private val domainListPersistence by lazy { DomainEntryPersistenceAndroid(applicationContext, lifecycleScope) }
    private val rootViewModel by lazy { RootViewModel({ finish() }, loginInteractor) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            intent?.navToDomainEntry()
        }
        setContent {
            KasterAndroidUi(loginInteractor, domainListPersistence, rootViewModel)
            BackHandler { rootViewModel.onInput(RootInput.BackPressed) }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.navToDomainEntry()
    }

    private fun Intent.navToDomainEntry() {
        val host = getStringExtra(Intent.EXTRA_TEXT)?.toUri()?.simpleHost ?: return
        rootViewModel.onInput(RootInput.ShowDomainEntry(host))
    }

    private val Uri.simpleHost: String?
        get() = host?.removePrefix("www.")
}
