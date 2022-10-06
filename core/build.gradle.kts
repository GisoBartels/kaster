plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {

            }
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
