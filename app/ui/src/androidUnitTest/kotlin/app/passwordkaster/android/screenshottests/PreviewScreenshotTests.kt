/*
 * Dr. Ing. h.c. F. Porsche AG confidential. This code is protected by intellectual property rights.
 * The Dr. Ing. h.c. F. Porsche AG owns exclusive legal rights of use.
 */
package app.passwordkaster.android.screenshottests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.android.resources.NightMode
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import com.google.testing.junit.testparameterinjector.TestParameterValuesProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class PreviewScreenshotTests {

    class PreviewParameter(private val showkasePreview: ShowkaseBrowserComponent) {
        val content: @Composable () -> Unit = showkasePreview.component
        override fun toString(): String = showkasePreview.componentName
    }

    object PreviewProvider : TestParameterValuesProvider() {
        override fun provideValues(context: Context?): List<PreviewParameter> =
            Showkase.getMetadata().componentList.map(PreviewScreenshotTests::PreviewParameter)
    }

    @Suppress("unused")
    enum class ScreenshotTestDevice(val deviceConfig: DeviceConfig) {
        Default(DeviceConfig.PIXEL_6_PRO),
        Dark(Default.deviceConfig.copy(nightMode = NightMode.NIGHT)),
        SmallScreenBigFont(DeviceConfig.NEXUS_4.copy(fontScale = 1.5f))
    }

    @Test
    fun screenshotTests(
        @TestParameter(valuesProvider = PreviewProvider::class) showkasePreview: PreviewParameter,
        @TestParameter testDevice: ScreenshotTestDevice
    ) {
        paparazzi.unsafeUpdateConfig(testDevice.deviceConfig.copy(softButtons = false))
        paparazzi.snapshot {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                showkasePreview.content()
            }
        }
    }

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.01,
    )
}
