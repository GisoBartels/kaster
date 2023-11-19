package app.passwordkaster.android.uitest

import android.app.Activity
import android.os.Bundle
import com.airbnb.android.showkase.models.Showkase

class ShowkaseLauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Showkase.getBrowserIntent(this))
    }
}
