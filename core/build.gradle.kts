import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    iosArm64 {
        binaries.framework { baseName = "core" }
        libsodium("ios")
    }
    iosSimulatorArm64 {
        binaries.framework { baseName = "core" }
        libsodium("ios-simulators")
    }

    sourceSets {
        commonMain {
            dependencies {}
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("jvmMain") {
            dependencies {
                implementation("com.password4j:password4j:1.6.0")
            }
        }
    }
}

fun KotlinNativeTarget.libsodium(libPath: String) {
    compilations.getByName("main") {
        val libsodium by cinterops.creating {
            includeDirs(file("libs/$libPath/include"))
        }
    }
    val path = file("libs/$libPath/lib").absolutePath
    binaries.all {
        linkerOpts("-L$path", "-lsodium")
    }
}
