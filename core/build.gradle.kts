import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    targetHierarchy.default()

    jvm()

    iosArm64 {
        binaries.framework {
            baseName = "core"
            isStatic = true
        }
        libsodium("ios")
    }
    iosSimulatorArm64 {
        binaries.framework {
            baseName = "core"
            isStatic = true
        }
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
                implementation("com.password4j:password4j:1.7.3")
            }
        }
    }
}

fun KotlinNativeTarget.libsodium(libPath: String) {
    val path = file("libs/$libPath/lib").absolutePath
    compilations.getByName("main") {
        val libsodium by cinterops.creating {
            includeDirs(file("libs/$libPath/include"))
        }
        kotlinOptions.freeCompilerArgs = listOf(
            "-include-binary", "$path/libsodium.a"
        )
    }
    binaries.all {
        linkerOpts("-L$path", "-lsodium")
    }
}
