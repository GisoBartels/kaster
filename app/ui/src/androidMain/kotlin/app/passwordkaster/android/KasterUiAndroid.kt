package app.passwordkaster.android

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import app.passwordkaster.common.KasterRoot
import app.passwordkaster.common.KasterTheme
import app.passwordkaster.logic.RootViewModel
import app.passwordkaster.logic.domainentry.DomainEntryPersistence
import app.passwordkaster.logic.login.LoginInteractor
import app.passwordkaster.logic.login.OSSLicenses
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun KasterAndroidUi(
    loginInteractor: LoginInteractor,
    domainListPersistence: DomainEntryPersistence,
    rootViewModel: RootViewModel,
    modifier: Modifier = Modifier
) {
    KasterTheme {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            KasterRoot(rootViewModel, loginInteractor, domainListPersistence, ossLicenses)
        }
    }
}

private val ossLicenses: OSSLicenses
    @Composable
    get() = object : OSSLicenses {
        private val context = LocalContext.current

        override fun show() {
            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        }
    }
