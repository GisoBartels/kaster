import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.compose")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("app.cash.paparazzi")
}

kotlin {
    androidTarget()
    jvm("desktop") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries {
            framework {
                baseName = "ui"
                isStatic = true
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.app.logic)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.preview)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.showkase)
                implementation(compose.preview)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.security.crypto)
                implementation(libs.androidx.datastore)
                implementation(libs.androidx.biometric)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ossLicenses)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit4)
                implementation(libs.testparameterinjector)
                implementation(libs.showkase)
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.showkase.processor)
    add("kspAndroidTest", libs.showkase.processor)
}

android {
    namespace = "app.passwordkaster.common"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

androidComponents {
    val skipReleaseProp = providers.gradleProperty("skipRelease")
    beforeVariants { variantBuilder ->
        if (variantBuilder.buildType == "release" && skipReleaseProp.isPresent) {
            variantBuilder.enable = false
        }
    }
}

compose.desktop {
    application {
        mainClass = "app.passwordkaster.desktop.MainKt"
        nativeDistributions {
            targetFormats(Dmg, Msi, Deb)
            packageName = "Kaster Desktop"
            packageVersion = "1.0.0"
        }
    }
}
