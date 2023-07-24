plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("app.cash.paparazzi")
}

kotlin {
    android {
        compilations.all {
            sourceSets.all {
                // make KSP-generated sources visible to IDE
                kotlin.srcDir("build/generated/ksp/$targetName/$name/kotlin")
            }
        }
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Kaster Common"
        homepage = "https://passwordkaster.app"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../../ios/Podfile")
        framework {
            baseName = "common"
            isStatic = true
        }
        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
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
                implementation(libs.accompanist.systemuicontroller)
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
        val iosMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                api(projects.core)
                api(compose.runtime)
            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

dependencies {
    add("kspAndroid", libs.showkase.processor)
    add("kspAndroidTest", libs.showkase.processor)
}

android {
    namespace = "app.passwordkaster.common"
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "Kaster Desktop"
            packageVersion = "1.0.0"
        }
    }
}
