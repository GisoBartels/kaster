plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("com.github.triplet.play") version "3.8.4"
}

@Suppress("UnstableApiUsage")
android {
    namespace = "app.passwordkaster.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "app.passwordkaster.android"
        minSdk = 24
        targetSdk = 33
        versionCode = buildNumber
        versionName = gitDescribeBuild

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
    testOptions {
        packagingOptions {
            // https://github.com/mockk/mockk/issues/297#issuecomment-901924678
            jniLibs.useLegacyPackaging = true
            resources.excludes += "META-INF/LICENSE*"
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
    implementation(project(":app:ui:common"))

    implementation(libs.kotlinx.collections.immutable)

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    debugImplementation(libs.showkase)
    kspDebug(libs.showkase.processor)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.kotest.assertions)
}
