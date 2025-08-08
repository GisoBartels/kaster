import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

kotlin {
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
                implementation("com.password4j:password4j:1.8.4")
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
        compilerOptions.options.freeCompilerArgs.addAll(
            "-include-binary", "$path/libsodium.a"
        )
    }
    binaries.all {
        linkerOpts("-L$path", "-lsodium")
    }
}
