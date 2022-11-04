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
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha03")

    implementation("androidx.compose.ui:ui:1.3.0")
    implementation("androidx.compose.ui:ui-tooling:1.3.0")
    implementation("androidx.compose.foundation:foundation:1.3.0")
    implementation("androidx.compose.material:material:1.3.0")
    implementation("androidx.compose.material:material-icons-core:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.3.0")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33
    defaultConfig {
        applicationId = "app.kaster.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
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
}