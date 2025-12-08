plugins {
    id("com.android.application")
    kotlin("android")
    id("com.github.triplet.play") version "3.13.0"
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.9"
}

android {
    namespace = "app.passwordkaster.android"
    compileSdk = AndroidConfig.compileSdk
    defaultConfig {
        applicationId = "app.passwordkaster.android"
        minSdk = AndroidConfig.minSdk
        targetSdk = AndroidConfig.targetSdk
        versionCode = buildNumber
        versionName = gitDescribeBuild

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}
