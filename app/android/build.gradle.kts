plugins {
    id("com.android.application")
    kotlin("android")
    id("com.github.triplet.play") version "3.12.1"
    id("com.google.android.gms.oss-licenses-plugin") version "0.10.6"
}

android {
    namespace = "app.passwordkaster.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "app.passwordkaster.android"
        minSdk = 24
        targetSdk = 35
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
