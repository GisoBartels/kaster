plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "app.kaster"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":UI:common"))

    implementation("androidx.appcompat:appcompat:1.5.1")

    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha04")

    implementation("androidx.compose.ui:ui:1.3.1")
    implementation("androidx.compose.ui:ui-tooling:1.3.1")
    implementation("androidx.compose.foundation:foundation:1.3.1")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.material:material-icons-core:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.1")

    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.1")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.1")
    androidTestImplementation("io.mockk:mockk-android:1.13.2")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "app.kaster.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "app.kaster.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    testOptions {
        packagingOptions {
            // https://github.com/mockk/mockk/issues/297#issuecomment-901924678
            jniLibs.useLegacyPackaging = true
            resources.excludes += "META-INF/LICENSE*"
        }
    }
}