plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.compose")
    id("com.google.devtools.ksp")
    id("dev.mokkery")
}

android {
    namespace = "app.passwordkaster.android.uitest"
    compileSdk = AndroidConfig.compileSdk
    defaultConfig {
        applicationId = "app.passwordkaster.android.uitest"
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
        versionCode = 1
        versionName = "UI Test"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets.getByName("test") {
        kotlin.srcDir("src/uiTest/kotlin")
    }
    sourceSets.getByName("androidTest") {
        kotlin.srcDir("src/uiTest/kotlin")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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

dependencies {
    implementation(project(":app:ui"))

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.showkase)
    ksp(libs.showkase.processor)

    testImplementation(libs.junit4)
    testImplementation(libs.robolectric)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.kotest.assertions)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.kotest.assertions)
}
