rootProject.name = "Kaster"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":core",
    ":UI:android",
    ":UI:desktop",
    ":UI:common"
)

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    plugins {
        id("app.cash.paparazzi") version "1.2.0-SNAPSHOT"
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    versionCatalogs {
        create("libs") {
            version("kotlinx-coroutines", "1.6.4")

            // common
            library("kotlinx-coroutines", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("kotlinx-coroutines")
            library("kotlinx-collections-immutable", "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

            // common test
            library("kotlinx-coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("kotlinx-coroutines")
            library("mockk", "io.mockk:mockk:1.13.2")
            library("turbine", "app.cash.turbine:turbine:0.12.0")
            library("kotest-assertions", "io.kotest:kotest-assertions-core:5.5.3")

            // JVM
            library("password4j", "com.password4j:password4j:1.6.0")

            // JVM test
            library("junit4", "junit:junit:4.13")
            library("testparameterinjector", "com.google.testparameterinjector:test-parameter-injector:1.10")

            // Android
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.5.1")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.6.1")
            library("androidx-security-crypto", "androidx.security:security-crypto-ktx:1.1.0-alpha04")
            library("androidx-datastore", "androidx.datastore:datastore:1.1.0-alpha01")
            library("androidx-compose-bom", "androidx.compose:compose-bom:2022.12.00")
            library("androidx-compose-ui", "androidx.compose.ui", "ui").withoutVersion()
            library("androidx-compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
            library("androidx-compose-material", "androidx.compose.material", "material").withoutVersion()
            library("androidx-compose-material-icons-core", "androidx.compose.material", "material-icons-core").withoutVersion()
            library("androidx-compose-material-icons-extended", "androidx.compose.material", "material-icons-extended").withoutVersion()
            library("accompanist-systemuicontroller", "com.google.accompanist:accompanist-systemuicontroller:0.27.0")

            version("showkase", "1.0.0-beta17")
            library("showkase", "com.airbnb.android", "showkase").versionRef("showkase")
            library("showkase-processor", "com.airbnb.android", "showkase-processor").versionRef("showkase")

            // Android test
            library("androidx-compose-ui-test-manifest", "androidx.compose.ui", "ui-test-manifest").withoutVersion()
            library("androidx-test-core", "androidx.test:core:1.5.0")
            library("androidx-test-runner", "androidx.test:runner:1.5.1")
            library("androidx-test-rules", "androidx.test:rules:1.5.0")
            library("androidx-compose-ui-test-junit4", "androidx.compose.ui:ui-test-junit4:1.3.2")
            library("mockk-android", "io.mockk:mockk-android:1.13.2")
        }
    }
}


