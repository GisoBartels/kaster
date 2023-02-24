plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("app.cash.paparazzi")
}

group = "app.passwordkaster"
version = "1.0-SNAPSHOT"

kotlin {
    android {
        compilations.all {
            sourceSets.all {
                // make KSP-generated sources visible to IDE
                kotlin.srcDir("build/generated/ksp/$targetName/$name/kotlin")
            }
        }
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core)
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.materialIconsExtended)
                api(compose.preview)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.mockk)
                implementation(libs.turbine)
                implementation(libs.kotest.assertions)
            }
        }
        named("androidMain") {
            dependencies {
                implementation(libs.showkase)
            }
        }
        named("androidUnitTest") {
            dependencies {
                implementation(libs.junit4)
                implementation(libs.testparameterinjector)
                implementation(libs.showkase)
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.showkase.processor)
    add("kspAndroidTest", libs.showkase.processor)
}

@Suppress("UnstableApiUsage")
android {
    namespace = "app.passwordkaster.common"
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
