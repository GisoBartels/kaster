plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("com.github.triplet.play") version "3.8.5"
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.6"
    id("dev.mokkery")
}

@Suppress("UnstableApiUsage")
android {
    namespace = "app.passwordkaster.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "app.passwordkaster.android"
        minSdk = 24
        targetSdk = 34
        versionCode = buildNumber
        versionName = gitDescribeBuild

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    signingConfigs {
        providers.environmentVariable("SIGN_KEY_PASSWORD").orNull?.let { signKeyPassword ->
            create("release") {
                storeFile = rootProject.file("keystore.jks")
                storePassword = signKeyPassword
                keyAlias = "kaster-upload"
                keyPassword = signKeyPassword
            }
        }
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("release")
        }
    }

    sourceSets.all {
        // make KSP-generated sources visible to IDE
        kotlin.srcDir("build/generated/ksp/$name/kotlin")
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

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    debugImplementation(libs.showkase)
    kspDebug(libs.showkase.processor)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.kotest.assertions)
}
