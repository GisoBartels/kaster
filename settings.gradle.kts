rootProject.name = "kaster"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":core",
    ":app:logic",
    ":app:ui",
    ":app:android",
    ":app:android-ui-test",
)

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("app.cash.paparazzi") version "2.0.0-alpha02"
        id("dev.mokkery") version "2.10.2"
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.android.gms.oss-licenses-plugin") {
                useModule("com.google.android.gms:oss-licenses-plugin:${requested.version}")
            }
        }
    }
}

plugins {
    id("com.gradle.develocity") version "4.2.2"
}

develocity {
    buildScan {
        val runsOnCI = providers.environmentVariable("CI").isPresent
        termsOfUseUrl = "https://gradle.com/help/legal-terms-of-use"
        termsOfUseAgree = "yes"
        tag("CI")
        publishing.onlyIf { runsOnCI }
        uploadInBackground = !runsOnCI
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }

    versionCatalogs {
        create("libs") {
            version("kotlinx-coroutines", "1.10.2")

            // common
            library(
                "kotlinx-coroutines",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-core"
            ).versionRef("kotlinx-coroutines")
            library(
                "kotlinx-collections-immutable",
                "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0"
            )
            library(
                "kotlinx-serialization-json",
                "org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0"
            )

            // common test
            library(
                "kotlinx-coroutines-test",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-test"
            ).versionRef("kotlinx-coroutines")

            library("turbine", "app.cash.turbine:turbine:1.2.1")
            library("kotest-assertions", "io.kotest:kotest-assertions-core:6.0.4")

            // JVM
            library("password4j", "com.password4j:password4j:1.8.4")

            // JVM test
            library("junit4", "junit:junit:4.13.2")
            library(
                "testparameterinjector",
                "com.google.testparameterinjector:test-parameter-injector:1.19"
            )

            // Android
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.7.1")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.11.0")
            library(
                "androidx-security-crypto",
                "androidx.security:security-crypto-ktx:1.1.0"
            )
            library("androidx-biometric", "androidx.biometric:biometric-ktx:1.2.0-alpha05")
            library("androidx-datastore", "androidx.datastore:datastore:1.1.7")
            library("ossLicenses", "com.google.android.gms:play-services-oss-licenses:17.3.0")

            version("showkase", "1.0.5")
            library("showkase", "com.airbnb.android", "showkase").versionRef("showkase")
            library(
                "showkase-processor",
                "com.airbnb.android",
                "showkase-processor"
            ).versionRef("showkase")

            // Android test
            library(
                "androidx-compose-ui-test-manifest",
                "androidx.compose.ui",
                "ui-test-manifest"
            ).withoutVersion()
            library("androidx-test-core", "androidx.test:core:1.7.0")
            library("androidx-test-runner", "androidx.test:runner:1.7.0")
            library("androidx-test-rules", "androidx.test:rules:1.7.0")
            library("androidx-compose-ui-test-junit4", "androidx.compose.ui:ui-test-junit4:1.9.4")
            library("robolectric", "org.robolectric:robolectric:4.16")
        }
    }
}


