rootProject.name = "Kaster"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":core",
    ":app:logic",
    ":app:ui",
    ":app:android",
)

val runsOnCI = providers.environmentVariable("CI").isPresent

buildCache {
    local { isEnabled = !runsOnCI }
}

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    plugins {
        id("app.cash.paparazzi") version "1.3.1"
        id("dev.mokkery") version "1.9.0-1.1.0"
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
    id("com.gradle.enterprise") version ("3.14")
}

gradleEnterprise {
    if (runsOnCI) {
        buildScan {
            termsOfServiceUrl = "https://gradle.com/terms-of-service"
            termsOfServiceAgree = "yes"
            publishAlways()
            tag("CI")
            isUploadInBackground = false
        }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    versionCatalogs {
        create("libs") {
            version("kotlinx-coroutines", "1.7.3")

            // common
            library("kotlinx-coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinx-coroutines")
            library("kotlinx-collections-immutable", "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

            // common test
            library("kotlinx-coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("kotlinx-coroutines")

            library("turbine", "app.cash.turbine:turbine:1.0.0")
            library("kotest-assertions", "io.kotest:kotest-assertions-core:5.6.2")

            // JVM
            library("password4j", "com.password4j:password4j:1.6.3")

            // JVM test
            library("junit4", "junit:junit:4.13.2")
            library("testparameterinjector", "com.google.testparameterinjector:test-parameter-injector:1.12")

            // Android
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.6.1")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.7.2")
            library("androidx-security-crypto", "androidx.security:security-crypto-ktx:1.1.0-alpha06")
            library("androidx-biometric", "androidx.biometric:biometric-ktx:1.2.0-alpha05")
            library("androidx-datastore", "androidx.datastore:datastore:1.1.0-alpha04")
            library("accompanist-systemuicontroller", "com.google.accompanist:accompanist-systemuicontroller:0.30.1")
            library("ossLicenses", "com.google.android.gms:play-services-oss-licenses:17.0.1")

            version("showkase", "1.0.0-beta18")
            library("showkase", "com.airbnb.android", "showkase").versionRef("showkase")
            library("showkase-processor", "com.airbnb.android", "showkase-processor").versionRef("showkase")

            // Android test
            library("androidx-compose-ui-test-manifest", "androidx.compose.ui", "ui-test-manifest").withoutVersion()
            library("androidx-test-core", "androidx.test:core:1.5.0")
            library("androidx-test-runner", "androidx.test:runner:1.5.2")
            library("androidx-test-rules", "androidx.test:rules:1.5.0")
            library("androidx-compose-ui-test-junit4", "androidx.compose.ui:ui-test-junit4:1.4.3")
        }
    }
}


